/**tests to  do  in cardgameintegration
 * test that pack is read  and cards  are delat correclty 
 * make sure that players draw from correct deck and  discard to the right deck 
 * the game handles winning correclty
 * output files are generated correctly 
 * game finish with  expected results and and correct card count  */ 


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class CardGameIntegrationTest {

    // TODO: 
    //  number of decks should be equal to number of players 
    @Test
    void createsCorrectNumberOfDecks() {
        int numberOfPlayers = 3; //

        CardGame game = new CardGame();
        List<CardDeck> decks = game.createDecks(numberOfPlayers);

        assertNotNull(decks, "decks list shouldn't be null!!");
        assertEquals(numberOfPlayers, decks.size());
    }
    
    @Test
    void createsCorrectNumberOfPlayers() {
        int numberOfPlayers = 3;

        CardGame game = new CardGame();
        List<CardDeck> decks = game.createDecks(numberOfPlayers);
        List<Player> players = game.createPlayers(numberOfPlayers, decks); 

        assertNotNull(players);
        assertEquals(numberOfPlayers, players.size());
    }
    
    @Test
    void playersStartWithFourCards() {
        
        List<Card> initialHand = new ArrayList<>();
        initialHand.add(new Card(1));
        initialHand.add(new Card(2));
        initialHand.add(new Card(3));
        initialHand.add(new Card(4));

        Player player = new Player(1, initialHand);

        assertEquals(4, player.getHand().size());
        // assertEquals(4, player.getNumCards()); 
    }
    //make sure game recognises winnin ghand  of four cards of the same type 
    @Test
    void canTellWinningHand() {
        // let's test 4 of a kind wins
        List<Card> winningHand = new ArrayList<>();
        winningHand.add(new Card(5));
        winningHand.add(new Card(5));
        winningHand.add(new Card(5));
        winningHand.add(new Card(5));

        Player player = new Player(1, winningHand);

        assertTrue(player.hasWinningHand());
        

    }
}