package game;

import cards.Cards;
import server.UserThread;

import java.util.ArrayList;
import java.util.Stack;

public class GameBoard {
    public ArrayList<Player> playerList;
    private boolean started = false;
    private GameController gameController = new GameController();
    public Stack<Cards> stackCards; //ArrayList - so we can use Collections.shuffle?
    public Player gameWinner;

    public GameBoard(){

    }
    /**
     * If this method is called, the game is on and no more users can join
     */
    public void startGame(){

        playGame();

    }

    /**
     * method to play rounds until someone has won the whole game
     */
    public void playGame(){
        Player firstplayer = compareDates();
        while (!gameWon()){
            Round round = new Round(firstplayer);
            round.play();
            firstplayer = round.getWinner();
        }
        gameWinner = firstplayer;
    }

    /**
     * it finds out who was last recently on a date
     * @return this player
     */
    public Player compareDates(){
        return playerList.get(0); //just puffer
    }
    /**
     * It creates a Player and adds it to the list of joined player
     * @param user Thread of the user
     * @param username
     */
    public void addUser(UserThread user, String username){
        Player player = new Player(user, username);
        playerList.add(player);
    }

    /**
     * It returns how many player already joined
     * @return number of player
     */
    public int getPlayerCount(){
        return playerList.size();
    }



    /**
     * checks if round is over who´s the winner depending on value cards/discarded cards
     * @return winner
     */
    public Player getWinner(){
        return gameWinner;
    }
    /**
     * checks whether some Player already has won the whole game
     * @return winGame
     */
    private boolean gameWon (){

        boolean win = false;
        //you win the game of you have enough token
        for (Player player : playerList){
            //2 player -> 7 token
            if((playerList.size()==2) && (player.getTokenCount()>=7)){
                win = true;
            } //3 player -> 5 token
            else if((playerList.size()==3) && (player.getTokenCount()>=5 )){
                win = true;
            }  //4 player -> 4 token
            else if ((playerList.size()==4) && (player.getTokenCount()>=4)){
                win = true;
            }
        }
        return win;
    }


}
