package server;

import game.GameController;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * This class mainly deals with establishing connection between server and client and helps in
 * communication between different users.
 *
 * @author louis and amulya
 */
public class ChatServer {
    private static final ArrayList<User> users = new ArrayList<>(10);
    private final int port;
    private final GameController gameController = new GameController(this);

    /**
     * Constructor for the ChatServer class which initialises the port number.
     * @param port port number where the server is bound to listen the client.
     */
    public ChatServer(int port) {
        this.port = port;
    }

    /**
     * Main method
     */
    public static void main(String[] args) {
        int port = 4444;

        ChatServer server = new ChatServer(port);
        server.start();
    }

    /**
     * Getter method for the Users.
     * @return returns the list of users.
     */
    public ArrayList<User> getUsers() {
        return users;
    }

    /**
     * It opens a channel for the connection between Server and Client.
     *
     */
    private void start() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Chat server is waiting for clients to connect to port " + port + ".");
            acceptClients(serverSocket);
        } catch (IOException e) {
            System.err.println("could not connect: " + e.getMessage());
        }
    }

    /**
     * This method accepts the clients request and ChatServer assigns a separate thread to handle multiple clients
     *
     * @param serverSocket socket from which connection is to be established
     */
    public void acceptClients(ServerSocket serverSocket) {
        boolean accept = true;
        while (accept) {
            try {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Accepted the connection from address: " + clientSocket.getRemoteSocketAddress());
                User user = new User();
                users.add(user);
                UserThread thread = new UserThread(clientSocket, this, user);
                thread.start();
            } catch (IOException e) {
                accept = false;
                System.out.println("Accept failed on: " + port);
            }
        }
    }

    /**
     * This method sends a message to each client which is connected to the server except the sender itself.
     * @param message the message which is to be sent.
     * @param sender the user who is responsible for sending the message.
     */
    public void communicate(String message, User sender) {

        for (User user : users) {
            if (user != sender) {
                user.message(message);
            }
        }
    }

    /**
     * Method to check if input for sending a direct message is valid.
     * It cuts the assigns username out of the message and checks if it was entered and/or
     * corresponds to an existing username.
     *
     * @param message String of user input in form '@<username> text'
     * @param sender user who sent the direct message
     * @return shows if user input for direct message was valid
     */
    public boolean communicateDirect(String message, User sender) {
        String userName = (message.split(" ", 2)[0]);
        String destinationUser = userName.substring(1);
        String messageUser = message.split(" ", 2)[1];
        if (messageUser.length() == 0) return false;
        for (User user : users) {
            if (user.getName().equals(destinationUser)) {
                user.message("[" + sender + " (direct)]: " + messageUser);
                return true;
            }
        }
        return false;
    }

    /**
     * Method to send user input including the game commands to the gameController.
     *
     * @param message String of user input including the command for the game
     * @param sender user who sent the message
     */

    public void communicateGame(String message, User sender) {
        gameController.readCommand(message, sender);
    }

    /**
     * It sends messages to all the user.
     * @param message message to be sent.
     */
    public void communicateAll(String message) {
        for (User user : users)
            user.message(message);
    }

    /**
     * It checks if the username is already used of another user.
     *
     * @param userName userName to be checked
     * @return True if username is free, false if it´s already assigned
     */
    public synchronized boolean isAvailable(String userName) {
        for (User u : users) {
            if (u.getName().equals(userName)) {
                return false;
            }
        }
        return true;
    }

    /**
     * This method removes a user from the list of users.
     *
     * @param user User to be removed
     */
    public void removeUser(User user) {
        //users.remove(user);//TODO remove player also

    }
}
