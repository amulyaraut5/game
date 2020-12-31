package server;

import game.gameObjects.tiles.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.JSONProtocol.JSONMessage;
import utilities.JSONProtocol.Multiplex;
import utilities.JSONProtocol.body.GameStarted;
import utilities.JSONProtocol.body.gameStarted.Maps;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

import static utilities.Utilities.*;

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
     * idGenerator to give id´s to the users
     */
    private final Random idGenerator = new Random();
    /**
     * Array with all the id´s that already exist
     */
    private ArrayList<Integer> idNumbers = new ArrayList<>();

    /**
     *
     */
    private ArrayList<UserThread> readyPlayerList = new ArrayList<>();

    private ArrayList<JSONMessage> playerValuesList = new ArrayList<>();


    /**
     *
     */
    private ArrayList<UserThread> addedPlayerList = new ArrayList<>();

    /**
     * Constructor for the ChatServer class
     */
    public Server() {
    }

    /**
     * Main method
     */
    public static void main(String[] args) {
        new Server().start();
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

    /**
     * It opens a channel for the connection between Server and Client.
     */
    public void start() {
        logger.info("SERVER STARTED");
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            logger.info("Chat server is waiting for clients to connect to port " + PORT + ".");


            //try out "GameStarted":
            Attribute attributeA = new RotatingBelt(DOWN_RIGHT, true, 2);
            int[] registers = {2, 4};
            Attribute attributeB = new PushPanel(Orientation.LEFT, registers);
            Attribute attributeC = new Wall(UP_RIGHT);
            Attribute attributeD = new Laser(Orientation.DOWN, 1);

            ArrayList<Attribute> fieldList1 = new ArrayList<>();
            ArrayList<Attribute> fieldList2 = new ArrayList<>();
            ArrayList<Attribute> fieldList3 = new ArrayList<>();
            fieldList1.add(attributeA);
            fieldList2.add(attributeB);
            fieldList3.add(attributeC);
            fieldList3.add(attributeD);

            ArrayList<Maps> mapList = new ArrayList<>();
            mapList.add(new Maps(1, fieldList1));
            mapList.add(new Maps(2, fieldList2));
            mapList.add(new Maps(3, fieldList3));


            JSONMessage jMessage = new JSONMessage(new GameStarted(mapList));
            System.out.println(Multiplex.serialize(jMessage));

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    serverSocket.close();
                    logger.info("The server is shut down!");
                } catch (IOException e) { /* failed */ }
            }));
            acceptClients(serverSocket);
        } catch (IOException e) {
            logger.error("could not connect: " + e.getMessage());
        }
        logger.info("SERVER CLOSED");
    }

    public void communicateUsers(JSONMessage jsonMessage, UserThread sender) {
        for (User user : users) {
            if (user.getThread() != sender) {
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
        } while (!idNumbers.contains(newID));

    }

    public ArrayList<JSONMessage> getPlayerValuesList() {
        return playerValuesList;
    }

    public void addToPlayerValuesList(JSONMessage playerAddedMessage) {
        this.playerValuesList.add(playerAddedMessage);
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
}
