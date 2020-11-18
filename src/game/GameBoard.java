package game;

import cards.Cards;
import server.UserThread;

import java.util.ArrayList;
import java.util.Stack;

public class GameBoard {
    public ArrayList<Player> playerList;
    private boolean started = false;
    private GameController gameController = new GameController();
    public Player lastRecentlyWon; //to check who´s next in following rounds
    public ArrayList <Player> activePlayerList;
    public Stack<Cards> stackCards;

    public GameBoard(){

    }
    /**
     * If this method is called, the game is on and no more users can join
     */
    public void startGame(){
        started = true;
        //Message to player that game has started
        //explore who begins
        Player firstplayer = compareDates();
        //message to firstplayer: what are his cards, what card does  he want to discard
        //message to other players: turnplayers turn

        //Game

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
     * It communicates whether the game has already started
     * @return started
     */
    public boolean isStarted() {
        return started;
    }
    /**
     * Method ends the whole game after couple of rounds
     */
    public void endGame(){

    }

    /**
     * checks whether some Player already has won the round
     * @return endRound
     */

    boolean endRound(){
        boolean endRound = false;
        //1. possibility: stack is empty

        //2.possibility: only one person left

        return endRound;
    }
    /**
     * checks whether some Player already has won the whole game
     * @return winGame
     */
    private boolean gameWon (){
        boolean winGame = false;
        //check whether one player has enough token
        //2 player -> 7 token
        //3 player -> 5 token
        //4 player -> 4 token
      return winGame;
    }


}
