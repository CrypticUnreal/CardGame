import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class PlayerCardDeckTest {
    @Test 
    void playerDrawAndDiscard(){
        //Create starting hand [1,1,5,1]
        //not preferred is 5 so discard

        List<Card> startingHand = new ArrayList<>();

        startingHand.add(new Card(1));
        startingHand.add(new Card(1));
        startingHand.add(new Card(5));
        startingHand.add(new Card(1));

        Player player1 = new Player(1, startingHand);
        
        //Create 2 decks - draw from 1 discard to 2

        CardDeck deck1 = new CardDeck(1);
        CardDeck deck2 = new CardDeck(2);

        //Put cards into deck1 (FIFO so first draw is 1)

        deck1.addCard(new Card(1));
        deck1.addCard(new Card(7));
        deck1.addCard(new Card(8));

        //draw from deck 1
        Card drawnCard = deck1.drawCard();
        assertEquals(1, drawnCard.getValue());

        //Process drawn card (temp 5 cards in hand)
        Card discardedCard = player1.processDrawnCard(drawnCard);

        assertEquals(5, discardedCard.getValue());

        //Put discarded card to deck 2
        deck2.addCard(discardedCard);

        //Now should have 4 cards in hand
        assertEquals(4, player1.getHandSize());

        //Should have 4 1s
        assertEquals("1 1 1 1", player1.handAsString());
        //they win
        assertTrue(player1.hasWinningHand());




    }
    //Now must implement so player knows their 2 decks so not manual.


    @Test 
    void playerTurnTest() {
        //initialise decks
        CardDeck deck1 = new CardDeck(1);
        CardDeck deck2 = new CardDeck(2);

        // draw from deck 1 and discard to 2
        Player player1 = new Player(1,deck1,deck2);

        //Starting hand [1,1,5,1]
        player1.addCard(new Card(1));
        player1.addCard(new Card(1));
        player1.addCard(new Card(5));
        player1.addCard(new Card(1));

        // 2 random cards in deck1 - 1 and 3, first card to draw is a 1
        deck1.addCard(new Card(1));
        deck1.addCard(new Card (3));

        //player performs draw and discard action
        player1.performTurn();

        //Should discard unwanted 5, and wins
        assertEquals("1 1 1 1", player1.handAsString());
        assertEquals(4, player1.getHandSize());
        assertTrue(player1.hasWinningHand());

        //Deck 1 had 2 cards, player drew 1
        assertEquals(1, deck1.size());
        
        // discarded 5 should now be in deck 2
        assertEquals(1, deck2.size());
        assertEquals(5, deck2.drawCard().getValue());
    }
}
