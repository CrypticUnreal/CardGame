//JUnit 5 test

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class CardDeckTest {

    @Test
    void cardsFIFOOrder(){
        //Create deck and add cards in this order
        CardDeck deck = new CardDeck(1);

        deck.addCard(new Card(4));
        deck.addCard(new Card(7));
        deck.addCard(new Card(2));

        //first card should be the first drawn because FIFO
        //assertEquals, First value should equal 2nd -> (4 should be the same as drawCard)
        assertEquals(4, deck.drawCard().getValue());
    }

    //Empty Deck
    @Test 
    void drawEmptyDeck(){
        CardDeck deck = new CardDeck(1);

        assertThrows(
            IllegalStateException.class,
            () -> deck.drawCard()
        );
    }

    //Null card test
    @Test
    void nullCard() {
        CardDeck deck = new CardDeck(1);

        assertThrows(
            NullPointerException.class,
            () -> deck.addCard(null)
        );
    }
    
}
