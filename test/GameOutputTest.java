import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class GameOutputTest {

    // JUnit temp dir
    @TempDir
    Path tempDir;

    @Test
    void writesPlayerOutput() throws IOException {
        GameOutput output = new GameOutput(tempDir);

        String expectedContent = "Player 2 wins";

        // Let's test writing out player 2's result
        output.writePlayerOutput(2, expectedContent);

        Path file = tempDir.resolve("player2_output.txt");

        // make sure it actually made the file
        assertTrue(Files.exists(file));

        // read it back and verify
        String actual = Files.readString(file);
        assertEquals(expectedContent, actual);
    }

    @Test
    void writesDeckOutput() throws IOException {
        GameOutput output = new GameOutput(tempDir);

        String expectedContent = "deck 1 contents: 3 5 7";

        output.writeDeckOutput(1, expectedContent);

        Path file = tempDir.resolve("deck1_output.txt");

        assertTrue(Files.exists(file));
        assertEquals(expectedContent, Files.readString(file));
    }

    // error handling tests 
    
    @Test
    void rejectsInvalidPlayerNumber() {
        GameOutput output = new GameOutput(tempDir);

        // p
        assertThrows(
            IllegalArgumentException.class,
            () -> {
                output.writePlayerOutput(0, "Player wins");
            }
        );
    }
    // check it rejcts  invalid deck number 
    @Test
    void rejectsInvalidDeckNumber() {
        GameOutput output = new GameOutput(tempDir);

        assertThrows(
            IllegalArgumentException.class,
            () -> output.writeDeckOutput(-1, "Deck contents")
        );
    }
    // check if it rejects null player 
    @Test
    void rejectsNullPlayerContent() {
        GameOutput output = new GameOutput(tempDir);

        assertThrows(
            IllegalArgumentException.class,
            () -> output.writePlayerOutput(1, null)
        );
    }
   // check it rejcts null deckContent 
    @Test
    void rejectsNullDeckContent() {
        GameOutput output = new GameOutput(tempDir);

        assertThrows(
            IllegalArgumentException.class,
            () -> output.writeDeckOutput(1, null)
        );
    }
    

}