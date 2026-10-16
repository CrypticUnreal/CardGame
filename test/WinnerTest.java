//test whether  every completed game declares exaclty one winner 
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class WinnerTest {

    // quick test to make sure game state handles declaring a winner properly
    @Test
    void exactlyOneWinnerIsDeclared() {
        GameState gameState = new GameState();

        // just picking player 1 as a dummy winner for the test
        assertTrue(gameState.declareWinner(1));


        // lets say player 2 tries to declare after
        assertFalse(gameState.declareWinner(2));

        // game should be finished
        assertTrue(gameState.isGameOver());


        // Player 1 should be the only winner
        assertEquals(1,gameState.getWinner());
        
    
        
       
    }
}