import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;


/**Test suite for the PackParser class.  testing  pack  follows all the rules 
 * no empty lines in a pack 
 * checking all values are non negative 
 * checking does no  pack exceed contains  8n cards 
 * parser rejects too few cards 
 *  rejects non numeric values 
 * rejects zero players 
 * accepts valid packs 
 * rejects zero cards 
**/

public class PackParserTest{
    //helper method to make packs that rae being tested 
    // since we want many  different packs to test this will help not having to recreate the  pack creating code over and over again 
    @TempDir
    Path tempDir;

    
    private Path createPackFile(String content) throws IOException {
        //  putting it in the temp dir 
        Path packFile = tempDir.resolve("pack.txt");
        Files.writeString(packFile, content);
        return packFile;
        }
    
    //test for empty lines in pack 
    @Test
    void  rejectsemptyline() throws IOException{
         Path file = createPackFile("1\n2\n3\n4\n \n6\n7\n8\n");

         PackParser  parser = new PackParser(file.toString());
         
         assertThrows(IllegalArgumentException.class,()->{parser.readPack(1)});
         }

    // test   for all  values to be non negative 
    @Test 
    void  rejectsnegativenumbers() throws IOException{
        Path file = createPackFile(("1\n2\n-3\n4\n5\n6\n7\n8\n"));
        
        PackParser  parser = new PackParser(file.toString());

        assertThrows(IllegalArgumentException.class,()->{parser.readPack(1)});
         }
    
    // test  to check packs  do not exceed  8n cards  where n is number of players 
    @Test
    void packsDoNotExceedEightN() throws IOException {
        Path file = createPackFile("1\n2\n3\n4\n5\n6\n7\n8\n9\n");

        PackParser parser = new PackParser(file.toString());

        assertThrows(IllegalArgumentException.class, () -> {
            parser.readPack(1);
        });
    }

    // test  to check parser rejects to few cards  less than 8n
    @Test
    void toofewcards() throws IOException {
        Path file = createPackFile("1\n2\n3\n4\n5\n6\n7\n");

        PackParser parser = new PackParser(file.toString());

        assertThrows(IllegalArgumentException.class, () -> {
            parser.readPack(1);
        });
    }
    // test  to check it rejects non numeric values 
    @Test
    void rejectsnonnumericnvalues() throws IOException {
        Path file = createPackFile("1\n2\n3\n4\n hi \n6\n7\n8\n");

        PackParser parser = new PackParser(file.toString());

        assertThrows(IllegalArgumentException.class, () -> {
            parser.readPack(1);
        });
    }
        // test  to check it rejects zero players  in argument 
    @Test
    void rejectszeroplayers () throws IOException {
        Path file = createPackFile("1\n2\n3\n4\n5\n6\n7\n8\n");

        PackParser parser = new PackParser(file.toString());

        assertThrows(IllegalArgumentException.class, () -> {
            parser.readPack(0);
        });
    }

    // rejects zero cards 
    @Test
    void rejectsEmptyFile() throws IOException {
        Path file = createPackFile("");

        PackParser parser = new PackParser(file.toString());

        assertThrows(IllegalArgumentException.class, () -> {
         parser.readPack(1);
    });
}

        // test  to  checks it accepts valid  pack  
    @Test
    void accepstsvalidpack() throws IOException {
        Path file = createPackFile("1\n2\n3\n4\n5\n6\n7\n8\n");

        PackParser parser = new PackParser(file.toString());

        List<Card> cards = parser.readPack(1);

        assertEquals(8, cards.size());
    }
}
