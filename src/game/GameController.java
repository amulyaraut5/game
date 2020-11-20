package game;

import server.ChatServer;
import server.UserThread;

import java.util.ArrayList;

public class GameController {

    private boolean startedGame = false;
    private boolean runningGame = false;
    private GameBoard gameboard;
    private ChatServer server;
    private Player player;

    public GameController () {

    }

    /**
     * reads and distributes all incoming commands regarding the game
     */
    public void readCommand (String message, UserThread sender) {
        //TODO: help
        //TODO: score
    }

    /**
     * Reacts to command "create".
     * This method creates a new GameBoard if not already done and adds the User to the ArrayList.
     * If a GameBoard was already created the User gets a message to join the game.
     */
    public void create (UserThread user, String username) {
        if (!startedGame) {
            GameBoard gameBoard = new GameBoard();
            startedGame = true;
            gameboard.addUser(user, username);
        } else if (startedGame && !runningGame) {

            //TODO: message: Someone has already created a game. Type join if you want to join the game.
        } else if (startedGame && runningGame) {
            //TODO: message: You're friends have started without you. Just wait and join in the next round.
        }
    }

    /**
     * Reacts to command "join".
     * This method checks if a GameBoard has already been created and/or started.
     * Also a player can only join if <4 players already joined.
     */

    public void join (UserThread user, String username) {
        if (startedGame && !runningGame && gameboard.getPlayerCount() < 4) {
            gameboard.addUser(user, username);
            server.justUser("You've joined the game.", user);
            //TODO: message: You've joined the game.
        } else if (!startedGame) {
            server.justUser("Please type 'start' to start", user);
            //TODO: message: please start a game
        } else if (runningGame) {
            //TODO: message: The game is already running
        } else if (gameboard.getPlayerCount() >= 4) {
            //TODO: message: game already full
        }
    }

    /**
     * Reacts to command "start"
     * checks if the player has already joined the game, if a game has been created/started
     * and if there are >=2 and <=4 players.
     * If game can be started the method playGame() is called from the GameBoard.
     */
    public void start () {
        // TODO: check if player already joined the game
        if (startedGame && !runningGame && (gameboard.getPlayerCount() >= 2)) {
            gameboard.playGame();
            runningGame = true;
        } else if (!startedGame) {
            //TODO: message: please start a game
        } else if (runningGame) {
            //TODO: message: The game is already running
        } else if (gameboard.getPlayerCount() > 2) {
            //TODO: message: not enough players to start the game yet
        }
    }

    /**
     * method that resets all game controlling variables when a game is ended.
     */
    public void reset () {
        startedGame = false;
        runningGame = false;
    }

    /**
     * Method to send message from GameBoard to GameController and then just to the playing users
     */
    public void sendMessage (String message, ArrayList<Player> playerList) {

    }

    /**
     * Method to send message from GameBoard to GameController and then just to one targeted player.
     */
    public void sendPrivateMessage (String message, Player player) {
        UserThread user = player.getUserThread();
        server.justUser (message, user);
    }
}
