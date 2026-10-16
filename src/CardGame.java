/*Need to implement the ring system for the card deck so it loops around
 so player 1 draws from 1 and discard to 2 ...... up to player 4 draw from 4 and discard to 1
*/
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


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

        // create all players and connect them to the deck ring
        public static List<Player> createPlayers(int numberOfPlayers,List<CardDeck> decks, GameState gameState) {

        List<Player> players = new ArrayList<>();

        for (int i = 0; i < numberOfPlayers; i++){
            //Player i draw from deck i
            CardDeck drawDeck = decks.get(i);

            //Player discards to next deck
            //Use mod % to wrap back around
            CardDeck discardDeck = decks.get((i+1) % numberOfPlayers);

            //Create the player
            Player player = new Player(i+1, drawDeck, discardDeck, gameState);

            players.add(player);

        }
        return players;
         }


         //OLD VERSION KEEP FOR TESTS!!

         


        

         public static List<Player> createPlayers(int numberOfPlayers, List<CardDeck> decks){
            GameState gameState = new GameState();

            return createPlayers(numberOfPlayers, decks, gameState);
         }

         // deal pack according to rules
         //First 4n cards round robin
         //Remaining 4n round robin to decks

         public static void dealCards(List<Card> pack, List<Player> players, List<CardDeck> decks){
            int numberOfPlayers = players.size();

            if (decks.size() != numberOfPlayers) {
                throw new IllegalArgumentException(
                    "Number of decks have to match number of players"
                );

            }

            if (pack.size() != 8 * numberOfPlayers) {
                throw new IllegalArgumentException(
                    "Packs must contain EXACTLY 8 cards per player");
            }

         int cardIndex = 0;

         //Give every player 4 cards round robin
         for (int round = 0; round < 4; round++){
            for (int playerIndex = 0; playerIndex < numberOfPlayers; playerIndex ++){
                players.get(playerIndex).addCard(pack.get(cardIndex));

                cardIndex++;
            }
         }

         //Give every deck 4 cards round robin
         for(int round = 0; round < 4; round++){

            for(int deckIndex = 0; deckIndex< numberOfPlayers; deckIndex++){
                decks.get(deckIndex).addCard(pack.get(cardIndex));

                cardIndex++;
            }
         }
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

         //constantly ask until valid +ve number entered
         private static int askForNumberOfPlayers(Scanner scanner){
            while (true) { 
                System.out.print("Please enter the number of players: ");
                String input = scanner.nextLine().trim();

                try {
                    int numberOfPlayers = Integer.parseInt(input);

                    if (numberOfPlayers > 0) {
                        return numberOfPlayers;
                    }
                } catch (NumberFormatException e){
                    //Invalid input handled under
                }
                System.out.println("Number of players must be a positive integer");

            }
         }

         // Keep asking until packparser accepts file
         private static List<Card> askForPack(Scanner scanner, int numberOfPlayers){
            while(true){
                System.out.println("Please enter location of pack to load:");

                String filePath = scanner.nextLine().trim();

                try {
                    PackParser parser = new PackParser(filePath);
                    return parser.readPack(numberOfPlayers);
                } catch (IOException | IllegalArgumentException e) {
                    System.out.println("Invalid pack:" + e.getMessage());
                }
            }
         }

         //convert deck to simple space separated values
         private static String cardsAsString(List<Card> cards){
            StringBuilder result = new StringBuilder();

            for (int i = 0; i< cards.size(); i++){
                if(i>0){
                    result.append(" ");
                }

                result.append(cards.get(i).getValue());
            }

            return result.toString();
         }

         //main entry point for the complete game
         public static void main(String[] args){
            Scanner scanner = new Scanner(System.in);

            // first ask number of players
            int numberOfPlayers = askForNumberOfPlayers(scanner);

            // second ask and validate pack
            List<Card> pack = askForPack(scanner, numberOfPlayers);

            // 3rd create shared game state
            GameState gameState = new GameState();

            //4 create decks
            List<CardDeck> decks = createDecks(numberOfPlayers);

            // 5 Create players
            List<Player> players = createPlayers(numberOfPlayers, decks, gameState);

            //6 deal all 8n cards
            dealCards(pack, players, decks);

            //Save starting hands before threads change them

            List<String> initialHands = new ArrayList<>();
            for(Player player: players){
                initialHands.add(player.handAsString());
            }

            // 7 start game
            List<Thread> threads = startPlayerThreads(players);
            //wait until winner
            waitForPlayerThreads(threads);

            int winner = gameState.getWinner();
            if (winner > 0){
                System.out.println("player " + winner + " wins");


            } else {
                System.out.println("Game ended, no winner");

            }

            //8 output files
            GameOutput output = new GameOutput();

            try {
                //Player files
                for (int i = 0; i < players.size(); i++){
                    Player player = players.get(i);
                    int playerNumber = player.getPlayerNumber();

                    StringBuilder content = new StringBuilder();
                    
                    content.append("player " + playerNumber + " initial hand: " + initialHands.get(i) + System.lineSeparator());

                    content.append(player.getTurnLog());

                    if (playerNumber == winner){
                        content.append("player " + playerNumber + " wins" + System.lineSeparator());

                    } else{
                        content.append("player " + playerNumber + " has been informed that player " + winner+ " has won" + System.lineSeparator());

                    }

                    content.append("player " + playerNumber + " exits" + System.lineSeparator());

                    content.append("player " + playerNumber + "final hand: " + player.handAsString() + System.lineSeparator() );

                    output.writePlayerOutput(playerNumber, content.toString());
                    }


                    //deck files
                    for (CardDeck deck: decks){
                        String content = "deck " + deck.getDeckNumber() + " contents: " + cardsAsString(deck.getCards()) + System.lineSeparator();

                        output.writeDeckOutput( deck.getDeckNumber(), content);

                    }

            } catch (IOException e) {
                System.err.println("Could not write output files: " + e.getMessage());
            }

            scanner.close();
         }
        }
