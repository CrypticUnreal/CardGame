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


    @Test
    void playerTurnMovesCardBetweenCorrectDecks() {

        int numPlayers = 3; 

        List<CardDeck> decks = CardGame.createDecks(numPlayers);
        List<Player> players = CardGame.createPlayers(numPlayers, decks);

    // Give each player four cards
        for (int i = 0; i < numPlayers; i++) {
            for (int j = 1; j <= 4; j++) {
                players.get(i).addCard(new Card(j));
            }
        }

    // Give each deck four cards
        for (CardDeck deck : decks) {
            for (int i = 1; i <= 4; i++) {
                deck.addCard(new Card(i + 10));
            }
        }

    // Player 1 draws from deck 1 and discards into deck 2
        Player p1 = players.get(0);
        p1.performTurn();

    // checking deck sizes after the turn
        int finalDeck0Size = decks.get(0).size();
        assertEquals(
            3,
            finalDeck0Size,
            "Deck 1 should have one fewer card after Player 1 draws."
        );

        int finalDeck1Size = decks.get(1).size();
        assertEquals(
            5,
            finalDeck1Size,
            "Deck 2 should have one extra card after Player 1 discards."
        );

        int handSize = p1.getHand().size();
        assertEquals(
            4,
            handSize,
            "Player 1 should still have four cards."
        );
}
}