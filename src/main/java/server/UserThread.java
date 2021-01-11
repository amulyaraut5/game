package server;

import game.gameObjects.maps.Blueprint;
import game.gameObjects.maps.DizzyHighway;
import game.gameObjects.maps.RiskyCrossing;
import game.gameObjects.tiles.Attribute;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.JSONProtocol.JSONBody;
import utilities.JSONProtocol.JSONMessage;
import utilities.JSONProtocol.Multiplex;
import utilities.JSONProtocol.body.Error;
import utilities.JSONProtocol.body.*;
import utilities.Utilities.MessageType;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Handles connection for each connected client,
 * therefore the server is able to handle multiple clients at the same time.
 *
 * @author vossa,
 */

public class UserThread extends Thread {

    /**
     * Logger to log information/warning
     */
    private static final Logger logger = LogManager.getLogger();
    private static int idCounter = 1; //Number of playerIDs is saved to give new player a new number
    private static ArrayList<PlayerAdded> addedPlayers = new ArrayList<>(10);
    private final User user; //Connected user, which data has to be filled in logIn()
    private final Socket socket;
    private final Server server = Server.getInstance();
    private final double protocol = 1.0;
    private PrintWriter writer;
    private BufferedReader reader;
    private boolean exit = false;
    private String map;

    public UserThread(Socket socket, User user) {
        this.socket = socket;
        this.user = user;

        this.user.setThread(this);

        try {
            InputStream input = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);
        } catch (IOException ex) {
            disconnect(ex);
        }
        Attribute.setUserThread(this);
    }

    public void setMap(String map) {
        this.map = map;
    }

    /**
     * The method runs a loop of reading messages from the user and sending them to all other users.
     * The user disconnects by typing "bye".
     */
    @Override
    public void run() {
        try {
            sendMessage(new HelloClient(protocol));

            while (!exit) {
                String text = reader.readLine();
                if (text == null) {
                    throw new IOException();
                }
                if (text.equals("DizzyHighway") || text.equals("RiskyCrossing")) {
                    setMap(text);
                } else {
                    //logger.debug("Protocol received: " + text);
                    JSONMessage msg = Multiplex.deserialize(text);
                    handleMessage(msg);
                }
            }

        } catch (IOException ex) {
            disconnect(ex);
        }
        disconnect();
    }

    /**
     * prints a message for specific user
     *
     * @param jsonBody the JSONBody of the message to sent
     */
    public void sendMessage(JSONBody jsonBody) {
        String json = Multiplex.serialize(new JSONMessage(jsonBody));
        //logger.debug("Protocol sent: " + json);
        writer.println(json);
        writer.flush();
    }

    /**
     * Based on the messageType the various protocol are differentiated and Object class type
     * is casted down to respective class.
     *
     * @param message received Object of JSONMessage
     */
    private void handleMessage(JSONMessage message) {
        MessageType type = message.getType();

        switch (type) {
            case HelloServer -> {
                HelloServer hs = (HelloServer) message.getBody();
                if (hs.getProtocol() == protocol) {
                    user.setId(idCounter++);
                    currentThread().setName("UserThread-" + user.getId());
                    sendMessage(new Welcome(user.getId()));

                    for (PlayerAdded addedPlayer : addedPlayers) {
                        sendMessage(addedPlayer);
                    }
                } else {
                    JSONBody error = new Error("Protocols don't match! " +
                            "Client Protocol: " + hs.getProtocol() + ", Server Protocol: " + protocol);
                    sendMessage(error);
                    disconnect();
                }
            }
            case PlayerValues -> {
                PlayerValues pv = (PlayerValues) message.getBody();
                boolean figureTaken = false;
                for (User user : server.getUsers()) {
                    if (pv.getFigure() == user.getFigure()) {
                        figureTaken = true;
                        break;
                    }
                }
                if (!figureTaken) {
                    user.setId(user.getId());
                    user.setName(pv.getName());
                    user.setFigure(pv.getFigure());
                    PlayerAdded addedPlayer = new PlayerAdded(user.getId(), pv.getName(), pv.getFigure());
                    addedPlayers.add(addedPlayer);
                    server.communicateAll(addedPlayer);
                } else {
                    JSONBody error = new Error("Robot is already taken!");
                    sendMessage(error);
                }
            }
            case SetStatus -> {
                SetStatus status = (SetStatus) message.getBody();
                server.communicateAll(new PlayerStatus(user.getId(), status.isReady()));
                boolean allUsersReady = server.setReadyStatus(user, status.isReady());

                if (allUsersReady) {
                    Blueprint chosenBlueprint = null;
                    if (map.equals("DizzyHighway")) {
                        chosenBlueprint = new DizzyHighway();
                    } else if (map.equals("RiskyCrossing")) {
                        chosenBlueprint = new RiskyCrossing();
                    }
                    server.startGame(chosenBlueprint);
                }
            }
            case SendChat -> {
                SendChat sc = (SendChat) message.getBody();
                if (sc.getTo() < 0)
                    server.communicateUsers(new ReceivedChat(sc.getMessage(), this.user.getName(), false), this);
                else {
                    server.communicateDirect(new ReceivedChat(sc.getMessage(), this.user.getName(), true), this, sc.getTo());
                }
            }
            case SelectCard -> {
                SelectCard selectCard = (SelectCard) message.getBody();
                //TODO send selectCard to ProgrammingPhase
                //Game.getInstance().messageToPhases(selectCard);
                server.communicateUsers(new CardSelected(this.user.getId(), selectCard.getRegister()), this);
            }
            default -> logger.error("The MessageType " + type + " is invalid or not yet implemented!");
        }
    }

    /**
     * The connection is closed and other users get notified that the user left.
     */
    private void disconnect() {
        if (!exit) {
            exit = true;
            //sendMessage("Bye " + user);
            server.removeUser(user);
            //server.communicate(user + " left the room.", user);
            logger.warn("Closed the connection with address:   " + socket.getRemoteSocketAddress());
            try {
                socket.close();
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
    }

    /**
     * Method is called if an Exception occurs during connection.
     * The connection is tried to close and other users get notified that the user left.
     *
     * @param ex Exception which occurred
     */
    private void disconnect(Exception ex) {
        if (!exit) {
            exit = true;
            logger.fatal("Error in UserThread with address " + socket.getRemoteSocketAddress() + ": " + ex.getMessage());
            server.removeUser(user);
            //server.communicate(user + " left the room.", user);
            logger.fatal("Closed the connection with address:   " + socket.getRemoteSocketAddress());
            try {
                socket.close();
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
    }

    private void logIn(String userName) {
        if (!server.isAvailable(userName)) {

            //TODO sendMessage(new JSONMessage("userNameTaken", "true"));
            //else {
            //sendMessage(new JSONMessage("userNameTaken", "false"));
            user.setName(userName);
            user.setId(user.getId());
            welcome();
        }

    }

    /**
     * Sends welcome message to the user and notifies all other users.
     */
    private void welcome() {
        //server.communicate(user + " joined the room.", user);
    }
}
