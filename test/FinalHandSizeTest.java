import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;

import org.junit.jupiter.api.Test;

public class FinalHandSizeTest {

    // test to check if threads finish correctly and everyone still has 4 cards
    @Test
    void everyPlayerFinishesWithFourCards() {
        int numberOfPlayers = 3; 

        GameState gameState = new GameState();

        // decks setup
        List<CardDeck> decks = CardGame.createDecks(numberOfPlayers);

        // players setup
        List<Player> players = CardGame.createPlayers(
            numberOfPlayers, decks, gameState
        );

        
        List<Thread> threads = CardGame.startPlayerThreads(players);

        
        CardGame.waitForPlayerThreads(threads);

        assertFalse(players.isEmpty(), "The game should have players.");

        // check everyone's final hand size
        for (Player player : players) {
            
            assertEquals(
                4,
                player.getHand().size(),
                "Player " + player.getPlayerNumber()
                    + " must finish with exactly four cards."
            );
        }
    }
}