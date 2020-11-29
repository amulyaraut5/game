package client;

import javafx.application.Platform;
import login.LoginController;
import popupGame.ControllerPopUp;
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
    private boolean connection;
    private ReaderThread readerThread;
    private Writer writer;
    private LoginController loginController;
    private Controller controller;
    private ControllerPopUp controllerPopUp;
    public ChatClient(LoginController loginController, String hostname, int port) {
        this.loginController = loginController;
        this.hostname = hostname;
        this.port = port;

        establishConnection();
    }

    public boolean isConnection() {
        return connection;
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
            connection = true;

        } catch (UnknownHostException e) {
            System.out.println("Connection failed - IP-address of host could not be determined: " + e.getMessage());
            connection = false;
        } catch (IOException e) {
            System.out.println("Connection failed - General I/O exception: " + e.getMessage());
            connection = false;
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
        if (message.contains("joined the room")) {
            command = "someone joined";
        } else if (message.substring(0, 1).equals("[")) {
            command = "#chat";
        } else if (message.contains("joined the game")) {
            command = "someone plays";
        } else if (message.contains("created a new game")) {
            command = "someone created a game";
        } else if (message.contains("your turn,")) {
            command = "your turn";
        } else if (message.contains("turn!")) {
            command = "it´s not your turn";
        } else if (message.contains("Type '#choose 1'")) {
            command = "choose cards";
        } else if (message.contains("Your guess was Incorrect.")) command = "incorrect";
        else if(message.contains("Winner of the round")) command = "someone won";
        else if(message.contains("Your guess was correct!")) command = "correct";
        String finalCommand = command;

        //The methods are not called directly from the controller,
        // the method calls take place in the JavaFX application thread.
        Platform.runLater(
                () -> {
                    switch (finalCommand) {
                        case "#login" -> loginController.ServerResponse(finalMessage);
                        case "someone joined" -> controller.setRoomUser(message.split(" ", 2)[0]);
                        case "#chat" -> controller.appendChatMessage(message);
                        case "someone plays" -> controller.setGamePlayer(message.split(" ", 2)[0]);
                        case "someone created a game" -> {
                            controller.setGamePlayer(message.split(" ", 2)[0]);
                            System.out.println("created");
                        }
                        case "#playerList:" -> controller.setFormerPlayer(finalMessage);
                        case "userList:" -> controller.setUserList(finalMessage);
                        case "someone won" -> {
                            controller.increaseRoundLabel();
                            try {
                                controller.setWinnerRound(message.split(" ")[(message.split(" ")).length-1]);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        case "it´s not your turn" -> controller.serverMessage.setText(message.split(" ", 2)[1]);
                        case "your turn", "incorrect", "correct"  -> controller.serverMessage.setText(message);
                        case "choose cards" -> controller.chooseCards(message);
                        case "#score: " -> controller.updateScore(message.split(" "));
                        default -> System.out.println(message);
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

    public void setPopUpController(ControllerPopUp controllerPopUp) {
        this.controllerPopUp = controllerPopUp;
    }
}
