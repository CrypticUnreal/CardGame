import java.util.List;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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


        //give each player cards

        //player 1 gets winning hand
        players.get(0).addCard(new Card(1));
        players.get(0).addCard(new Card(1));
        players.get(0).addCard(new Card(1));
        players.get(0).addCard(new Card(1));

        //player 2 gets hand
        players.get(1).addCard(new Card(2));
        players.get(1).addCard(new Card(3));
        players.get(1).addCard(new Card(1));
        players.get(1).addCard(new Card(4));

        //player 3 gets hand
        players.get(2).addCard(new Card(1));
        players.get(2).addCard(new Card(3));
        players.get(2).addCard(new Card(2));
        players.get(2).addCard(new Card(1));

        // Give each deck four cards
    for (CardDeck deck : decks) {
        for (int i = 1; i <= 4; i++) {
        deck.addCard(new Card(i));
        }
    }
        

        //Start all the player threads
        List<Thread> threads = CardGame.startPlayerThreads(players);


        //test fails if waiting for threads takes longer than 5 seconds 
        assertTimeoutPreemptively(
            Duration.ofSeconds(5),
            () -> CardGame.waitForPlayerThreads(threads)
        );

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