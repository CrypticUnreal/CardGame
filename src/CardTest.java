
//Import JUnit
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class CardTest {
    @Test 
    void negativeCardReject(){
        //Expect newCard(-1) to not be accepted
        assertThrows(
            IllegalArgumentException.class,
            () -> new Card(-1)

        );
    }
}
