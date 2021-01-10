package server;

import game.gameObjects.maps.Blueprint;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.JSONProtocol.JSONMessage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import static utilities.Utilities.PORT;

public class Server extends Thread {

    /**
     * Logger to log information/warning
     */
    private static final Logger logger = LogManager.getLogger();
    private static Server instance;
    /**
     * list of users that gets added after every new instance of user is created
     */
    private final ArrayList<User> users = new ArrayList<>(10);
    /**
     *
     */
    private ArrayList<UserThread> readyPlayerList = new ArrayList<>();
    private ArrayList<JSONMessage> playerValuesList = new ArrayList<>();
    private Blueprint blueprint;
    /**
     *
     */
    private ArrayList<UserThread> addedPlayerList = new ArrayList<>();

    /**
     * private Constructor for the ChatServer class
     */
    private Server() {
    }

    /**
     * Main method starts only the server separately
     */
    public static void main(String[] args) {
        Server.getInstance().start();
    }

    /**
     * Gets the singleton instance of server.
     *
     * @return instance of server
     */
    public static Server getInstance() {
        if (instance == null) instance = new Server();
        return instance;
    }

    /**
     * It opens a channel for the connection between Server and Client.
     */
    @Override
    public void run() {
        logger.info("SERVER STARTED");
        currentThread().setName("ServerThread");

        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            logger.info("Chat server is waiting for clients to connect to port " + PORT + ".");

            acceptClients(serverSocket);

        } catch (IOException e) {
            logger.error("could not connect: " + e.getMessage());
        }
        logger.info("SERVER CLOSED");
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
                logger.info("Accept failed on: " + PORT);
            }
        }
    }

    public void setMap(Blueprint blueprint) {
        this.blueprint = blueprint;
    }

    /**
     * Getter for the Users.
     *
     * @return returns the list of users.
     */
    public ArrayList<User> getUsers() {
        return users;
    }

    public void changeReadyPlayerList(int change, UserThread userThread) {
        //remove this userThread
        if (change == 0) {
            readyPlayerList.remove(userThread);
        }
        //add  this userThread
        else if (change == 1) {
            readyPlayerList.add(userThread);
        }
        if (readyPlayerList.size() >= 2) {
            logger.info("Tiles can be initialized");
            //JSONMessage jsonMessage = new JSONMessage(new GameStarted("test"));
            //communicateUsers();
        }
    }

    public void communicateUsers(JSONMessage jsonMessage, UserThread sender) {
        for (User user : users) {
            if (user.getThread() != sender) {
                user.message(jsonMessage);
            }
        }
    }

    public void communicateAll(JSONMessage jsonMessage) {
        for (User user : users) {
            user.message(jsonMessage);
        }
    }

    public void communicateDirect(JSONMessage jsonMessage, UserThread sender, int receiver) {
        for (User user : users) {
            logger.info("User" + user.getId());
            logger.info("Jsonm" + jsonMessage.toString());

            if (user.getId() == receiver) {
                user.message(jsonMessage);
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

    public ArrayList<JSONMessage> getPlayerValuesList() {
        return playerValuesList;
    }

    public void addToPlayerValuesList(JSONMessage playerAddedMessage) {
        this.playerValuesList.add(playerAddedMessage);
    }
}
