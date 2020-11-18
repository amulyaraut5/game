package game;

import server.UserThread;

import java.util.ArrayList;

public class GameBoard {
    public ArrayList<Player> playerList;
    private boolean started = false;

    public GameBoard(){

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



}
