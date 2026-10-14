//test whether  every completed game declares exaclty one winner 
import static org.junit.jupiter.api.Assertions.assertEquals;


import org.junit.jupiter.api.Test;

public class WinnerTest {

    // quick test to make sure game state handles declaring a winner properly
    @Test
    void exactlyOneWinnerIsDeclared() {
        GameState gameState = new GameState();

        // just picking player 1 as a dummy winner for the test
        Player winner = new Player(1);

        // Record the winner.
        gameState.declareWinner(winner);
        
     

        // Exactly one winner should be recorded.
        assertEquals(1, gameState.getWinnerCount());
        
       
    }
}