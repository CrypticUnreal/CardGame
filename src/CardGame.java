/*Need to implement the ring system for the card deck so it loops around
 so player 1 draws from 1 and discard to 2 ...... up to player 4 draw from 4 and discard to 1
*/
import java.util.ArrayList;
import java.util.List;


public class CardGame {
        public static List<CardDeck> createDecks(int numberOfPlayers) {
            List<CardDeck> decks = new ArrayList<>();

            //if i players,
            //create deck 1 to i
            for (int i = 1; i<= numberOfPlayers; i++){
                decks.add(new CardDeck(i));
            }
            return decks;
        }

        // create all players and connect them to correct draw and discard
        public static List<Player> createPlayers(
            int numberOfPlayers,
            List<CardDeck> decks) {

        List<Player> players = new ArrayList<>();

        for (int i = 0; i < numberOfPlayers; i++){
            //Player i draw from deck i
            CardDeck drawDeck = decks.get(i);

            //Player discards to next deck
            //Use mod % to wrap back around
            CardDeck discardDeck = decks.get((i+1) % numberOfPlayers);

            //Create the player
            Player player = new Player(i+1, drawDeck, discardDeck);

            players.add(player);

        }
        return players;
            }

    }