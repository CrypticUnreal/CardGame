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

    // the deck that the player draws from
    private CardDeck drawDeck;
    
    // the deck that the player discards cards into
    private CardDeck discardDeck;

    
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

    // Constructor to create player and connect them to input and output decks

    public Player(int playerNumber, CardDeck drawDeck, CardDeck discardDeck){
        // Create player first
        this(playerNumber);

        // Player must always have decks to draw and discard into
        this.drawDeck = Objects.requireNonNull(
            drawDeck, "Draw deck can not be null");

        //discard deck
        this.discardDeck = Objects.requireNonNull(
            discardDeck, "Discard deck can not be null");
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

    // function to dicard snon prefereed hand 
    // Throws away a card that isn't the one we want
    public synchronized Card discardNonPreferredCard() {
        if (hand.isEmpty()) {
            throw new IllegalStateException("can't discard from empty hand");
        }

        int size = hand.size();
        
        // Loop around starting from discardCursor
        for (int i = 0; i < size; i++) {
            int idx = (discardCursor + i) % size;
            Card candidate = hand.get(idx);

            //  checks if number is our preffered number if not it get rid of it 
            if (candidate.getValue() != getPreferredValue()) {
                Card removed = hand.remove(idx);
                
                // update cursor, startig next search at th eposition after ht e removed card 
                // removing cards immediately shifts cards to the left mainting original order 
                if (hand.isEmpty()) {
                    discardCursor = 0;
                } else {
                    discardCursor = idx % hand.size();
                }

                return removed;
            }
        }
        

        // If we get here, it means all cards in hand are our preferred value... 
        // which shouldn't happen unless we already won, but just in case:
        throw new IllegalStateException("Player " + playerNumber + " has nothing to discard!!");
    }

    // synchronise funtion 
    // adds a drawn card and  then removes  a non preffered  card ensuring that a player always ends up with four cards  
    // makes operations on players hand atomic  with all  othher methods affecting playes hand 
    //having an error with  testing because  checks are in different order need to  change this  to check  if card is null first  then check if  has four cards 
    public synchronized Card processDrawnCard(Card drawnCard) {

        // check if drawn card  is null 
        if ( drawnCard == null ){
            throw new NullPointerException(" card cannot be null");
        }
        if (hand.size() != 4) {
            // we should always start a turn with 4 cards
            throw new IllegalStateException("Wait, hand size isn't 4 at start of turn?");
        }

        addCard(drawnCard);
        Card thrownAway = discardNonPreferredCard();

        // double check we are back to 4 cards
        if (hand.size() != 4) {
            System.out.println("ERROR: Hand size is incorrect  after turn processing!");
        }

        return thrownAway;
    }

    //Perform one complete player turn, draw from player's draw deck, process card, choose one to discard, add discard to next deck.
    public void performTurn() {
        //take card from front of deck
        Card drawnCard = drawDeck.drawCard();

        //add to hand and pick discarded card
        Card discardedCard = processDrawnCard(drawnCard);

        //Put unwanted card to back of next deck
        discardDeck.addCard(discardedCard);
    }
    
    // output  screen for the  cards  sperated by spaces 
    // returns  values in the crrent order for example a player has hand with 1287  it will return 1 2 8 7 

    public synchronized String handAsString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < hand.size(); i++) {
            if (i > 0) {
                sb.append(" ");
            }
            sb.append(hand.get(i).getValue());
        }
        return sb.toString();
    }
}