package server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.JSONProtocol.JSONMessage;
import utilities.JSONProtocol.Multiplex;
import utilities.JSONProtocol.body.*;
import utilities.JSONProtocol.body.Error;
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

    private final User user; //Connected user, which data has to be filled in logIn()
    private final Socket socket;
    private final Server server;
    private final double protocol = 0.1;
    private int playerID;
    /**
     * Logger to log information/warning
     */
    private static final Logger logger = LogManager.getLogger();
    private PrintWriter writer;
    private BufferedReader reader;
    private boolean exit = false;

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
                logger.debug("Protocol received: " + text);

                // After the reader object reads the serialized message from the socket it is then
                // deserialized and handled in handleMessage method.
                JSONMessage msg = Multiplex.deserialize(text);
                handleMessage(msg);

            }

        } catch (IOException ex) {
            disconnect(ex);
        }
        if (!exit) disconnect();
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
            case HelloServer:
                // The messageBody which is Object is then casted down to HelloServer class
                HelloServer hs = (HelloServer) message.getBody();
                //logger.info("\n Received Protocol: " + type + "\n Group: " + hs.getGroup() + "\n Protocol: " + hs.getProtocol() + "\n isAI: " + hs.isAI());
                if (!(hs.getProtocol() == protocol)) {
                    //TODO send Error and disconnect the client
                    JSONMessage jsonMessage = new JSONMessage(new Error("Protocols don't match"));
                    logger.warn("Protocols don´t match");
                    logger.warn(Multiplex.serialize(jsonMessage));
                    sendMessage(jsonMessage);
                    //disconnect();
                } else {
                    playerID = server.getNewID();
                    JSONMessage jsonMessage = new JSONMessage(new Welcome(playerID));
                    currentThread().setName("UserThread-" + playerID);
                    sendMessage(jsonMessage);
                }
                break;
            case PlayerValues:
                    // The messageBody which is Object is then casted down to PlayerValues class
                PlayerValues ps = (PlayerValues) message.getBody();
                user.setName(ps.getName());
                JSONMessage jsonMessage = new JSONMessage(new PlayerAdded(playerID, ps.getName(), ps.getFigure() ));
                server.addToPlayerValuesList(jsonMessage);

                server.communicateUsers(jsonMessage, this);
                for (JSONMessage jM : server.getPlayerValuesList()){
                    sendMessage(jM);
                    logger.info(jM.toString());
                }
                break;
            case SetStatus:
                SetStatus st = (SetStatus) message.getBody();
                JSONMessage jsonMessagePlayerStatus = new JSONMessage(new PlayerStatus(playerID, st.isReady()));
                if(st.isReady()){
                    server.changeReadyPlayerList(1, this);
                } else {
                    server.changeReadyPlayerList(0, this);
                }
                sendMessage(jsonMessagePlayerStatus);
                break;
            case SendChat:
                SendChat sc = (SendChat) message.getBody();
                logger.info(this.user.getName());
                if(sc.getTo()<0){
                    server.communicateUsers(new JSONMessage(new ReceivedChat(sc.getMessage(), this.user.getName(), false)), this);
                } else{
                    // TODO private Message
                }
        }
    }

    /**
     * prints a message for specific user
     *
     * @param msg the message to be sent
     */
    public void sendMessage(JSONMessage msg) {
        String json = Multiplex.serialize(msg);
        logger.debug("Protocol sent:" + json);
        writer.println(json);
        writer.flush();
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
}
