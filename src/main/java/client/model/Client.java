package client.model;

import client.view.GameViewController;
import client.view.LoginController;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import json.Commands;
import json.Message;
import server.User;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import static client.view.GameViewController.*;

/**
 * This class creates the socket for the client.
 * It handles the connection and disconnection to the server.
 * In this process it creates or interrupts the WriterThread and ReaderThread.
 *
 * @author janau
 */
public class Client {
    /**
     * hostname of the server is saved here for the socket creation.
     */
    private String hostname;
    /**
     * port of the server on the named host is saved here for the socket creation.
     */
    private int port;
    private final GameViewController gController;
    /**
     * Stream socket which get connected to the specified port number on the named host of the server.
     */
    private Socket socket;
    /**
     * The readerThread reads the input of the user from given socket.
     */
    private ReaderThread readerThread;
    /**
     * The writerThread writes the console input of the user from given socket.
     */
    private Writer writer;

    private GameViewController gameViewController;

    private User user;

    /**
     * constructor of ChatClient to initialize the attributes hostname and port.
     *
     * @param hostname Hostname of the server.
     * @param port     Port of the server on the named host.
     */
    /*public Client(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

     */

    /**
     * The Constructor sets viewModel and calls establishConnection()
     */
    public Client(GameViewController gController) {
        this.gController = gController;
        establishConnection();
    }


    /**
     * Main method for the client program. A new ChatClient is created
     * and an attempt is made to establish the connection.
     *
     * @param args unused arguments
     */
    public static void main(String[] args) {

        String hostname = "localhost";
        int port = 5444;

        //Client client = new Client(hostname, port);
        //client.establishConnection();
    }

    /**
     * This method establishes the connection between the server and the client using the assigned hostname and port.
     * If this was successful it creates a ReaderThread and a WriterThread which handle the communication onwards.
     */
    private void establishConnection() {
        try {
            socket = new Socket(hostname, port);

            readerThread = new ReaderThread(socket, this);
            readerThread.start();

            System.out.println("Connection to server successful.");

        } catch (UnknownHostException e) {
            System.out.println("Connection failed - IP-address of host could not be determined: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Connection failed - General I/O exception: " + e.getMessage());
        }
    }

    /**
     * methods ends the client program. The reader and writer threads get interrupted and the socket is closed.
     */
    public void disconnect() {
        readerThread.interrupt();
        try {
            socket.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
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
        System.out.println("The server is no longer reachable: " + ex.getMessage());
        try {
            socket.close();
            System.out.println("The connection with the server is closed.");
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        System.out.println("Type \"bye\" to exit.");
    }

    public void sendUserInput(String message) {
        writer.sendUserInput(message);

    }

    /**
     * Method that gets called by view.PrimaryViewModel.sendMessage().
     * It contains the Logic for interpreting the messages sent by the user and also sends them off to the server.
     * messages are sent to the server.
     */
    public void processViewMessage(String message) throws Exception {
        if (message != null) {
            writer.send(Commands.Message(getMessage(message)));
        }
    }

    /**
     * The Text message will be posted in the view
     */
    private void processTextMessage(JsonObject jsonObject) {
        Gson gson = new Gson();
        if (jsonObject.has("Message")) {
            String strMessage = jsonObject.get("Message").getAsString();
            Message textMessage = gson.fromJson(strMessage, Message.class);
            gameViewController.displayInChat(textMessage.getMessage());
        }
    }

    /**
     * processes the server messages and depending on it, it calls the specified methods
     */
    protected void ServerMessage(String text) throws Exception {
        JsonObject jsonObject = JsonParser.parseString(text).getAsJsonObject();

        if (jsonObject.has("TextMessage")) {
            jsonObject = jsonObject.getAsJsonObject("TextMessage");
            processTextMessage(jsonObject);
        } else {
            //nothing
        }
    }

    /**
     * creating an object Message and set the message
     * it should be send to all users
     */
    private Message getMessage(String msg) {
        Message textMessage = new Message();
        textMessage.setSender(user.getName());

        textMessage.setMessage(msg);
        return textMessage;
    }

    public void setLoginController(LoginController controller){
    }

    public void setGameViewController(GameViewController controller){
        this.gameViewController = controller;
    }
}