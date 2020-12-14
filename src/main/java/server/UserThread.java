package server;

import client.model.JSONMessage;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

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
    /**
     * Logger to log information/warning
     */
    private Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private PrintWriter userOut;
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
            userOut = new PrintWriter(output, true);
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
        Gson gson = new Gson();

        String serverMessage;
        try {
            while (!exit) {
                String text = reader.readLine();
                if (text == null) {
                    throw new IOException();
                }
                JSONMessage msg = gson.fromJson(text, JSONMessage.class);

                if (msg.getType().equals("checkName")) {
                    logger.info(msg.getBody().toString());
                    logIn((String) msg.getBody());
                }
                if (msg.getType().equals("userMessage")) {
                    serverMessage = "[" + user + "]: " + msg.getBody();
                    server.communicate(serverMessage, user);
                }

            }

        } catch (IOException ex) {
            disconnect(ex);
        }
        if (!exit) disconnect();
    }

    /**
     * prints a message for specific user
     *
     * @param message the message to be sent
     */
    public void sendMessage(String type, String message) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", type);
        jsonObject.addProperty("body", message);
        userOut.println(jsonObject.toString());
    }


    private void logIn(String userName) {
        if (!server.isAvailable(userName))
            sendMessage("userNameTaken", "true");
        else {
            sendMessage("userNameTaken", "false");
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
