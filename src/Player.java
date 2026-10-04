import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

// TODO: week 2  objectives   player fiel  should implement player hand  management  and the players discard strategy 
//  
// we using basic synchronised aproach to prevent  a player from having  more than 5  cards at one time 
public class Player {

    public static final int HAND_SIZE = 4;// made final cause its contant based on game rules 

    // player number   determines  card number they want to collect ( player 1 wants 1s )

    private final int playerNumber;
    // keeping the hand private so other threads don't mess with it directly
    private final List<Card> hand;
    
    // keeps track of where we were when choosing what to throw away last time preventing from always favoruing same  card 
    private int discardCursor;
     

     //function to create  an empty player 

    public Player(int playerNumber) {
        if (playerNumber <= 0) {
            throw new IllegalArgumentException("Player number must be positive dude.");
        }
        this.playerNumber = playerNumber;
        this.hand = new ArrayList<>(HAND_SIZE + 1); // sometimes holds 5 before dropping
        this.discardCursor = 0;
    }
    //todo : deal with  player holding more than 4 cards 


    // Constructor when we already have cards like after dealing)
    public Player(int playerNumber, List<Card> initialHand) {
        this(playerNumber);
        Objects.requireNonNull(initialHand, "hand shouldn't be null");
        
        if (initialHand.size() != HAND_SIZE) {
            throw new IllegalArgumentException("Need exactly 4 cards to start!");
        }

        // just loop through and add them using our existing method
        for (Card c : initialHand) {
            addCard(c);
        }
    }
   // helper to get player number 
    public int getPlayerNumber() {
        return playerNumber;
    }
    // helper to get  player value 
    public int getPreferredValue() {
        return playerNumber; // same thing  based on  assinment  player 1 chooses card 1 
    }
    
    //funftion to add  one card to hand at a time 
    //  hand may shortly hold 5 cards whe a player draws before they discard but they should never hold more than 5 at same time 
    public synchronized void addCard(Card card) {
        if (card == null) {
            throw new NullPointerException("cant add a null card ");
        }//  make sure always get an existing card 

        // Jmake sure player does not have more than 5 cards ever
        if (hand.size() >= 5) {
            throw new IllegalStateException("Hand is already too full!");
        }

        hand.add(card);
    }
    // helper to  get number of cards in players hands 
    public synchronized int getHandSize() {
        return hand.size();
    }

    // helper that allows  parts of program  to read the players hand safely without  being  able to change it 
    public synchronized List<Card> getHand() {
        List<Card> copy = new ArrayList<>(hand);
        return Collections.unmodifiableList(copy);
    }

    //  function to determine winner by playe whpo has four cards of the same suit  
    // player can win  even if its not  there preferedd value 
    public synchronized boolean hasWinningHand() {
        if (hand.size() != HAND_SIZE) {
            return false;
        }
        // look at first card 
        int val = hand.get(0).getValue();
        // check if all cards match the first one
        for (int i = 1; i < hand.size(); i++) {
            if (hand.get(i).getValue() != val) {
                return false;
            }
        }
        return true;
    }

    //Todo 

}