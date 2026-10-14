

# Quick stress-test script to see if the game hangs or deadlocks.
TODO: add correct path once create  main 
RUNS=100
PLAYER_COUNT=${1:-3}  # default to 3 players if nothing passed in
RESULTS_FILE="stress-test-results.txt"

# keep track of pass/fail 
PASSED=0
FAILED=0

echo "Starting stress tests..."
echo "Players: $PLAYER_COUNT"
echo "Runs: $RUNS"
 
# init the log file
echo "Stress Test Results" > "$RESULTS_FILE"
echo "Players: $PLAYER_COUNT" >> "$RESULTS_FILE"
echo "Total runs: $RUNS" >> "$RESULTS_FILE"
echo "------------------------------" >> "$RESULTS_FILE"

for ((i = 1; i <= RUNS; i++)); do

    # echoing to terminal just to see progress so it doesn't look frozen
    echo "Running game $i of $RUNS..."



    echo "Till we create Main" >> "$RESULTS_FILE"

done

echo ""
echo "Stress testing finished. (Well, simulation finished anyway)"
echo "Results saved to $RESULTS_FILE"