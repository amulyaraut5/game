package game;

import server.UserThread;

/**
 * For each user who plays the game, a Player object is created.
 * The Player class stores data of the user and player specific data.
 */
public class Player {
    private final UserThread user;
    private final String userName;
    private int numOfTokens;

    /**
     * Creates new player with given parameters.
     *
     * @param user     UserThread connected with this player
     * @param userName Name of the player
     */
    public Player(UserThread user, String userName) {
        this.user = user;
        this.userName = userName;
        numOfTokens = 0;
    }

    /**
     * returns the name of the user connected with this player.
     *
     * @return Name of the Player.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Getter for the userThread.
     *
     * @return UserThread of this player.
     */
    public UserThread getUserThread() {
        return user;
    }

    /**
     * Resets the number of tokens for that player to '0'.
     */
    public void resetNumOfTokens() {
        numOfTokens = 0;
    }

    /**
     * Increases the number of tokens for that player.
     *
     * @return Number of tokens after the increase.
     */
    public int increaseNumOfTokens() {
        return ++numOfTokens;
    }

    /**
     * Getter for the number of tokens for that player.
     *
     * @return Number of tokens.
     */
    public int getNumOfTokens() {
        return numOfTokens;
    }
}
