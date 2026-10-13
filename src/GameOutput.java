import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class GameOutput {

    // helper to  write text files for the players
    // wplayer number starts at 1 
    public void writePlayerOutput(int playerNumber, String content)
            throws IOException {
        
        // String fileName = "player" + playerNumber + "_output.txt";
        
        String fileName = "player" + playerNumber + "_output.txt";
        
        
        Path p = Path.of(fileName);
        Files.writeString(p, content);
    }

    public void writeDeckOutput(int deckNumber, String content)
            throws IOException {

        // Note: I remember that deck files sometimes need a specific folder, 
        // but keeping it in root for now.
        String fileName = "deck" + deckNumber + "_output.txt";

        Path deckPath = Path.of(fileName);
        Files.writeString(deckPath, content);
    }
}