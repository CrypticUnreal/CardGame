
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

// Test suite for the Player class.  testing  players  hand and discard strategy 
// Added these tests to make sure the discard rotation and hand logic aren't totally broken.
public class PlayerTest {

    // helper method to make lists of cards in tests
    private List<Card> cards(int... values) {
        List<Card> l = new ArrayList<>();
        for (int v : values) {
            l.add(new Card(v));
        }
        return l;
    }

    @Test
    void constructorAcceptsExactlyFourCards() {
        Player p = new Player(2, cards(2, 3, 2, 4));

        assertEquals(4, p.getHandSize(), "Hand size should be 4 right after init");
        assertEquals("2 3 2 4", p.handAsString());
    }

    @Test
    void constructorRejectsNonPositivePlayerNumber() {
        // playerId 0 or negative shouldn't be allowed
        assertThrows(IllegalArgumentException.class, () -> {
            new Player(0);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new Player(-99);
        });
    }

    @Test
    void constructorRejectsHandThatIsNotFourCards() {
        // too few cards
        assertThrows(IllegalArgumentException.class, () -> {
            new Player(1, cards(1, 2, 3));
        });

        // too many cards
        assertThrows(IllegalArgumentException.class, () -> {
            new Player(1, cards(1, 2, 3, 4, 5));
        });
    }

    @Test
    void winningHandRequiresExactlyFourEqualValues() {
        Player winner = new Player(2, cards(7, 7, 7, 7));
        Player nonWinner = new Player(2, cards(7, 7, 7, 6));
        
        Player incomplete = new Player(2);
        incomplete.addCard(new Card(7));
        incomplete.addCard(new Card(7));
        incomplete.addCard(new Card(7));
        // check that a player can win with any four same cards 
        assertTrue(winner.hasWinningHand());
        assertFalse(nonWinner.hasWinningHand());
        assertFalse(incomplete.hasWinningHand(), "incomplete hand shouldn't win!");
    }

    @Test
    void processDrawnCardNeverDiscardsPreferredDenomination() {
        Player p = new Player(2, cards(2, 5, 2, 6));

        Card discarded = p.processDrawnCard(new Card(2));

        // make sure it didn't throw away a 2, since player 2 wants to keep 2s
        assertTrue(discarded.getValue() != 2);
        assertEquals(4, p.getHandSize());

        // Count how many preferred cards are left using loop
        int prefCount = 0;
        for(Card c : p.getHand()) {
            if(c.getValue() == 2) {
                prefCount++;
            }
        }
        
        assertEquals(3, prefCount, "Should have kept all preferred cards");
    }

    @Test
    void drawnNonPreferredCardCanBeDiscarded() {
        Player p = new Player(1, cards(1, 1, 1, 2));

        Card firstDiscard = p.processDrawnCard(new Card(3));
        Card secondDiscard = p.processDrawnCard(new Card(4));
       //strategy rotates through eligible cards  after two 3 is the new eligble card and should not be held indefinately 
        assertEquals(2, firstDiscard.getValue());
        assertEquals(3, secondDiscard.getValue());
        assertEquals(4, p.getHandSize());
    }

    @Test
    void repeatedTurnsRotateThroughNonPreferredCards() {
        Player p = new Player(1, cards(2, 3, 4, 5));

        List<Integer> discarded = new ArrayList<>();

        // doing it one by one
        discarded.add(p.processDrawnCard(new Card(6)).getValue());
        discarded.add(p.processDrawnCard(new Card(7)).getValue());
        discarded.add(p.processDrawnCard(new Card(8)).getValue());
        discarded.add(p.processDrawnCard(new Card(9)).getValue());

        // Check if round-robin works properly
        //This directly tests the no-indefinite-hoarding requirements 
        assertEquals(Arrays.asList(2, 3, 4, 5), discarded);
        assertEquals(4, p.getHandSize());
    }

    @Test
    void completedTurnAlwaysLeavesFourCards() {
        Player p = new Player(4, cards(4, 1, 2, 3));

        // run a bunch of turns just to stress test the size constraint
        // every completed action should leave  4 cards 
        for (int i = 5; i < 50; i++) {
            p.processDrawnCard(new Card(i));
            assertEquals(4, p.getHandSize());
        }
    }
     // checks the player cannot  start playing unless they have four cards 
    @Test
    void processingTurnRejectsIncorrectStartingHandSize() {
        Player p = new Player(1);
        p.addCard(new Card(1)); // only 1 card, should fail

        assertThrows(IllegalStateException.class, () -> {
            p.processDrawnCard(new Card(2));
        });
    }
    
    @Test
    void discardingFailsWhenEveryCardIsPreferred() {
        Player p = new Player(3, cards(3, 3, 3, 3));

        assertTrue(p.hasWinningHand());

        // If hand is all preferred cards, trying to discard should  throw exception
        assertThrows(IllegalStateException.class, () -> {
            p.discardNonPreferredCard();
        });
    }

    @Test
    void returnedHandCannotModifyInternalState() {
        Player p = new Player(1, cards(1, 2, 3, 4));

        List<Card> exposedHand = p.getHand();

        // Trying to add a card to the returned list should be rejectedp
        assertThrows(UnsupportedOperationException.class, () -> {
            exposedHand.add(new Card(5));
        });
            // confirm rplayers real hand remains unchanged 
        assertEquals(4, p.getHandSize());
        assertEquals("1 2 3 4", p.handAsString());
    }

    @Test
    void nullCardsAreRejected() {
        Player p = new Player(1);
        //A null card must not be added directly to the players hand 
        assertThrows(NullPointerException.class, () -> {
            p.addCard(null);
        });
         // a null drawn card is also rejected 
        assertThrows(NullPointerException.class, () -> {
            p.processDrawnCard(null);
        });
    }
}