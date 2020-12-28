package server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

import static utilities.Utilities.PORT;

public class Server {

    /**
     * Logger to log information/warning
     */
    private static final Logger logger = LogManager.getLogger();

    /**
     * list of users that gets added after every new instance of user is created
     */
    private final ArrayList<User> users = new ArrayList<>(10);
    /**
     * port where the server is bound to listen
     */
    private final int port;
    /**
     * idGenerator to give id´s to the users
     */
    private final Random idGenerator = new Random();
    /**
     * Array with all the id´s that already exist
     */
    ArrayList<Integer> idNumbers = new ArrayList<>();

    /**
     * Constructor for the ChatServer class which initialises the port number.
     *
     * @param port port number where the server is bound to listen the client.
     */
    public Server(int port) {
        this.port = port;
    }

    /**
     * Main method
     */
    public static void main(String[] args) {
        Server server = new Server(PORT);
        server.start();
    }

    /**
     * Getter for the Users.
     *
     * @return returns the list of users.
     */
    public ArrayList<User> getUsers() {
        return users;
    }

    /**
     * It opens a channel for the connection between Server and Client.
     */
    public void start() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            logger.info("Chat server is waiting for clients to connect to port " + port + ".");
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    serverSocket.close();
                    logger.info("The server is shut down!");
                } catch (IOException e) { /* failed */ }
            }));
            acceptClients(serverSocket);
        } catch (IOException e) {
            logger.fatal("could not connect: " + e.getMessage());
        }
    }

    /**
     * This method accepts the clients request and ChatServer assigns a separate thread to handle multiple clients
     *
     * @param serverSocket socket from which connection is to be established
     */
    private void acceptClients(ServerSocket serverSocket) {
        boolean accept = true;
        while (accept) {
            try {
                Socket clientSocket = serverSocket.accept();
                logger.info("Accepted the connection from address: " + clientSocket.getRemoteSocketAddress());
                User user = new User();
                users.add(user);
                UserThread thread = new UserThread(clientSocket, this, user);
                thread.start();
            } catch (IOException e) {
                accept = false;
                logger.info("Accept failed on: " + port);
            }
        }
    }

    /**
     * This method sends a message to each client which is connected to the server except the sender itself.
     *
     * @param message the message which is to be sent.
     * @param sender  the user who is responsible for sending the message.
     */
    public void communicate(String message, User sender) {

        for (User user : users) {
            if (user != sender) {
                user.message(message);
            }
        }
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
        users.remove(user);
    }

    /**
     * This method creates an id which is not assigned to any user yet
     *
     * @return a new ID for the user
     */
    public int getNewID() {
        int newID;
        do {
            newID = idGenerator.nextInt(Integer.MAX_VALUE);
            idNumbers.add(newID);
            return newID;
        } while (idNumbers.contains(newID));

    }
}
