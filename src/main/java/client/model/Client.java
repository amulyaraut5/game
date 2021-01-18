package client.model;

import client.ViewManager;
import client.view.*;
import game.Player;
import javafx.application.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.JSONProtocol.JSONBody;
import utilities.JSONProtocol.JSONMessage;
import utilities.JSONProtocol.Multiplex;
import utilities.JSONProtocol.body.Error;
import utilities.JSONProtocol.body.*;
import utilities.Utilities;
import utilities.enums.MessageType;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static utilities.Utilities.PORT;

/**
 * This Singleton Class handles the connection and disconnection to the server.
 * It also communicates between ViewModel and server.
 *
 * @author sarah,
 */
public class Client {
    private static final Logger logger = LogManager.getLogger();

    private static Client instance;
    private final ViewManager viewManager = ViewManager.getInstance();

    private GameViewController gameViewController;
    private LoginController loginController;
    private LobbyController lobbyController;
    private ChatController chatController;
    /**
     * every time the ReaderThread gets the MessageType PlayerAdded the new player will be stored in that list
     */
    private ArrayList<Player> players = new ArrayList<>();
    /**
     * Stream socket which get connected to the specified port number on the named host of the server.
     */
    private Socket socket;
    /**
     * The readerThread reads the input of the user from given socket.
     */
    private ReaderThread readerThread;
    /**
     * the printWriter which writes messages onto the socket connected to the server.
     */
    private PrintWriter writer;
    private boolean exit = false;
    private int playerID;

    public static Client getInstance() {
        if (instance == null) instance = new Client();
        return instance;
    }

    /**
     * This method establishes the connection between the server and the client using the assigned hostname and port.
     * If this was successful it creates a ReaderThread and a WriterThread which handle the communication onwards.
     */
    public boolean establishConnection() {
        try {
            String hostname = "localhost";
            socket = new Socket(hostname, PORT);
            writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);

            readerThread = new ReaderThread(socket, this);
            readerThread.start();

            logger.info("Connection to server successful.");
            return true;
        } catch (IOException e) {
            logger.warn("No connection to server: " + e.getMessage());
        }
        return false;
    }

    /**
     * This method ends the client program. The reader and writer threads get interrupted and the socket is closed.
     */
    public void disconnect() {
        readerThread.interrupt();
        try {
            socket.close();
            logger.info("The connection with the server is closed.");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * This method should be called if a critical exception occurs in the client. (e.g. socket closed)
     * The reader and writer threads get interrupted and the socket is closed.
     *
     * @param ex The exception which occurred
     */
    public void disconnect(Exception ex) {
        readerThread.interrupt();
        logger.warn("The server is no longer reachable: " + ex.getMessage());
        try {
            socket.close();
            logger.info("The connection with the server is closed.");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public void setController(ArrayList<Controller> controllerList) {
        loginController = (LoginController) controllerList.get(0);
        lobbyController = (LobbyController) controllerList.get(1);
        gameViewController = (GameViewController) controllerList.get(2);
    }

    public void setChatController(ChatController chatController) {
        this.chatController = chatController;
    }

    /**
     * Based on the messageType the various protocol are differentiated and Object class type
     * is casted down to respective class and then it interacts with the different controllers.
     *
     * @param message
     */
    public void handleMessage(JSONMessage message) {
        MessageType type = message.getType();

        Platform.runLater(() -> {
            switch (type) {
                case HelloClient -> {
                    connect();
                }
                case Welcome -> {
                    Welcome wc = (Welcome) message.getBody();
                    playerID = wc.getPlayerID();
                    viewManager.nextScene();
                }
                case PlayerAdded -> {
                    PlayerAdded playerAdded = (PlayerAdded) message.getBody();
                    addNewPlayer(playerAdded);
                }
                case Error -> {
                    Error error = (Error) message.getBody(); //TODO
                }
                case PlayerStatus -> {
                    PlayerStatus playerStatus = (PlayerStatus) message.getBody();
                    lobbyController.displayStatus(playerStatus);
                }
                case ReceivedChat -> {
                    ReceivedChat receivedChat = (ReceivedChat) message.getBody();
                    chatController.receivedChat(receivedChat);
                }
                case GameStarted -> {
                    GameStarted gameStarted = (GameStarted) message.getBody();
                    gameViewController.buildMap(gameStarted);
                    viewManager.nextScene();
                }
                case StartingPointTaken -> {
                    StartingPointTaken msg = (StartingPointTaken) message.getBody();
                    gameViewController.placeRobotInMap(msg.getPlayerID(), msg.getPosition());
                }
                case YourCards -> {
                    YourCards yourCards = (YourCards) message.getBody();
                    gameViewController.programCards(yourCards);
                }
                case ConnectionUpdate -> {
                    ConnectionUpdate connectionUpdate = (ConnectionUpdate) message.getBody(); //TODO
                }
                case Reboot -> {
                    Reboot reboot = (Reboot) message.getBody();
                    logger.info("Player " + reboot.getPlayerID() + "fell into pit");
                    // TODO set the Robot image back to reboot token
                }
                case Energy -> {
                    Energy energy = (Energy) message.getBody();
                    logger.info("Player " + energy.getPlayerID() + "received 1 energy cube");
                }
                case CheckPointReached -> {
                    CheckpointReached checkpointsReached = (CheckpointReached) message.getBody();
                    logger.info("Player " + checkpointsReached.getPlayerID() + "has reached checkpoint: " + checkpointsReached.getNumber());
                    //TODO Display the message in chat for players/users
                }
                case GameWon -> {
                    GameWon gameWon = (GameWon) message.getBody();
                    logger.info("Player " + gameWon.getPlayerID() + "has won the game");
                    //TODO Display the message in chat for players/users
                }
                case Movement -> {
                    Movement movement = (Movement) message.getBody();
                    gameViewController.handleMovement(movement.getPlayerID(), movement.getTo());
                }
                case PlayerTurning -> {
                    PlayerTurning playerTurning = (PlayerTurning) message.getBody();
                    gameViewController.handlePlayerTurning(playerTurning.getPlayerID(), playerTurning.getDirection());
                }
                default -> logger.error("The MessageType " + type + " is invalid or not yet implemented!");
            }
        });
    }

    /**
     * This method creates a HelloServer protocol message and sends it to server
     * it gets called when client gets a HelloClient message
     */
    public void connect() {
        JSONBody jsonBody = new HelloServer(Utilities.PROTOCOL, "Astreine Akazien", false);
        sendMessage(jsonBody);
    }

    public void addNewPlayer(PlayerAdded playerAdded) {
        Player player = new Player(playerAdded);
        players.add(player);

        if (playerID == player.getID()) {
            gameViewController.getPlayerMapController().loadPlayerMap(player);
            viewManager.nextScene();
        }
        lobbyController.setJoinedUsers(player, false);
        chatController.addUser(player);
    }

    /**
     * This message changes a JSONMessage so that it's possible
     * to send it as a String over the socket to the server
     *
     * @param jsonBody
     */
    public void sendMessage(JSONBody jsonBody) {
        String json = Multiplex.serialize(JSONMessage.build(jsonBody));
        logger.debug("Protocol sent: " + json);
        writer.println(json);
        writer.flush();
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }
}