package client;

import javafx.application.Platform;
import login.LoginController;
import view.Controller;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * This class creates the socket for the client.
 * It handles the connection and disconnection to the server.
 * In this process it creates or interrupts the WriterThread and ReaderThread.
 *
 * @author janau
 */

public class ChatClient {
    private final int port;
    private final String hostname;
    private Socket socket;

    private ReaderThread readerThread;
    private Writer writer;
    private LoginController loginController;
    private Controller controller;


    public ChatClient(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;

        establishConnection();
    }

    /**
     * This method establishes the connection between the server and the client using the assigned hostname and port.
     * If this was successful it creates a ReaderThread and a WriterThread which handle the communication onwards.
     */

    public void establishConnection() {
        try {
            socket = new Socket(hostname, port);

            writer = new Writer(socket, this);
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
     *
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
     * @param ex
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

    public void handleServerMessage(String message) {
        String command = message;
        if (message.contains(" ")) {
            command = message.substring(0, message.indexOf(" "));
        }
        boolean join = false;

        String finalMessage = message.substring(message.indexOf(" ") + 1);
        String finalCommand = command;
        if (message.contains("joined the room")){
            finalCommand = "someone joined";
        }
        //The methods are not called directly from the controller,
        // the method calls take place in the JavaFX application thread.
        String finalCommand1 = finalCommand;
        Platform.runLater(
                () -> {
                    switch (finalCommand1) {
                        case "#login" -> loginController.ServerResponse(finalMessage);
                        case "someone joined" -> controller.ServerResponse(message);
                    }
                }
        );
    }

    public void sentUserInput(String input) {
        writer.sentUserInput(input);
    }

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }
}
