package server;

import game.gameObjects.maps.DizzyHighway;
import game.gameObjects.maps.MapFactory;
import game.gameObjects.maps.RiskyCrossing;
import game.gameObjects.tiles.Attribute;
import game.gameObjects.tiles.Tile;
import game.round.Laser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.JSONProtocol.JSONMessage;
import utilities.JSONProtocol.Multiplex;
import utilities.JSONProtocol.body.Error;
import utilities.JSONProtocol.body.*;
import utilities.MapConverter;
import utilities.Utilities.MessageType;

import java.io.*;
import java.net.Socket;

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
    private final User user; //Connected user, which data has to be filled in logIn()
    private final Socket socket;
    private final Server server;
    private final double protocol = 1.0;
    private int playerID;
    private PrintWriter writer;
    private BufferedReader reader;
    private boolean exit = false;
    MapConverter mapConverter = MapConverter.getInstance();
    MapFactory mapFactory = MapFactory.getInstance();
    private String map;

    public UserThread(Socket socket, Server server, User user) {

        this.socket = socket;
        this.server = server;
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

            // HelloClient protocol is first serialized and sent through socket to Client.
            JSONMessage jsonMessage = new JSONMessage(new HelloClient(protocol));
            sendMessage(jsonMessage);
            //<------------------------->

            while (!exit) {
                String text = reader.readLine();
                if (text == null) {
                    throw new IOException();
                }
                if(text.equals("DizzyHighway") || text.equals("RiskyCrossing")){
                    setMap(text);
                }
                else{
                    JSONMessage msg = Multiplex.deserialize(text);
                    handleMessage(msg);
                }
                //logger.debug("Protocol received: " + text);

                // After the reader object reads the serialized message from the socket it is then
                // deserialized and handled in handleMessage method.

            }

        } catch (IOException ex) {
            disconnect(ex);
        }
        if (!exit) disconnect();
    }

    /**
     * prints a message for specific user
     *
     * @param msg the message to be sent
     */
    public void sendMessage(JSONMessage msg) {
        String json = Multiplex.serialize(msg);
        //logger.debug("Protocol sent:" + json);
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

                //  <----------------Test For Laser --------------->
                //new Laser().activateBoardLaser();
                //  <----------------Test For Laser --------------->

                if (!(hs.getProtocol() == protocol)) {
                    //TODO send Error and disconnect the client
                    JSONMessage jsonMessage = new JSONMessage(new Error("Protocols don't match"));
                    logger.warn("Protocols don´t match");
                    logger.warn(Multiplex.serialize(jsonMessage));
                    sendMessage(jsonMessage);
                    //disconnect();
                } else {
                    playerID = idCounter++;
                    JSONMessage jsonMessage = new JSONMessage(new Welcome(playerID));
                    currentThread().setName("UserThread-" + playerID);
                    sendMessage(jsonMessage);
                    for (JSONMessage jM : server.getPlayerValuesList()) {
                        sendMessage(jM);
                        logger.info(jM.toString());
                    }
                }

            }
            case PlayerValues -> {
                PlayerValues ps = (PlayerValues) message.getBody();
                user.setName(ps.getName());
                JSONMessage jsonMessage = new JSONMessage(new PlayerAdded(playerID, ps.getName(), ps.getFigure()));
                server.addToPlayerValuesList(jsonMessage);
                user.setId(playerID);
                user.setName(ps.getName());
                server.communicateUsers(jsonMessage, this);
                /*for (JSONMessage jM  : server.getPlayerValuesList()) {
                    sendMessage(jM);
                    logger.info(jM.toString());
                }*/
                sendMessage(jsonMessage);
            }
            case SetStatus -> {
                SetStatus st = (SetStatus) message.getBody();
                JSONMessage jsonMessagePlayerStatus = new JSONMessage(new PlayerStatus(playerID, st.isReady()));
                if (st.isReady()) {
                    server.changeReadyPlayerList(1, this);
                } else {
                    server.changeReadyPlayerList(0, this);
                }
                server.communicateUsers(jsonMessagePlayerStatus, this);
                sendMessage(jsonMessagePlayerStatus);
                // TODO Start the game after min. players required join the game
                if(map.equals("DizzyHighway")){
                    DizzyHighway dizzyHighway = new DizzyHighway();
                    Tile[][] dizzy = mapFactory.constructMap(dizzyHighway);
                    GameStarted testBody1 = mapConverter.convert(dizzy);
                    JSONMessage testMessage = new JSONMessage(testBody1);
                    sendMessage(testMessage);
                    break;
                }
                else if(map.equals("RiskyCrossing")){
                    RiskyCrossing riskyCrossing = new RiskyCrossing();
                    Tile[][] testmap = mapFactory.constructMap(riskyCrossing);
                    GameStarted testbody = mapConverter.convert(testmap);
                    JSONMessage testmessage = new JSONMessage(testbody);
                    sendMessage(testmessage);
                    break;
                }
            }
            case SendChat -> {
                SendChat sc = (SendChat) message.getBody();
                if (sc.getTo() < 0)
                    server.communicateUsers(new JSONMessage(new ReceivedChat(sc.getMessage(), this.user.getName(), false)), this);
                else {
                    server.communicateDirect(new JSONMessage(new ReceivedChat(sc.getMessage(), this.user.getName(), true)), this, sc.getTo());
                    // TODO private Message
                }
            }
            case SelectCard -> {
                SelectCard selectCard = (SelectCard) message.getBody();
                //TODO send selectCard to ProgrammingPhase
                //Game.getInstance().messageToPhases(selectCard);
                server.communicateUsers(new JSONMessage(new CardSelected(this.playerID, selectCard.getRegister())), this);
            }
            case GameWon -> {
                GameWon gameWon = (GameWon) message.getBody();
                server.communicateUsers(new JSONMessage(new GameWon(gameWon.getPlayerID())), this);
                // TODO end the game
            }
            default -> logger.error("The MessageType " + type + " is invalid or not yet implemented!");

        }
    }

    /**
     * The connection is closed and other users get notified that the user left.
     */
    private void disconnect() {
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

    /**
     * Method is called if an Exception occurs during connection.
     * The connection is tried to close and other users get notified that the user left.
     *
     * @param ex Exception which occurred
     */
    private void disconnect(Exception ex) {
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

    private void logIn(String userName) {
        if (!server.isAvailable(userName)) {

            //TODO sendMessage(new JSONMessage("userNameTaken", "true"));
            //else {
            //sendMessage(new JSONMessage("userNameTaken", "false"));
            user.setName(userName);
            user.setId(playerID);
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
