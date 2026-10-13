import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


// this class  saves results of card game into the text file , so the resuts can be  inspected after  the game end 
public class GameOutput {

    // helper to  write text files for the players
    // wplayer number starts at 1 
    public void writePlayerOutput(int playerNumber, String content)
            throws IOException {
        
        // String fileName = "player" + playerNumber + "_output.txt";
        if (playerNumber <= 0) {
            throw new IllegalArgumentException("Player number must be positive.");
        }
        if (content == null) {
            throw new IllegalArgumentException("Player output content cannot be null.");
        }
        String fileName = "player" + playerNumber + "_output.txt";
        
        
        Path p = Path.of(fileName);
        Files.writeString(p, content);
    }

    public void writeDeckOutput(int deckNumber, String content)
            throws IOException {

        if (deckNumber <= 0) {
            throw new IllegalArgumentException("Deck number must be positive.");
        }
        if (content == null) {
            throw new IllegalArgumentException("Deck output content can't be null .");
        }

     
        String fileName = "deck" + deckNumber + "_output.txt";

        Path deckPath = Path.of(fileName);
        Files.writeString(deckPath, content);
    }


}