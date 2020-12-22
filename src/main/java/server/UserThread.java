package server;

import Utilities.JSONProtocol.JSONMessage;
import Utilities.JSONProtocol.Multiplex;
import Utilities.JSONProtocol.connection.HelloClient;
import Utilities.JSONProtocol.connection.HelloServer;
import Utilities.JSONProtocol.connection.Welcome;
import Utilities.Utilities.MessageType;
import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles connection for each connected client,
 * therefore the server is able to handle multiple clients at the same time.
 *
 * @author vossa,
 */

public class UserThread extends Thread {

    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy");
    private final User user; //Connected user, which data has to be filled in logIn()
    private final Socket socket;
    private final Server server;
    private final double protocol = 0.1;
    /**
     * Logger to log information/warning
     */
    private Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private PrintWriter writer;
    private BufferedReader reader;
    private boolean exit = false;

    public UserThread(Socket socket, Server server, User user) {
        logger.setLevel(Level.ALL);

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
     * Turns a String into a date or null
     *
     * @param date as String
     * @return null if String is not a valid date or the date
     */
    public static LocalDate turnIntoDate(String date) {
        try {
            return LocalDate.parse(date, formatter);
        } catch (DateTimeParseException e) {
            return null;
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
            logger.info("Sent Protocol:");
            JSONMessage jsonMessage = new JSONMessage(MessageType.HelloClient, new HelloClient(protocol));
            logger.info(Multiplex.serialize(jsonMessage));
            writer.println(Multiplex.serialize(jsonMessage));
            writer.flush();
            //<------------------------->

            while (!exit) {
                String text = reader.readLine();
                if (text == null) {
                    throw new IOException();
                }
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
     * is downcasted to respective class.
     *
     * @param message
     * @throws ClassNotFoundException
     */

    private void handleMessage(JSONMessage message) throws IOException {
        MessageType type = message.getMessageType();

        switch (type) {
            case HelloServer:
                // The messageBody which is Object is then downcasted to HelloServer class
                HelloServer hs = (HelloServer) message.getMessageBody();
                logger.info("\n Received Protocol: " + type + "\n Group: " + hs.getGroup() + "\n Protocol: " + hs.getProtocol() + "\n isAI: " + hs.isAI());
                logger.info(String.valueOf(hs.getProtocol() == protocol));
                if (!(hs.getProtocol() == protocol)) {
                    //TODO send Error and disconnect the client
                    JSONMessage jsonMessage = new JSONMessage(MessageType.Error, new Utilities.JSONProtocol.specialMessages.Error("Ups! That did not work. Try to adjust something."));
                    logger.info(Multiplex.serialize(jsonMessage));
                    logger.info("Protocols don´t match");
                    writer.println(Multiplex.serialize(jsonMessage));
                    writer.flush();
                    //disconnect();
                    break;
                } else {
                    int playerID = server.getNewID();
                    JSONMessage jsonMessage = new JSONMessage(MessageType.Welcome, new Welcome(playerID));
                    System.out.println(jsonMessage);
                    logger.info(Multiplex.serialize(jsonMessage));
                    writer.println(Multiplex.serialize(jsonMessage));
                    writer.flush();
                    break;
                }
        }

    }

    /**
     * prints a message for specific user
     *
     * @param msg the message to be sent
     */
    public void sendMessage(JSONMessage msg) {
        Gson gson = new Gson();
        writer.println(gson.toJson(msg));
    }


    private void logIn(String userName) {
        if (!server.isAvailable(userName)) {

            //TODO sendMessage(new JSONMessage("userNameTaken", "true"));
            //else {
            //sendMessage(new JSONMessage("userNameTaken", "false"));
            user.setName(userName);
            welcome();
        }

    }


    /**
     * Sends welcome message to the user and notifies all other users.
     */
    private void welcome() {
        server.communicate(user + " joined the room.", user);
    }

    /**
     * The connection is closed and other users get notified that the user left.
     */
    private void disconnect() {
        //sendMessage("Bye " + user);
        server.removeUser(user);
        server.communicate(user + " left the room.", user);
        logger.warning("Closed the connection with address:   " + socket.getRemoteSocketAddress());
        try {
            socket.close();
        } catch (IOException e) {
            logger.severe(e.getMessage());
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
        logger.severe("Error in UserThread with address " + socket.getRemoteSocketAddress() + ": " + ex.getMessage());
        server.removeUser(user);
        server.communicate(user + " left the room.", user);
        logger.severe("Closed the connection with address:   " + socket.getRemoteSocketAddress());
        try {
            socket.close();
        } catch (IOException e) {
            logger.severe(e.getMessage());
        }
    }
}
