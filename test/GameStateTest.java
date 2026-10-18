//Import JUnit
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class GameStateTest {

    //Brand new game should not be finished already
    @Test 
    void NewGameNoWinner(){
        GameState gameState = new GameState();

        assertFalse(gameState.isGameOver());

        // 0 means no one won yet
        assertEquals(0, gameState.getWinner());


    }
    
    // Check that zero and negative player numbers cannot be winners
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

     
    @Test// Check that zero and negative player numbers cannot be winners
    void rejectsInvalidWinnerNumber() {
        GameState gameState = new GameState();

            // Player numbers must be positive
        assertThrows(
            IllegalArgumentException.class,
            () -> {
             gameState.declareWinner(0);
            }
            );


        assertThrows(
            IllegalArgumentException.class,
            () -> {
              gameState.declareWinner(-1);
            }
        );

    // Invalid declarations must not end the game
        boolean gameOver = gameState.isGameOver();
            assertFalse(gameOver);
    
        int winningPlayer = gameState.getWinner();
            assertEquals(0, winningPlayer);
    }

  
    // adding 5s timeout should be plenty.
    @Test
    void onlyOneWinnerWhenThreadsCompete() throws InterruptedException {

        GameState gameState = new GameState();

    // Both threads wait until they are allowed to start
        CountDownLatch startLatch = new CountDownLatch(1);

    // Count how many players successfully declare a winner
        AtomicInteger successfulWinners = new AtomicInteger(0);

    // Let's set up thread 1
        Thread player1 = new Thread(() -> {
            try {
                startLatch.await();

                boolean won = gameState.declareWinner(1);
                if (won) {
                    successfulWinners.incrementAndGet();
                }

            } catch (InterruptedException e) {
            // restore interrupt flag just in case
                Thread.currentThread().interrupt();
            }
        });

    // And thread 2 (basically the same thing)
        Thread player2 = new Thread(() -> {
            try {
                startLatch.await();

            
                boolean won = gameState.declareWinner(2);
                if (won) {
                    successfulWinners.incrementAndGet();
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // Start both threads
        player1.start();
        player2.start();

    // Allow both threads to attempt to declare a winner
        startLatch.countDown();

    // Wait for both threads, but not indefinitely
        player1.join(5000);
        player2.join(5000);

    // Check both threads have finished
        assertFalse(player1.isAlive(), "Player 1 thread should finish.");
        assertFalse(player2.isAlive(), "Player 2 thread should finish.");

    // Exactly one player should have successfully declared a winner
        int totalWinners = successfulWinners.get();
        assertEquals(
            1,
            totalWinners,
            "Only one player should successfully declare a winner."
        );

    // The game should be finished
        assertTrue(gameState.isGameOver());

    // Either player 1 or player 2 can be the winner
        int winner = gameState.getWinner();
        boolean validWinner = (winner == 1 || winner == 2);

        assertTrue(
            validWinner,
            "The winner must be player 1 or player 2."
        );
}
}
