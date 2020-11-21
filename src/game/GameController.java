package game;

import server.ChatServer;
import server.User;

public class GameController {

    private final String COMMANDS = """
            Use the following commands to control the game:\s
             #create: creates a new game\s
             #join: join the game\s
             #start: starts the game\s
             #score: look at current scores\s
             #choose: if you have to choose a card or another player\s""";
    private boolean startedGame = false;
    private boolean runningGame = false;
    private GameBoard gameboard;
    private ChatServer server;

    public GameController() {

    }

    /**
     * reads and distributes all incoming commands regarding the game
     */
    public synchronized void readCommand(String message, User user) {
        String command = message;
        if (message.contains(" ")) {
            command = message.substring(0, message.indexOf(" "));
        }
        //case "#end":
        switch (command) {
            case "#create" -> create(user);
            case "#join" -> join(user);
            case "#start" -> start(user);
            case "#help" -> user.message(COMMANDS);
            case "#score" -> gameboard.getScorePlayer();
            case "#choose" -> {
                message = message.substring(message.indexOf(" ") + 1);
                gameboard.incomingResponse(message, user);
                //case "#end":
                //TODO default case
            }
        }
    }

    /**
     * Reacts to command "create".
     * This method creates a new GameBoard if not already done and adds the User to the ArrayList.
     * If a GameBoard was already created the User gets a message to join the game.
     */
    public void create(User user) {
        if (!startedGame) {
            gameboard = new GameBoard();
            startedGame = true;
            gameboard.addPlayer(user);
        } else if (!runningGame) {
            user.message("Someone has already created a game. Type '#join' if you want to join the game");
        } else {
            user.message("You're friends have started without you. Just wait and join in the next round.");
        }
    }

    /**
     * Reacts to command "join".
     * This method checks if a GameBoard has already been created and/or started.
     * Also a player can only join if <4 players already joined.
     */

    public void join(User user) {

        if (!gameboard.playerAlreadyJoined(user) && startedGame && !runningGame && gameboard.getPlayerCount() < 4) {
            gameboard.addPlayer(user);
            user.message("You've joined the game.");
        } else if (gameboard.playerAlreadyJoined(user)) {
            user.message("You've already joined the game.");
        } else if (!startedGame) {
            user.message("Please type '#create' to create a new game.");
        } else if (runningGame) {
            user.message("You're friends have started without you. Just wait and join in the next round.");
        } else if (gameboard.getPlayerCount() >= 4) {
            user.message("All player slots have already been taken. Please wait and join the next game.");
        }
    }

    /**
     * Reacts to command "start"
     * checks if the player has already joined the game, if a game has been created/started
     * and if there are >=2 and <=4 players.
     * If game can be started the method playGame() is called from the GameBoard.
     */
    public void start(User user) {
        if (gameboard.playerAlreadyJoined(user) && startedGame && !runningGame && (gameboard.getPlayerCount() >= 2)) {
            gameboard.run();
            runningGame = true;
        } else if (!gameboard.playerAlreadyJoined(user)) {
            user.message("You need to join the game to start it.");
        } else if (!startedGame) {
            user.message("Please type '#create' to create a new game.");
        } else if (runningGame) {
            user.message("You're friends have started without you. Just wait and join in the next round.");
        } else if (gameboard.getPlayerCount() > 2) {
            user.message("You need more players to start the game.");
        }
    }


    /**
     * method that resets all game controlling variables when a game is ended.
     */
    //TODO muss resettet werden
    public void reset() {
        startedGame = false;
        runningGame = false;
    }

    /**
     * Method to send message from GameBoard to GameController and then to all users
     */
    public void sendMessage(String message) {
        server.communicateAll(message);
    }
}
