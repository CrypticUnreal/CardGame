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
        public static List<Player> createPlayers(int numberOfPlayers,List<CardDeck> decks, GameState gameState) {

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


         //OLD VERSION KEEP FOR TESTS!!

         


        

         public static List<Player> createPlayers(int numberOfPlayers, List<CardDeck> decks){
            GameState gameState = new GameState();

            return createPlayers(numberOfPlayers, decks, gameState);
         }

         //one thread for every player
         public static List<Thread> startPlayerThreads(List<Player> players){
            List<Thread> threads = new ArrayList<>();

            for(Player player : players){
                Thread thread = new Thread(player, "Player - " + player.getPlayerNumber());

                threads.add(thread);
                thread.start();
            }
            return threads;
         }

         //WAIT until every thread has stopped
         public static void waitForPlayerThreads(List<Thread>threads) {
            for (Thread thread: threads){
                try{
                    thread.join();

                }catch (InterruptedException e){
                    Thread.currentThread().interrupt();

                    throw new IllegalStateException("Interrupted while waiting for players", e);
                }
            }
             
         }


    }