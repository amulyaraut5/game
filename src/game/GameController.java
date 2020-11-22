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
                #create: creates a new game\s
                #join:   join the game\s
                #start:  starts the game\s
                #score:  look at current scores\s
                #choose: 'Card/Player': if you have to choose a card or another player\s""";
    public GameBoard gameBoard;
    private boolean createdGame = false;
    private boolean runningGame = false;
    private ChatServer server;

    public GameController(ChatServer server) {
        this.server = server;
        Card.setController(this);
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
            case "#score" -> score(user);
            case "#choose" -> {
                message = message.substring(message.indexOf(" ") + 1);
                if (message.contains("#choose")) {
                    user.message("Unexpected command: " + command + "\n Please enter a valid command. Type #help to see them.");
                } else {
                    gameBoard.getActiveRound().writeResponse(message, user);
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
        if (!createdGame) {
            gameBoard = new GameBoard(this);
            createdGame = true;
            gameBoard.addPlayer(user);
            user.message("You created a new game!\nYour friends can join with '#join'. (2-4 Players)");
            server.communicate(user + " created a game!\nEveryone can join with '#join'. (2-4 Players)", user);
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
        if (!gameBoard.alreadyJoined(user) && createdGame && !runningGame && gameBoard.getPlayerCount() < 4) {
            gameBoard.addPlayer(user);
            user.message("You've joined the game. If you want to start already type '#start'.");
            server.communicate(user + " joined the game! ("+ + gameBoard.getPlayerCount() + "/4)"+"\nPlayers can start the game by typing '#start'.", user);
        } else if (gameBoard.alreadyJoined(user)) {
            user.message("You've already joined the game. If you want to start, type '#start'.");
        } else if (!createdGame) {
            user.message("Please type '#create' to create a new game.");
        } else if (runningGame) {
            user.message("You're friends have started without you. Just wait and join in the next game.");
        } else if (gameBoard.getPlayerCount() >= 4) {
            user.message("All player slots have already been taken. Please wait and join the next game.");
        }
    }

    public void score(User user) {
        if (!createdGame) user.message("No board has been created. Please type '#create' to create a game.");
        else if (!gameBoard.alreadyJoined(user))
            user.message("You haven´t joined the game. Please type '#join' to join");
            //TODO können auch User sich den Score anzeigenlassen, die keine Spieler sind?
        else if (!runningGame) {
            user.message("the game has not been started yet. Type '#start' to start it.");
            gameBoard.getScorePlayer(user);
        } else gameBoard.getScorePlayer(user);
    }

    /**
     * Reacts to command "start".
     * Checks if the player has already joined the game, if a game has been created/started
     * and if there are >=2 and <=4 players.
     * If game can be started the method playGame() is called from the GameBoard.
     */
    public void start(User user) {
        if (!gameBoard.alreadyJoined(user)) {
            user.message("You need to join the game to start it. Type '#join' to do so.");
        } else if (!createdGame) {
            user.message("Please type '#create' to create a new game.");
        } else if (runningGame) {
            user.message("You're friends have started without you. Just wait and join in the next game.");
        } else if (gameBoard.getPlayerCount() < 2) {
            user.message("You need at least one more player to start the game.");
        } else {
            runningGame = true;
            server.communicateAll("The game has started! Please wait until it's your turn.");
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
     * Method to send message from GameBoard to GameController and then to all users
     */
    public void communicateAll(String message) {
        server.communicateAll(message);
    }

    public void communicate(String message, Player player) {
        ArrayList<User> users = server.getUsers();
        for (User user : users) {
            if (User.isSameUser(user, player)) server.communicate(message, user);
        }
    }
}
