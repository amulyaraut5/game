package server;

import game.Game;
import game.Player;
import javafx.application.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.Constants;
import utilities.JSONProtocol.JSONBody;
import utilities.JSONProtocol.JSONMessage;
import utilities.JSONProtocol.Multiplex;
import utilities.JSONProtocol.body.Error;
import utilities.JSONProtocol.body.*;
import utilities.QueueMessage;
import utilities.enums.GameState;
import utilities.enums.MessageType;
import utilities.enums.ServerState;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static utilities.Constants.PORT;

/**
 * This class mainly deals with establishing connection between server and client and helps in
 * communication between different users.
 *
 * @author Louis
 * @author amulya
 * @author janau
 */

public class Server extends Thread {

    private static final Logger logger = LogManager.getLogger();
    private static Server instance;
    private Game game;
    private ServerController serverController;

    /** saves all connected users */
    private final ArrayList<User> users = new ArrayList<>(10);

    /** saves all users that clicked ready in the view */
    private final ArrayList<User> readyUsers = new ArrayList<>();

    /** Queue to read json messages step by step */
    private final BlockingQueue<QueueMessage> messageQueue = new LinkedBlockingQueue<>();

    /** stores all users and and their status(either ready or false) */
    private final HashMap<User, Boolean> usersAI = new HashMap<>();

    /** list of AIs */
    private final ArrayList<User> AIs = new ArrayList<>();

    /** list of non AIs */
    private final ArrayList<User> notAIs = new ArrayList<>();

    /** port at which server is open for client to connect */
    private int port = Constants.PORT;

    /** an instance of User */
    private User selectUser = null;

    /** number of playersIDs is saved to give a new player a new number */
    private int idCounter = 1;

    /** ServerState that shows that the clients are in the lobby currently */
    private ServerState serverState = ServerState.LOBBY;

    private boolean isMapSelected = false;
    private boolean isMapSent = false;
    private String selectedMap;

    private Server() {
    }

    public void setPort(int port) {
        this.port = port;
    }

    /**
     * It opens a channel for the connection between Server and Client, and picks the message
     * from the blocking queue in the order they are received.
     */
    @Override
    public void run() {
        game = Game.getInstance();
        setName("ServerThread");
        logger.info("SERVER STARTED");

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            logger.info("Chat server is waiting for clients to connect to port " + serverSocket.getLocalPort() + ".");
            Thread acceptClients = new Thread(() -> acceptClients(serverSocket));
            acceptClients.setDaemon(true);
            acceptClients.start();

            while (!isInterrupted()) {
                synchronized (this) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        logger.warn(e.getMessage());
                    }
                }
                QueueMessage queueMessage;
                while ((queueMessage = messageQueue.poll()) != null) {
                    if (isMessageValid(queueMessage)) handleMessage(queueMessage);
                }
            }
        } catch (IOException e) {
            logger.error("Server could not be created: " + e.getMessage());
        }
        logger.info("SERVER CLOSED");
    }

    /**
     * checks if a received json message is valid, that means if the ServerState allows the message at this point
     *
     * @param queueMessage received Object of JSONMessage
     * @return boolean true if the message is valid at this point in the game/serverState
     */
    private boolean isMessageValid(QueueMessage queueMessage) {
        MessageType type = queueMessage.getJsonMessage().getType();
        boolean isValid = false;
        String state = serverState.toString();

        switch (serverState) {
            case LOBBY -> {
                isValid = serverState.getAllowedMessages().contains(type);
                if (!isValid) queueMessage.getUser().message(new Error("Game has not yet started!"));
            }
            case RUNNING_GAME -> {
                GameState gameState = game.getGameState();
                isValid = gameState.getAllowedMessages().contains(type);
                if (!isValid) {
                    queueMessage.getUser().message(new Error("Game has already started!"));
                    state += " " + gameState;
                }
            }
        }
        if (!isValid)
            logger.warn("MessageType " + type + " is not valid in the current state: " + state + "!");
        return isValid;
    }

    /**
     * Based on the messageType the various protocol are differentiated and Object class type
     * is casted down to respective class.
     *
     * @param queueMessage received Object of JSONMessage
     */
    private void handleMessage(QueueMessage queueMessage) {
        JSONMessage message = queueMessage.getJsonMessage();
        MessageType type = message.getType();
        User user = queueMessage.getUser();
        switch (type) {
            case HelloServer -> {
                HelloServer hs = (HelloServer) message.getBody();
                addNewUser(user, hs);
            }
            case PlayerValues -> {
                PlayerValues pv = (PlayerValues) message.getBody();
                addPlayerValues(user, pv);
            }
            case SetStatus -> {
                SetStatus status = (SetStatus) message.getBody();
                communicateAll(new PlayerStatus(user.getID(), status.isReady()));
                dealWithMap(user, status);
            }
            case SendChat -> {
                SendChat sc = (SendChat) message.getBody();
                if (sc.getTo() < 0) {
                    cheat(sc, user);
                } else {
                    communicateDirect(new ReceivedChat(sc.getMessage(), user.getID(), true), sc.getTo());
                }
            }
            case SelectCard -> {
                SelectCard selectCard = (SelectCard) message.getBody();
                game.getProgrammingPhase().handleCardSelection(game.userToPlayer(user), selectCard);
                communicateUsers(new CardSelected(user.getID(), selectCard.getRegister()), user);
            }
            case SetStartingPoint -> {
                SetStartingPoint setStartingPoint = (SetStartingPoint) message.getBody();
                game.getConstructionPhase().setStartingPoint(user, setStartingPoint.getPosition());
            }
            case PlayIt -> {
                if (AIs.equals(readyUsers)) {
                    Timer t = new Timer();
                    t.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            game.getActivationPhase().activateCards(user.getID());
                        }
                    }, 1900);
                } else
                    game.getActivationPhase().activateCards(user.getID());
            }
            case MapSelected -> {
                MapSelected selectMap = (MapSelected) message.getBody();
                selectedMap = selectMap.getMap().get(0);
                isMapSelected = true;
                communicateAll(selectMap);

                if ((readyUsers.size() == users.size()) && readyUsers.size() > 1) {
                    game.play();
                    serverState = ServerState.RUNNING_GAME;
                }
            }
            case SelectDamage -> {
                SelectDamage selectDamage = (SelectDamage) message.getBody();
                game.getActivationPhase().handleSelectedDamage(selectDamage, user);
            }
            default -> logger.error("The MessageType " + type + " is invalid or not yet implemented!");
        }
    }

    /**
     * determines if the message is a cheat or not
     *
     * @param sc   the received Chat protocol
     * @param user the user who sent the chat
     */
    private void cheat(SendChat sc, User user) {
        String message = sc.getMessage();
        Pattern cheatPattern = Pattern.compile("^#+");
        Matcher cheatMatcher = cheatPattern.matcher(message);
        if (cheatMatcher.lookingAt()) {
            if (serverState == ServerState.RUNNING_GAME) {
                game.handleCheat(message, user);
            } else user.message(new Error("Cheats are deactivated at the lobby."));
        } else {
            communicateUsers(new ReceivedChat(sc.getMessage(), user.getID(), false), user);
        }
    }

    /**
     *
     * @param user is the current player
     * @param pv is the type of protocol message which is handled
     */
    private void addPlayerValues(User user, PlayerValues pv) {
        boolean figureTaken = false;
        for (User userLoop : getUsers()) {
            if (pv.getFigure() == userLoop.getFigure()) {
                figureTaken = true;
                break;
            }
        }
        if (!figureTaken) {
            if (!pv.getName().isBlank()) {
                user.setID(user.getID());
                user.setName(pv.getName());
                user.setFigure(pv.getFigure());
                communicateAll(new PlayerAdded(user.getID(), pv.getName(), pv.getFigure()));
            } else {
                user.message(new Error("Username can't be blank!"));
            }
        } else {
            user.message(new Error("Robot is already taken!"));
        }
    }

    /**
     *
     * @param user is the current player
     * @param hs is the type of protocol message which is handled
     */
    private void addNewUser(User user, HelloServer hs) {
        if (hs.getProtocol() == Constants.PROTOCOL) {
            if (users.size() <= 6) {
                user.setID(idCounter++);
                currentThread().setName("UserThread-" + user.getID());
                if (hs.isAI()) {
                    AIs.add(user);
                } else notAIs.add(user);

                user.message(new Welcome(user.getID()));

                for (User u : users) {
                    if (u.getName() != null) {
                        user.message(new PlayerAdded(u.getID(), u.getName(), u.getFigure()));
                    }
                }
                for (User readyUser : readyUsers) {
                    user.message(new PlayerStatus(readyUser.getID(), true));
                }
                if (selectedMap != null) user.message(new MapSelected(selectedMap));
            } else {
                user.message(new Error("Server is already full!"));
                user.getThread().disconnect();
            }
        } else {
            JSONBody error = new utilities.JSONProtocol.body.Error("Protocols don't match! " +
                    "Client Protocol: " + hs.getProtocol() + ", Server Protocol: " + Constants.PROTOCOL);
            user.message(error);
            user.getThread().disconnect();
        }
    }
    /**
     * This method sends the SelectMap protocol message to the first user who presses ready. If the same
     * player presses unready, then the SelectMap protocol message is sent to the next player
     * in the playersList.
     * If all users are AIs, a random map is selected.
     * In addition to that, it calls the play method from the {@link Game} to start the game
     *
     * @param user is the current player
     * @param status is the type of protocol message which is handled
     */
    private void dealWithMap(User user, SetStatus status) {
        usersAI.put(user, status.isReady());

        ArrayList<String> maps = new ArrayList<>();
        Random r = new Random();
        maps.add("DizzyHighway");
        maps.add("ExtraCrispy");

        if (!status.isReady() && selectUser==user) {
            isMapSent = false;
            isMapSelected = false;
        }
        if (!isMapSent && !isMapSelected) {
            for (User user1 : notAIs) {
                try {
                    if (usersAI.get(user1)) {
                        user1.message(new SelectMap(maps));
                        isMapSent = true;
                        selectUser = user1;
                        break;
                    }
                } catch (NullPointerException ignored) {
                }
            }
        }
        boolean allUsersReady = setReadyStatus(user, status.isReady());

        if (allUsersReady && isMapSelected) {
            game.play();
            serverState = ServerState.RUNNING_GAME;
        }
        if (allUsersReady && (AIs.size() == readyUsers.size())) {
            selectedMap = maps.get(r.nextInt(maps.size()));
        }
    }

    public void startAIGame() {
        game.play();
        serverState = ServerState.RUNNING_GAME;
    }

    /**
     * This method resets all the things from the server, makes ready for the new game.
     *
     * @param winnerID id of the player who has won the game
     */

    public void gameWon(int winnerID) {
        serverState = ServerState.LOBBY;
        readyUsers.clear();
        isMapSelected = false;
        isMapSent = false;
        communicateAll(new GameWon(winnerID));
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
                UserThread userThread = new UserThread(clientSocket, user);
                userThread.setDaemon(true);
                userThread.start();
            } catch (IOException e) {
                accept = false;
                logger.error("Accepting clients failed on port " + PORT + ": " + e.getMessage());
            }
        }
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

    /**
     * This method sends a message to each client which is connected to the server except the sender itself.
     * @param jsonBody type of protocol message which needs to be sent.
     * @param sender the user who is responsible for sending the message.
     */

    synchronized public void communicateUsers(JSONBody jsonBody, User sender) {
        String json = Multiplex.serialize(JSONMessage.build(jsonBody));
        logger.debug("Protocol sent: " + json);
        for (User user : users) {
            if (!user.equals(sender)) {
                user.message(jsonBody);
            }
        }
    }

    /**
     * This method sends the message to all the connected users.
     *
     * @param jsonBody type of protocol message which needs to be sent.
     */

    synchronized public void communicateAll(JSONBody jsonBody) {
        String json = Multiplex.serialize(JSONMessage.build(jsonBody));
        logger.debug("Protocol sent: " + json);
        for (User user : users) {
            user.message(jsonBody);
        }
        if (serverController != null) {
            Platform.runLater(() -> serverController.update(JSONMessage.build(jsonBody)));
        }
    }

    /**
     * It sends the message to the particular user based on the id of the player.
     *
     * @param jsonBody type of protocol message which needs to be sent.
     * @param receiver id of the receiver
     */

    synchronized public void communicateDirect(JSONBody jsonBody, int receiver) {
        for (User user : users) {
            if (user.getID() == receiver) {
                user.message(jsonBody);
            }
        }
    }

    public void setServerController(ServerController serverController) {
        this.serverController = serverController;
    }

    /**
     * This method removes a saved user from the server and game.
     * If no user is left on the server, the server is closed.
     *
     * @param user User to be removed
     */
    public void removeUser(User user) {

        if (serverState == ServerState.LOBBY) {
            readyUsers.remove(user);
        } else {
            game.getPlayers().remove(game.userToPlayer(user));
            Player temp = null;
            for (Player player : game.getActivePlayers()) {
                if (player.getID() == user.getID()) {
                    temp = player;
                }
            }
            game.getActivePlayers().remove(temp);

            //if the player is not the only one left in the game theres demand for further handling
            if (!(game.getPlayers() == null)) {

                if (game.getGameState() == GameState.CONSTRUCTION) {

                    //If its the users turn to set his starting point and he is not the last player that was left to do so
                    //the next player is set to pick a start point, otherwise the next phase is called
                    if (user.getID() == game.getConstructionPhase().getCurrentPlayer().getID()) {

                        if (game.getConstructionPhase().getCurrentIndex() < game.getPlayers().size()) {
                            Player currentPlayer = game.getPlayers().get(game.getConstructionPhase().getCurrentIndex());
                            game.getConstructionPhase().setCurrentPlayer(currentPlayer);
                            communicateAll(new CurrentPlayer(currentPlayer.getID()));
                        } else {
                            game.nextPhase();
                        }
                    }
                }
                if (game.getGameState() == GameState.PROGRAMMING) {
                    //if the user has not put down his cards already he is removed from the list tracking this
                    game.getProgrammingPhase().getNotReadyPlayers().remove(game.userToPlayer(user));
                    //if he was the last one missing filling his registers the timer is stopped
                    if (game.getProgrammingPhase().getNotReadyPlayers().isEmpty()) {
                        game.getProgrammingPhase().endProgrammingTimer();
                    }
                }
                if (game.getGameState() == GameState.ACTIVATION)
                    game.getActivationPhase().removeCurrentCards(user.getID());
            }
        }
        users.remove(user);
        communicateAll(new ConnectionUpdate(user.getID(), false, "Remove"));
        if (user.getName() != null) communicateAll(new Error(user.getName() + " left the game."));

        if (users.size() == 0) {
            interrupt();
            synchronized (this) {
                notify();
            }
        }
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
     * Getter for the Users.
     *
     * @return returns the list of users.
     */
    public ArrayList<User> getUsers() { return users; }

    public BlockingQueue<QueueMessage> getMessageQueue() { return messageQueue; }

    public void setServerState(ServerState serverState) { this.serverState = serverState; }

    public ArrayList<User> getReadyUsers() { return readyUsers; }

    public String getSelectedMap() { return selectedMap; }

    public ServerState getServerState() { return serverState; }
}
