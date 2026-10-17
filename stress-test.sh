

# TOD
RUNS=100
PLAYER_COUNT=${1:-3}
TIME_LIMIT=10 # seconds

RESULTS_FILE="stress-test-results.txt"
FAILURES_FILE="stress-test-failures.txt"
PACK_FILE="stress-test-pack.txt"

PASSED=0
FAILED=0
TIMED_OUT=0

# make sure player count is a positive
if ! [[ "$PLAYER_COUNT" =~ ^[1-9][0-9]*$ ]]; then
    echo "Hey, player count needs to be a positive integer!"
    exit 1
fi

# timeout command 
if command -v gtimeout >/dev/null 2>&1; then
    TIMEOUT_CMD="gtimeout"
elif command -v timeout >/dev/null 2>&1; then
    TIMEOUT_CMD="timeout"
else
    echo "Error: timeout command not found. If you're on a Mac, just do 'brew install coreutils'"
    exit 1
fi

# compile first or we go nowhere
echo "Compiling java stuff..."
# mvn clean compile? nah just compile is fine
if ! mvn -q compile; then
    echo "Build failed!! Check your java code."
    exit 1
fi

# generate pack file using python bc python is easier for file IO here xD
python3 - "$PLAYER_COUNT" "$PACK_FILE" <<'PYTHON'
import sys

num_players = int(sys.argv[1])
out_name = sys.argv[2]

# 8 cards per player is standard for this game
cards_list = list(range(1, 8 * num_players + 1))

with open(out_name, "w") as f:
    for c in cards_list:
        f.write(str(c) + "\n")
PYTHON

echo "Starting stress tests now..."
echo "Players count: $PLAYER_COUNT"
echo "Total runs: $RUNS"
echo "Time limit: ${TIME_LIMIT}s per game"

# init result files
cat << EOF > "$RESULTS_FILE"
Stress Test Results
Players: $PLAYER_COUNT
Total runs: $RUNS
------------------------------
EOF

cat << EOF > "$FAILURES_FILE"
Stress Test Failures
Players: $PLAYER_COUNT
------------------------------
EOF

# clean old outputs just in case
rm -f player*_output.txt deck*_output.txt

for ((i = 1; i <= RUNS; i++)); do

    echo "Running game number $i..."

    # clear logs from previous iteration
    rm -f player*_output.txt deck*_output.txt

    # pipe player count and pack file path into the java program
    GAME_OUTPUT=$(
        printf '%s\n%s\n' "$PLAYER_COUNT" "$PACK_FILE" |
        "$TIMEOUT_CMD" "$TIME_LIMIT" java -cp target/classes CardGame 2>&1
    )
    EXIT_CODE=$?

    REASON=""

    # check exit status codes for timeouts (124 or 137)
    if [ "$EXIT_CODE" -eq 124 ] || [ "$EXIT_CODE" -eq 137 ]; then
        REASON="TIMEOUT: took longer than ${TIME_LIMIT} seconds!"
        TIMED_OUT=$((TIMED_OUT + 1))

    elif [ "$EXIT_CODE" -ne 0 ]; then
        REASON="ERROR: java crashed with exit code $EXIT_CODE"

    # make sure someone actually won and only ONE person won
    elif [ "$(printf '%s\n' "$GAME_OUTPUT" | grep -Eic 'player [0-9]+ wins')" -ne 1 ]; then
        REASON="ERROR: expected exactly one winner, got something else"

    else
        # check if player output files were generated
        for ((p = 1; p <= PLAYER_COUNT; p++)); do
            if [ ! -f "player${p}_output.txt" ]; then
                REASON="ERROR: player${p}_output.txt is missing!"
                break
            fi
        done

        # check deck output files too
        if [ -z "$REASON" ]; then
            for ((d = 1; d <= PLAYER_COUNT; d++)); do
                if [ ! -f "deck${d}_output.txt" ]; then
                    REASON="ERROR: deck${d}_output.txt is missing!"
                    break
                fi
            done
        fi

        # verify everybody has 4 cards at the end
        if [ -z "$REASON" ]; then
            for ((p = 1; p <= PLAYER_COUNT; p++)); do

                FINAL_HAND=$(grep -E "^player ${p} final hand:" "player${p}_output.txt" 2>/dev/null | tail -n 1)

                # sometimes formatting drops the space... handle that
                if [ -z "$FINAL_HAND" ]; then
                    FINAL_HAND=$(grep -E "^player ${p}final hand:" "player${p}_output.txt" 2>/dev/null | tail -n 1)
                fi

                if [ -z "$FINAL_HAND" ]; then
                    REASON="ERROR: couldn't find final hand for player $p"
                    break
                fi

                CARD_VALUES=$(printf '%s\n' "$FINAL_HAND" | sed 's/.*final hand:[[:space:]]*//')
                CARD_COUNT=$(printf '%s\n' "$CARD_VALUES" | awk '{print NF}')

                if [ "$CARD_COUNT" -ne 4 ]; then
                    REASON="ERROR: player $p finished with $CARD_COUNT cards instead of 4 :("
                    break
                fi
            done
        fi
    fi

    # log everything
    if [ -z "$REASON" ]; then
        PASSED=$((PASSED + 1))
        echo "Game $i: PASS" >> "$RESULTS_FILE"
    else
        FAILED=$((FAILED + 1))
        echo "Game $i: FAIL -> $REASON" >> "$RESULTS_FILE"
        
        # dumping failure details into the failure log
        {
            echo "Game $i: $REASON"
            echo "Output was:"
            printf '%s\n' "$GAME_OUTPUT"
            echo "------------------------------"
        } >> "$FAILURES_FILE"
    fi

done

# summary at the bottom of results file
{
    echo "------------------------------"
    echo "Passed: $PASSED"
    echo "Failed: $FAILED"
    echo "Timed out: $TIMED_OUT"
} >> "$RESULTS_FILE"

echo ""
echo "All done testing!"
echo "Passed: $PASSED / $RUNS"
echo "Failed: $FAILED"
echo "Timeouts: $TIMED_OUT"
echo "Results written to $RESULTS_FILE"

# cleanup temp pack file
rm -f "$PACK_FILE"

if [ "$FAILED" -gt 0 ]; then
    exit 1
fi

exit 0