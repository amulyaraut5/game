package client.model;

import client.view.Controller;
import client.view.GameViewController;
import client.view.LobbyController;
import client.view.LoginController;
import com.google.gson.Gson;
import game.gameObjects.tiles.Attribute;
import javafx.application.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.JSONProtocol.JSONMessage;
import utilities.JSONProtocol.body.Error;
import utilities.JSONProtocol.body.*;
import utilities.Utilities;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import static utilities.Utilities.PORT;

/**
 * This Singleton Class handles the connection and disconnection to the server.
 * It also communicates between ViewModel and server.
 *
 * @author sarah,
 */
public class Client {
    /**
     * Logger to log information/warning
     */
    private static final Logger logger = LogManager.getLogger();
    /**
     * Singleton instance of Client
     */
    private static Client instance;
    /**
     * hostname of the server is saved here for the socket creation.
     */
    private final String hostname = "localhost";
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

    private int playerId;

    /**
     * readyList is the
     */
    //TODO handle case that id of player that already connected changes
    private ArrayList<String> readyList = new ArrayList<>();
    private GameViewController gameViewController;
    private LoginController loginController;
    private LobbyController lobbyController;


    /**
     * every time the ReaderThread gets the MessageType PlayerAdded the new player will be stored in that list
     */
    public ArrayList<PlayerAdded> playerList= new ArrayList<>();

    /**
     * constructor of ChatClient to initialize the attributes hostname and port.
     */
    private Client() {
    }

    public static Client getInstance() {
        if (instance == null) instance = new Client();
        return instance;
    }

    /**
     * This method creates a HelloServer protocol message and sends it to server
     * it gets called when client gets a HelloClient message
     *
     * @param helloClient //TODO more than protocol possible?
     */
    public void connect(HelloClient helloClient) {
        JSONMessage msg = new JSONMessage(new HelloServer(0.1, "Astreine Akazien", false));
        sendMessage(msg);
    }

    /**
     * This method establishes the connection between the server and the client using the assigned hostname and port.
     * If this was successful it creates a ReaderThread and a WriterThread which handle the communication onwards.
     */
    public void establishConnection() {
        while (true) {
            try {
                socket = new Socket(hostname, PORT);
                writer = new PrintWriter(socket.getOutputStream(), true);
                break;
            } catch (IOException ex) {
                logger.warn("No connection.");
            }
        }

        readerThread = new ReaderThread(socket, this);
        readerThread.start();

        logger.info("Connection to server successful.");

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
        logger.info("The server is no longer reachable: " + ex.getMessage());
        try {
            socket.close();
            logger.info("The connection with the server is closed.");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        logger.info("Type \"bye\" to exit.");
    }

    /**
     * This message changes a JSONMessage so that it's possible
     * to send it as a String over the socket to the server
     *
     * @param message
     */
    public void sendMessage(JSONMessage message) {
        Gson gson = new Gson();
        String json = gson.toJson(message);
        logger.debug("Protocol sent: " + json);
        writer.println(json);
    }

    /**
     * Based on the messageType the various protocol are differentiated and Object class type
     * is downcasted to respective class.
     *
     * @param message
     * @throws ClassNotFoundException
     */
    void handleMessage(JSONMessage message) throws ClassNotFoundException {

        Utilities.MessageType type = message.getType();
        Platform.runLater(() -> {
            switch (type) {
                case HelloClient:
                    HelloClient hc = (HelloClient) message.getBody();
                    connect(hc);
                    break;
                case Welcome:
                    Welcome wc = (Welcome) message.getBody();
                    playerId = wc.getPlayerID();
                    break;
                case PlayerAdded:
                    PlayerAdded playerAdded = (PlayerAdded) message.getBody();
                    //logger.info("Player Added: " + playerAdded.getId());
                    addNewPlayer(playerAdded);
                    logger.info(playerList.size() +" = playerList Size");
                    lobbyController.setJoinedUsers(playerAdded);
                    break;
                case Error:
                    Error error = (Error) message.getBody();
                    logger.warn("Error Message: " + error.getError());
                    break;
                case PlayerStatus:
                    PlayerStatus playerStatus = (PlayerStatus) message.getBody();
                    lobbyController.setReadyUsersTextArea(playerStatus);
                    break;
                case ReceivedChat:
                    ReceivedChat receivedChat = (ReceivedChat) message.getBody();
                    String receivedMessage;
                    if(receivedChat.isPrivat())
                        receivedMessage = "[" +receivedChat.getFrom() + "] @You: " + receivedChat.getMessage();
                    else receivedMessage = "[" +receivedChat.getFrom() + "] " + receivedChat.getMessage();
                    lobbyController.setTextArea(receivedMessage);
                    break;
                case GameStarted:
                    GameStarted gameStarted = (GameStarted) message.getBody();
                    logger.info("The game has started.");
                    break;
                case ConnectionUpdate:
                    ConnectionUpdate connectionUpdate = (ConnectionUpdate) message.getBody();
                    logger.info("Player " + connectionUpdate.getId() + " has lost connection to server.");
                    break;
                case Reboot:
                    Reboot reboot = (Reboot) message.getBody();
                    logger.info("Player " + reboot.getPlayerID() + "fell into pit");
                    // TODO set the Robot image back to reboot token
                    break;
                case Energy:
                    Energy energy = (Energy) message.getBody();
                    logger.info("Player "+ energy.getPlayerID() + "received 1 energy cube");
                    break;
                case CheckPointReached:
                    CheckpointReached checkpointsReached = (CheckpointReached) message.getBody();
                    logger.info("Player " + checkpointsReached.getPlayerID() + "has reached checkpoint: " + checkpointsReached.getNumber());
                    //TODO Display the message in chat for players/users
                    break;
                case GameWon:
                    GameWon gameWon = (GameWon) message.getBody();
                    logger.info("Player " + gameWon.getPlayerID() + "has won the game");
                    //TODO Display the message in chat for players/users
                    break;
                default:
                    logger.warn("Something went wrong");
            }
        });
    }


    public void addToReadyList(String id) {
        readyList.add(id);
    }

    public void deleteFromReadyList(String id) {
        readyList.remove(id);
    }

    public void setGameViewController(GameViewController gameViewController) {
        //this.gameViewController = gameViewController;
    }

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }

    public void setController(ArrayList<Controller> controllerList) {
        loginController = (LoginController) controllerList.get(0);
        lobbyController = (LobbyController) controllerList.get(1);
        gameViewController = (GameViewController) controllerList.get(2);
    }



    public void addNewPlayer(PlayerAdded playerAdded) {
        playerList.add(playerAdded);
    }


    public boolean playerListContains(int robotID) {
        for(PlayerAdded playerAdded : playerList){
            if(playerAdded.getFigure()==robotID){
                return true;
            }
        }
        return false;
    }

    public int getIDFrom(String destinationUser) {
        for (PlayerAdded playerAdded: playerList){
            if(destinationUser.equals(playerAdded.getName())){
                return playerAdded.getId();
            }
        }
        return -2;
    }
}