package server;

import game.Game;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.JSONProtocol.JSONBody;
import utilities.JSONProtocol.JSONMessage;
import utilities.JSONProtocol.body.Error;
import utilities.JSONProtocol.body.*;
import utilities.Utilities;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static utilities.Utilities.PORT;

public class Server extends Thread {

    private static final Logger logger = LogManager.getLogger();
    private static Server instance;
    private final ArrayList<PlayerAdded> addedPlayers = new ArrayList<>(10);
    private final ArrayList<User> users = new ArrayList<>(10); //all Users
    private final ArrayList<User> readyUsers = new ArrayList<>(); //Users which pressed ready
    private final BlockingQueue<QueueMessage> messageQueue = new LinkedBlockingQueue<>();
    private Game game;
    private int idCounter = 1; //Number of playerIDs is saved to give new player a new number
    private boolean exit = false; //TODO Server

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
        game = Game.getInstance();
        setName("ServerThread");
        logger.info("SERVER STARTED");

        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            logger.info("Chat server is waiting for clients to connect to port " + PORT + ".");
            Thread acceptClients = new Thread(() -> acceptClients(serverSocket));
            acceptClients.start();

            while (!exit) {
                synchronized (this) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        logger.warn(e.getMessage());
                    }
                }
                QueueMessage queueMessage;
                while ((queueMessage = messageQueue.poll()) != null) {
                    handleMessage(queueMessage);
                }

            }
        } catch (IOException e) {
            logger.error("Server could not be created: " + e.getMessage());
        }
        logger.info("SERVER CLOSED");
    }

    /**
     * Based on the messageType the various protocol are differentiated and Object class type
     * is casted down to respective class.
     *
     * @param queueMessage received Object of JSONMessage
     */
    private void handleMessage(QueueMessage queueMessage) {
        JSONMessage message = queueMessage.getJsonMessage();
        Utilities.MessageType type = message.getType();
        User user = queueMessage.getUser();
        switch (type) {
            case HelloServer -> {
                HelloServer hs = (HelloServer) message.getBody();
                if (hs.getProtocol() == Utilities.PROTOCOL) {
                    user.setId(idCounter++);
                    currentThread().setName("UserThread-" + user.getId());
                    user.message(new Welcome(user.getId()));

                    for (PlayerAdded addedPlayer : addedPlayers) {
                        user.message(addedPlayer);
                    }
                } else {
                    JSONBody error = new utilities.JSONProtocol.body.Error("Protocols don't match! " +
                            "Client Protocol: " + hs.getProtocol() + ", Server Protocol: " + Utilities.PROTOCOL);
                    user.message(error);
                    user.getThread().disconnect();
                }
            }
            case PlayerValues -> {
                PlayerValues pv = (PlayerValues) message.getBody();
                boolean figureTaken = false;
                for (User userLoop : getUsers()) {
                    if (pv.getFigure() == userLoop.getFigure()) {
                        figureTaken = true;
                        break;
                    }
                }
                if (!figureTaken) {
                    if (!pv.getName().isBlank()){
                        user.setId(user.getId());
                        user.setName(pv.getName());
                        user.setFigure(pv.getFigure());
                        PlayerAdded addedPlayer = new PlayerAdded(user.getId(), pv.getName(), pv.getFigure());
                        addedPlayers.add(addedPlayer);
                        communicateAll(addedPlayer);
                    }else{
                        user.message(new Error("Username can't be blank!"));
                    }
                } else {
                    user.message(new Error("Robot is already taken!"));
                }
            }
            case SetStatus -> {
                SetStatus status = (SetStatus) message.getBody();
                communicateAll(new PlayerStatus(user.getId(), status.isReady()));
                boolean allUsersReady = setReadyStatus(user, status.isReady());
                if (allUsersReady) {
                    game.play();
                }
                //user.message(new YourCards(programmingCards));
            }
            case SendChat -> {
                SendChat sc = (SendChat) message.getBody();
                if (sc.getTo() < 0)
                    communicateUsers(new ReceivedChat(sc.getMessage(), user.getId(), false), user);
                else {
                    communicateDirect(new ReceivedChat(sc.getMessage(), user.getId(), true), sc.getTo());
                }
            }
            case SelectCard -> {
                SelectCard selectCard = (SelectCard) message.getBody();
                //TODO send selectCard to ProgrammingPhase
                //Game.getInstance().messageToPhases(selectCard);
                communicateUsers(new CardSelected(user.getId(), selectCard.getRegister()), user);
            }
            case SetStartingPoint -> {
                SetStartingPoint setStartingPoint = (SetStartingPoint) message.getBody();

                game.setStartingPoint(user, setStartingPoint.getPosition());
            }
            default -> logger.error("The MessageType " + type + " is invalid or not yet implemented!");
        }
    }

    public BlockingQueue<QueueMessage> getMessageQueue() {
        return messageQueue;
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
                logger.error("Accepting clients failed on port " + PORT + ": " + e.getMessage());
            }
        }
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
        } else readyUsers.remove(user);

        return (users.size() == readyUsers.size() && users.size() > 1);
    }

    public void communicateUsers(JSONBody jsonBody, User sender) {
        for (User user : users) {
            if (!user.equals(sender)) {
                user.message(jsonBody);
            }
        }
    }

    public void communicateAll(JSONBody jsonBody) {
        for (User user : users) {
            user.message(jsonBody);
        }
    }

    public void communicateDirect(JSONBody jsonBody, int receiver) {
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
        readyUsers.remove(user);
        users.remove(user);
    }
}
