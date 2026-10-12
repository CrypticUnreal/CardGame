//Import JUnit
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameStateTest {

    //Brand new game should not be finished already
    @Test 
    void NewGameNoWinner(){
        GameState gameState = new GameState();

        assertFalse(gameState.isGameOver());

        // 0 means no one won yet
        assertEquals(0, gameState.getWinner());


    }
    
    //First player who declares is the winner
    @Test 
    void firstPlayerDeclareWins(){
        GameState gameState = new GameState();

        boolean success = gameState.declareWinner(3);
        assertTrue(success);
        assertTrue(gameState.isGameOver());
        assertEquals(3, gameState.getWinner());

        }

    //When someone wins, another player should not replace them

    @Test 
    void SecondWinnerFalse(){

        GameState gameState = new GameState();
        //Player 3 wins first then 2 wins
        assertTrue(gameState.declareWinner(3));
        assertFalse(gameState.declareWinner(2));

        //3 should still be the winner
        assertEquals(3, gameState.getWinner());


}
}
