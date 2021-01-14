package server;

import game.gameObjects.tiles.Attribute;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.JSONProtocol.JSONBody;
import utilities.JSONProtocol.JSONMessage;
import utilities.JSONProtocol.Multiplex;
import utilities.JSONProtocol.body.HelloClient;
import utilities.Utilities;

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

    private final User user; //Connected user, which data has to be filled in logIn()
    private final Socket socket;
    private final Server server = Server.getInstance();
    private PrintWriter writer;
    private BufferedReader reader;
    private boolean exit = false;

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
    }

    /**
     * The method runs a loop of reading messages from the user and sending them to all other users.
     * The user disconnects by typing "bye".
     */
    @Override
    public void run() {
        try {
            sendMessage(new HelloClient(Utilities.PROTOCOL));

            while (!exit) {
                String text = reader.readLine();
                if (text == null) {
                    throw new IOException();
                }
                else {
                    //logger.debug("Protocol received: " + text);
                    JSONMessage msg = Multiplex.deserialize(text);
                    QueueMessage queueMessage = new QueueMessage(msg, this.user);
                    server.getMessageQueue().add(queueMessage); //TODO put?
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
        String json = Multiplex.serialize(JSONMessage.build(jsonBody));
        //logger.debug("Protocol sent: " + json);
        writer.println(json);
        writer.flush();
    }

    /**
     * The connection is closed and other users get notified that the user left.
     */
    void disconnect() {
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
