/*
Must know what deck it is
Store Card objects
remove from front and add to back (FIFO)
 */

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;


public class CardDeck {
    //Identify deck
    private final int deckNumber;

    //Store card inside deck. (dequeue for FIFO)
    private final Deque<Card> cards;

    //Constructor create deck
    public CardDeck(int deckNumber){
        this.deckNumber = deckNumber;
        this.cards = new ArrayDeque<>();
    }

    //Return the number of the deck
    public int getDeckNumber() {
        return deckNumber;
    }

    /*
    Add a card to BACK
    (synchronised) protects the deck from being modified by 2 players at once, makes one wait.
     */
    public synchronized void addCard(Card card) {

        //deck can not contain null value
        if(card == null){
            throw new NullPointerException(
                "Can not add a null card to the deck."
            );

        }

        cards.addLast(card);
    }

    //Remove and return card from Front
    public synchronized Card drawCard() {
        // handle empty deck
        if (cards.isEmpty()){
            throw new IllegalStateException("Can not draw from an empty deck."
            );
        }
        return cards.removeFirst();
    }

    //Return how many cards in deck
    public synchronized int size() {
        return cards.size();
    }

    //Return a copy of cards in deck
    public synchronized List<Card> getCards() {
        return new ArrayList<>(cards);
    }

    //Print deck easier for testing
    @Override
    public synchronized String toString() {
        return cards.toString();
    }
}