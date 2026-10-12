public class GameState {

    //Check game over
    private boolean gameOver;

    //Store number of winnder (0 means no one)
    private int winner;

    //Create new game state, obviously no one has won yet
    public GameState() {
        this.gameOver = false;
        this.winner= 0;

    }

    //Check game is finished
    public synchronized boolean isGameOver(){
        return gameOver;
    }

    //Return winning player's number
    public synchronized int getWinner() {
        return winner;
    }

    /*Player calls this when they have won
    True if winner, False if someone else won before
    */
   public synchronized boolean declareWinner(int playerNumber){
        //Player number must be valid
        if(playerNumber <= 0){
            throw new IllegalArgumentException ("Player Number must be positive");
        }
        //If someone won before them
        if(gameOver == true){
            return false;
        }

        //First player to declare they won
        winner = playerNumber;
        gameOver = true;

        return true;
   }
}