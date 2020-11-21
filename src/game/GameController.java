package game;

import server.ChatServer;
import server.User;

/**
 * The GameController mainly handles the communication between the chat and the game.
 * It reads all the game commands and distributes it to the assigned classes in the game.
 * In particular it handles the creation of the GameBoard, joining and starting a game.
 */

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
     * Reads and distributes all incoming commands regarding the game.
     * In case the command consists of additional information in choose the command gets cut.
     * It checks if the required additional information is there and then sends only this
     * additional information to the gameboard to be evaluated.
     */
    public synchronized void readCommand(String message, User user) {
        String command = message;
        if (message.contains(" ")) {
            command = message.substring(0, message.indexOf(" "));
        }
        switch (command) {
            case "#create" -> create(user);
            case "#join" -> join(user);
            case "#start" -> start(user);
            case "#help" -> user.message(COMMANDS);
            case "#score" -> gameboard.getScorePlayer();
            case "#choose" -> {
                message = message.substring(message.indexOf(" ") + 1);
                if (message.contains("#choose")) {
                    user.message("Unexpected command: " + command + "\n Please enter a valid command. Type #help to see them.");
                } else {
                    gameboard.incomingResponse(message, user);
                }
            }
            default -> user.message("Unexpected command: " + command + "\n Please enter a valid command. Type #help to see them.");
        } //case "#end":
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
            user.message("Someone has already created a game. Type '#join' if you want to join the game.");
        } else {
            user.message("You're friends have started without you. Just wait and join in the next game.");
        }
    }

    /**
     * Reacts to command "join".
     * This method checks if a GameBoard has already been created and/or started.
     * Also a player can only join if <4 players already joined.
     */
    public void join(User user) {

        if (!gameboard.alreadyJoined(user) && startedGame && !runningGame && gameboard.getPlayerCount() < 4) {
            gameboard.addPlayer(user);
            user.message("You've joined the game. If you want to start already type '#start'.");
        } else if (gameboard.alreadyJoined(user)) {
            user.message("You've already joined the game. If you want to start already type '#start'.");
        } else if (!startedGame) {
            user.message("Please type '#create' to create a new game.");
        } else if (runningGame) {
            user.message("You're friends have started without you. Just wait and join in the next game.");
        } else if (gameboard.getPlayerCount() >= 4) {
            user.message("All player slots have already been taken. Please wait and join the next game.");
        }
    }

    /**
     * Reacts to command "start".
     * Checks if the player has already joined the game, if a game has been created/started
     * and if there are >=2 and <=4 players.
     * If game can be started the method playGame() is called from the GameBoard.
     */
    public void start(User user) {
        if (gameboard.alreadyJoined(user) && startedGame && !runningGame && (gameboard.getPlayerCount() >= 2)) {
            gameboard.run();
            runningGame = true;
        } else if (!gameboard.alreadyJoined(user)) {
            user.message("You need to join the game to start it. Type '#join' to do so.");
        } else if (!startedGame) {
            user.message("Please type '#create' to create a new game.");
        } else if (runningGame) {
            user.message("You're friends have started without you. Just wait and join in the next game.");
        } else if (gameboard.getPlayerCount() > 2) {
            user.message("You need at least one more player to start the game.");
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
