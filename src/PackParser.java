import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PackParser {

    private final String filePath;

    /**
     *  Create  parser for a file .

     */
    public PackParser(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "File path cannot be empty or null."
            );
        }

        this.filePath = filePath;
    }

     // reading and validating a pack 
    public List<Card> readPack(int numPlayers) throws IOException {

        if (numPlayers <= 0) {
            throw new IllegalArgumentException(
                "Number of players must be positive."
            );
        }

        List<Card> cards = new ArrayList<>();

        try (BufferedReader reader =
                 new BufferedReader(new FileReader(filePath))) {

            String line;
            int lineNumber = 0;
             
             // reading line by line 
            while ((line = reader.readLine()) != null) {

                lineNumber++;

                line = line.trim();

                // Reject  a line if its empty 
                if (line.isEmpty()) {
                    throw new IllegalArgumentException(
                        "Empty line at line " + lineNumber
                    );
                }

                int value;

                try {
                    value = Integer.parseInt(line);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException(
                        "Invalid card value at line " + lineNumber
                    );
                }

                //make sure card values are  non-negative.
                if (value < 0) {
                    throw new IllegalArgumentException("Negative card value at line " + lineNumber);
                }

                //  changing the  numbers into an actual card object 
                cards.add(new Card(value));

                // Max card checks 
                if (cards.size() > 8 * numPlayers) {
                    throw new IllegalArgumentException("Pack contains too many cards.");
                }
            }

        }
         // final size  check 
        //  pack must contain 8n cards 
         int expectedSize = 8 * numPlayers;
        if (cards.size() != expectedSize) {
            throw new IllegalArgumentException("Invalid pack size. Expected " + expectedSize + " but got " + cards.size());
        }

        return cards;
    }
}