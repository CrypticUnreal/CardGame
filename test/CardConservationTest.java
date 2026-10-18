import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class CardConservationTest {

    @Test
    void allCardsAreAccountedFor() {

        int numberOfPlayers = 3;
        // 8 cards per player total (4 in hand + 4 in deck?)
        int expectedTotalCards = 8 * numberOfPlayers;

        // Create three decks.
        CardGame game = new CardGame();
        List<CardDeck> decks = game.createDecks(numberOfPlayers);

        // Create three players, each with four cards.
        List<Player> players = new ArrayList<>();

        for (int i = 0; i < numberOfPlayers; i++) {

            // Connect the player to their draw and discard decks
            Player player = new Player(
                i + 1,
                decks.get(i),
                decks.get((i + 1) % numberOfPlayers)
            );

            // Give each player four cards
            for (int j = 0; j < 4; j++) {
                player.addCard(new Card(i + j + 1));
            }

            players.add(player);
        }

        // Put the remaining twelve cards into the decks.
        int cardValue = 10; 

        for (CardDeck deck : decks) {
            for (int i = 0; i < 4; i++) {
                deck.addCard(new Card(cardValue));
                cardValue++;
            }
        }

        // Count every card in the players' hands. efore player takes a turn 
        int cardsBefore = 0;

        for (Player player : players) {
            cardsBefore += player.getHand().size();
        }

        // Count every card in the decks.
      
        for (CardDeck deck : decks) {
            cardsBefore += deck.size();
        }

        // The total must equal the original number of cards.
        assertEquals(
            expectedTotalCards,
            cardsBefore,
            "There should be 24 cards before any turns."
        );
        
        // Player 1 performs one turn
        players.get(0).performTurn();
        // Count all cards after the turn
        int cardsAfter = 0;

        for (Player player : players) {
            cardsAfter += player.getHand().size();
        }

        for (CardDeck deck : decks) {
            cardsAfter += deck.size();
        }

        // Check that no cards were lost or duplicated
        assertEquals(
        cardsBefore,
        cardsAfter,
        "Total number of cards must stay the same after a turn."
        );

        // Check that Player 1 still has four cards
        assertEquals(
          4,
         players.get(0).getHand().size(),
         "Player 1 must still have four cards after a turn."
        );
    }
}