package client.model;

import client.view.GameViewController;
import client.view.LoginController;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class creates the socket for the client.
 * It handles the connection and disconnection to the server.
 * In this process it creates or interrupts the WriterThread and ReaderThread.
 *
 * @author janau
 */
public class Client {
    /**
     * Logger to log information/warning
     */
    Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    /**
     * hostname of the server is saved here for the socket creation.
     */
    private final String hostname;
    /**
     * port of the server on the named host is saved here for the socket creation.
     */
    private final int port;
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
     * The writerThread writes the console input of the user from given socket.
     */

    private GameViewController gameViewController;

    private LoginController loginController;

    /**
     * constructor of ChatClient to initialize the attributes hostname and port.
     *
     * @param loginController
     * @param hostname Hostname of the server.
     * @param port     Port of the server on the named host.
     */
    public Client(LoginController loginController, String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
        this.loginController = loginController;

        logger.setLevel(Level.ALL);

        establishConnection();
    }


    /**
     * This method establishes the connection between the server and the client using the assigned hostname and port.
     * If this was successful it creates a ReaderThread and a WriterThread which handle the communication onwards.
     */
    private void establishConnection() {
        while (true) {
            try {
                socket = new Socket(hostname, port);
                writer = new PrintWriter(socket.getOutputStream(), true);
                break;
            } catch (IOException ex) {
                logger.warning("No connection.");
            }
        }


        readerThread = new ReaderThread(socket, this);
        readerThread.start();

        logger.info("Connection to server successful.");

    }

    /**
     * methods ends the client program. The reader and writer threads get interrupted and the socket is closed.
     */
    public void disconnect() {
        readerThread.interrupt();
        try {
            socket.close();
        } catch (IOException e) {
            logger.severe(e.getMessage());
        }
    }

    /**
     * Method should be called if a critical exception occurs in the client. (e.g. socket closed)
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
            logger.severe(e.getMessage());
        }
        System.out.println("Type \"bye\" to exit.");
    }


    public void setLoginController(LoginController controller) {
        this.loginController = controller;
    }

    public void setGameViewController(GameViewController controller) {
        this.gameViewController = controller;
    }

    public void sentUserInput(JsonObject jsonObject) {
        writer.println(jsonObject.toString());
    }

    public void callServerResponse(boolean taken) throws IOException {
        loginController.serverResponse(taken);
    }

    public void chatMessage(String messageBody) {
        gameViewController.setTextArea(messageBody);
    }
}