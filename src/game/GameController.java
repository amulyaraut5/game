package game;

import server.ChatServer;
import server.UserThread;

import java.time.LocalDate;
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
    public void readCommand (String message, UserThread user) {
        //TODO: getUserName
        String username = "Default";
        switch (message) {
            case "#create":
                //TODO
                break;
            case "#join":
                //TODO
                break;
            case "#start":
                //TODO
                break;
            case "#help":
                //TODO
                break;
            case "#score":
                //TODO
                break;
            //TODO: cards
            //TODO: end game
        }
    }

    /**
     * Reacts to command "create".
     * This method creates a new GameBoard if not already done and adds the User to the ArrayList.
     * If a GameBoard was already created the User gets a message to join the game.
     */
    public void create (UserThread user, String username, LocalDate lastDate) {
        if (!startedGame) {
            GameBoard gameBoard = new GameBoard();
            startedGame = true;
            gameboard.addUser(user, username, lastDate);
        } else if (!runningGame) {
            server.justUser("Someone has already created a game. Type '#join' if you want to join the game", user);
        } else {
            server.justUser("You're friends have started without you. Just wait and join in the next round.", user);
        }
    }

    /**
     * Reacts to command "join".
     * This method checks if a GameBoard has already been created and/or started.
     * Also a player can only join if <4 players already joined.
     */

    public void join (UserThread user, String username, LocalDate lastDate) {
        // TODO: check if player already joined the game
        if (startedGame && !runningGame && gameboard.getPlayerCount() < 4) {
            gameboard.addUser(user, username, lastDate);
            server.justUser("You've joined the game.", user);
        } else if (!startedGame) {
            server.justUser("Please type '#create' to create a new game.", user);
        } else if (runningGame) {
            server.justUser("You're friends have started without you. Just wait and join in the next round.", user);
        } else if (gameboard.getPlayerCount() >= 4) {
            server.justUser("All player slots have already been taken. Please wait and join the next game.", user);
        }
    }

    /**
     * Reacts to command "start"
     * checks if the player has already joined the game, if a game has been created/started
     * and if there are >=2 and <=4 players.
     * If game can be started the method playGame() is called from the GameBoard.
     */
    public void start (UserThread user) {
        // TODO: check if player already joined the game
        if (startedGame && !runningGame && (gameboard.getPlayerCount() >= 2)) {
            gameboard.playGame();
            runningGame = true;
        } else if (!startedGame) {
            server.justUser("Please type '#create' to create a new game.", user);
        } else if (runningGame) {
            server.justUser("You're friends have started without you. Just wait and join in the next round.", user);
        } else if (gameboard.getPlayerCount() > 2) {
            server.justUser("You need more players to start the game.", user);
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
     * Method to send message from GameBoard to GameController and then to all users
     */
    public void sendMessage (String message) {
        server.communicateAll(message);
    }

    /**
     * Method to send message from GameBoard to GameController and then just to one targeted player.
     */
    public void sendPrivateMessage (String message, Player player) {
        UserThread user = player.getUserThread();
        server.justUser (message, user);
    }
}
