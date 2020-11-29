package game;

import card.Card;
import server.ChatServer;
import server.User;

import java.util.ArrayList;

/**
 * The GameController mainly handles the communication between the chat and the game.
 * It reads all the game commands and distributes it to the assigned classes in the game.
 * In particular it handles the creation of the GameBoard, joining and starting a game.
 *
 * @author janau
 */

public class GameController {

    private final String COMMANDS = """
            Use the following commands to control the game:\s
                #play:   join the game\s
                #start:  starts the game\s
                #score:  look at current scores\s
                #choose <card/player>: if you have to choose a card or another player\s""";
    private final ChatServer server;
    private GameBoard gameBoard;
    private boolean createdGame = false;
    private boolean runningGame = false;

    /**
     * Constructor for the GameController.
     *
     * @param server server connected to the GameBoard which is responsible for the Game commands.
     */
    public GameController(ChatServer server) {
        this.server = server;
        Card.setController(this);
    }

    /**
     * Reads and distributes all incoming commands regarding the game.
     * In case the command consists of additional information in choose the command gets cut.
     * It checks if the required additional information is there and then sends only this
     * additional information to the GameBoard to be evaluated.
     *
     * @param message command that is received from the Server
     * @param user    user that sent the command
     */
    public synchronized void readCommand(String message, User user) {
        String command = message;
        if (message.contains(" ")) {
            command = message.substring(0, message.indexOf(" "));
        }
        switch (command) {
            case "#play" -> play(user);
            case "#start" -> start(user);
            case "#help" -> user.message(COMMANDS);
            case "#choose" -> {
                message = message.substring(message.indexOf(" ") + 1);
                if (message.contains("#choose")) {
                    user.message("Unexpected command: " + command + "\nPlease enter a valid command. Type #help to see them.");
                } else {
                    gameBoard.getActiveRound().writeResponse(message, user);
                }
            }
            case "#playerList" -> {
                if (createdGame) {
                    String playerList = "";
                    for (Player pl : gameBoard.getPlayerList()) {
                        playerList += pl.getName();
                        playerList += " ";
                    }
                    user.message("#playerList: " + playerList);
                }
            }

            default -> user.message("Unexpected command: " + command + "\nPlease enter a valid command. Type #help to see them.");
        } //case "#end":
    }


    /**
     * Reacts to command "play".
     * This method checks if a GameBoard has already been created and/or started.
     * If the player is the first to join the GameBoard gets created. If he is the 4th player the game starts
     * automatically.
     *
     * @param user user who joined the game
     */
    public void play(User user) {
        if (!createdGame) {
            gameBoard = new GameBoard(this);
            createdGame = true;
            gameBoard.addPlayer(user);
            user.message("You created a new game!\nYour friends can join with '#play'. (2-4 Players)");
            server.communicate(user + " created a game!\nEveryone can join with '#play'. (2-4 Players)", user);
        } else if (!gameBoard.alreadyJoined(user) && !runningGame && gameBoard.getPlayerCount() < 4) {
            gameBoard.addPlayer(user);
            if (gameBoard.getPlayerCount() < 4) {
                user.message("You've joined the game. If you want to start already type '#start'.");
                server.communicate(user + " joined the game! (" + gameBoard.getPlayerCount() + "/4)" + "\nPlayers can start the game by typing '#start'.", user);
            } else {
                user.message("You've joined the game.");
                server.communicate(user + " joined the game! (" + gameBoard.getPlayerCount() + "/4).", user);
                start(user);
            }
        } else if (gameBoard.alreadyJoined(user)) {
            user.message("You've already joined the game. If you want to start, type '#start'.");
        } else if (runningGame) {
            user.message("You're friends have started without you. Just wait and join in the next game.");
        } else if (gameBoard.getPlayerCount() >= 4) {
            user.message("All player slots have already been taken. Please wait and join the next game.");
        }
    }

    /**
     * Reacts to command "start".
     * Checks if the player has already joined the game, if a game has been created/started
     * and if there are >=2 and <=4 players.
     * If game can be started the method playGame() is called from the GameBoard.
     *
     * @param user user who started the game
     */
    public void start(User user) {
        if (!gameBoard.alreadyJoined(user)) {
            user.message("You need to join the game to start it. Type '#play' to do so.");
        } else if (!createdGame) {
            user.message("Please type '#play' to create a new game.");
        } else if (runningGame) {
            user.message("You're friends have started without you. Just wait and join in the next game.");
        } else if (gameBoard.getPlayerCount() < 2) {
            user.message("You need at least one more player to start the game.");
        } else {
            runningGame = true;
            communicateAll("The game has started! Please wait until it's your turn.");
            gameBoard.start();
        }
    }


    /**
     * method that resets all game controlling variables when a game is ended.
     */
    public void reset() {
        createdGame = false;
        runningGame = false;
    }

    /**
     * Method to send in Game messages to the Users/Players. This method sends a message to every User.
     *
     * @param message String that should be send
     */
    public void communicateAll(String message) {
        message = "\033[35m" + message + "\033[0m";
        server.communicateAll(message);
    }

    /**
     * Method to send in Game messages to the Users/Players. This method sends a message to everyone except one specified
     * Player.
     *
     * @param message String that should be send
     * @param player  Player who doesn't get send message
     */
    public void communicate(String message, Player player) {
        ArrayList<User> users = server.getUsers();
        message = "\033[35m" + message + "\033[0m";
        for (User user : users) {
            if (User.isSameUser(user, player)) server.communicate(message, user);
        }
    }
}
