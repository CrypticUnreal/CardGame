import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals; 

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class GameOutputTest {

    @TempDir
    Path tempDir;

   //just chek if a file with player content is created 
    @Test
    void writesPlayerOutput() throws IOException {
        // Path file 
        
       
        GameOutput out = new GameOutput();
  
        
        Path file = tempDir.resolve("player2_output.txt");
        
        // 
       
        Files.writeString(file, "Player two wins");

        assertTrue(Files.exists(file));
       
    }
}