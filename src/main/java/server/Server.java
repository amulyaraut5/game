package server;

import game.gameObjects.maps.Blueprint;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.JSONProtocol.JSONBody;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import static utilities.Utilities.PORT;

public class Server extends Thread {

    private static final Logger logger = LogManager.getLogger();
    private static Server instance;
    private final ArrayList<User> users = new ArrayList<>(10); //all Users
    private ArrayList<User> readyUsers = new ArrayList<>(); //Users which pressed ready

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
        currentThread().setName("ServerThread");
        logger.info("SERVER STARTED");

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
                UserThread thread = new UserThread(clientSocket, user);
                thread.start();
            } catch (IOException e) {
                accept = false;
                logger.info("Accept failed on: " + PORT);
            }
        }
    }

    public void setMap(Blueprint blueprint) {
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
     * Adds a user to the list of ready users if they are ready, or removes them if not.
     * It is tested, if the player was already ready/ not ready.
     *
     * @param user  User to change the status for
     * @param ready true if ready, otherwise false
     * @return true if all players (min. 2) are ready
     */
    public boolean setReadyStatus(User user, boolean ready) {
        if (ready) {
            if (!readyUsers.contains(user)) readyUsers.add(user);
        } else {
            if (readyUsers.contains(user)) readyUsers.remove(user);
        }
        return (users.size() == readyUsers.size() && users.size() > 1);
    }

    public void communicateUsers(JSONBody jsonBody, UserThread sender) {
        for (User user : users) {
            if (user.getThread() != sender) {
                user.message(jsonBody);
            }
        }
    }

    public void communicateAll(JSONBody jsonBody) {
        for (User user : users) {
            user.message(jsonBody);
        }
    }

    public void communicateDirect(JSONBody jsonBody, UserThread sender, int receiver) {
        for (User user : users) {
            if (user.getId() == receiver) {
                user.message(jsonBody);
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
}
