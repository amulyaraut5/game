package client.model;

import client.view.Controller;
import client.view.GameViewController;
import client.view.LobbyController;
import client.view.LoginController;
import com.google.gson.Gson;
import javafx.application.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.JSONProtocol.JSONMessage;
import utilities.JSONProtocol.body.HelloClient;
import utilities.JSONProtocol.body.HelloServer;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static utilities.Utilities.PORT;

/**
 * This Singleton Class handles the connection and disconnection to the server.
 * It also communicates between ViewModel and server.
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

    /**
     * readyList is the
     */
    //TODO handle case that id of player that already connected changes
    private ArrayList<String> readyList = new ArrayList<>();
    private GameViewController gameViewController;
    private LoginController loginController;
    private LobbyController lobbyController;
    private Map<Integer, String> otherPlayerMap = new HashMap<>();

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
        readerThread.setName("ReaderThread");
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
     * This method receives a String and a type and sends it to main
     *
     * @param messageBody the specified message
     * @param type        the type of the message
     */
    public void sendToMain(String messageBody, String type) {
        //TODO main.sendChatMessage(messageBody, type);

        //System.out.println("messageBody = " + messageBody + ", type = " + type);
        Platform.runLater(() -> {
            if (type.equals("loginController")) loginController.write(messageBody);
            else if (type.equals("receivedChat")) lobbyController.setTextArea(messageBody);
            else if (type.equals("playerAdded")) lobbyController.setJoinedUsersTextArea(messageBody);
            else if(type.equals("playerStatusIsReady")) lobbyController.setReadyUsersTextArea(messageBody, true);
            else if(type.equals("playerStatusIsNotReady")) lobbyController.setReadyUsersTextArea(messageBody, false);
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
        loginController = (LoginController)controllerList.get(0);
        lobbyController = (LobbyController) controllerList.get(1);
        gameViewController = (GameViewController) controllerList.get(2);
    }

    public void addNewPlayer(int id, String name) {
        otherPlayerMap.put(id, name);
    }

    public String getIDFrom(int id) {
        return otherPlayerMap.get(id);
    }
}