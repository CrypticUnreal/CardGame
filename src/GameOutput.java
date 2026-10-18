import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


// this class  saves results of card game into the text file , so the resuts can be  inspected after  the game end 
public class GameOutput {

    //to help with testing  create a constructor  that writes files to current directory 
    private final Path outputDirectory;

    //  write files to the current directory.
    public GameOutput() {
        this(Path.of("."));
    }

    //  another ccontsructor that allows tests to choose a directory.
    public GameOutput(Path outputDirectory) {
        if (outputDirectory == null) {
            throw new IllegalArgumentException(
                "Output directory cannot be null."
            );
        }

        this.outputDirectory = outputDirectory;
    }



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
        
        
        Path p = outputDirectory.resolve(fileName);
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

        Path deckPath = outputDirectory.resolve(fileName);
        Files.writeString(deckPath, content);
    }


}