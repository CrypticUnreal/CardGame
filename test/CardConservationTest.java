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

            List<Card> hand = new ArrayList<>();

            // filling up hands for the test
            for (int j = 0; j < 4; j++) {
                hand.add(new Card(i + j));
            }

            Player player = new Player(i + 1, hand);
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

        // Count every card in the players' hands.
        int totalCards = 0;

        for (Player player : players) {
            totalCards += player.getHand().size();
        }

        // Count every card in the decks.
      
        for (CardDeck deck : decks) {
            // adding deck size to total
            totalCards += deck.size();
        }

        // The total must equal the original number of cards.
        assertEquals(
                expectedTotalCards,
                totalCards,
                "Cards must not be lost or duplicated."
        );
        
        
    }
}