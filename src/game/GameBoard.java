package game;

import server.UserThread;

import java.util.ArrayList;

public class GameBoard {
    public ArrayList<Player> playerList;
    private boolean started = false;
    private GameController gameController = new GameController();
    public Player lastRecentlyWon; //to check who´s next in following rounds

    public GameBoard(){

    }
    /**
     * If this method is called, the game is on and no more users can join
     */
    public void startGame(){
        started = true;

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


}
