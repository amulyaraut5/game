package server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.Constants;
import utilities.JSONProtocol.JSONBody;
import utilities.JSONProtocol.JSONMessage;
import utilities.JSONProtocol.Multiplex;
import utilities.JSONProtocol.body.HelloClient;
import utilities.QueueMessage;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * Handles connection for each connected client,
 * therefore the server is able to handle multiple clients at the same time.
 *
 * @author vossa,
 */

public class UserThread extends Thread {
    private static final Logger logger = LogManager.getLogger();

    private final User user;
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
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
        } catch (IOException ex) {
            disconnect(ex);
        }
    }

    /**
     * The method runs a loop of reading messages from the user and sending them to all other users.
     */
    @Override
    public void run() {
        try {
            sendMessage(new HelloClient(Constants.PROTOCOL));

            while (!exit) {
                String jsonText = reader.readLine();
                if (jsonText == null) {
                    throw new IOException("Connection closed");
                }
                logger.debug("Protocol received: " + jsonText);

                JSONMessage msg = Multiplex.deserialize(jsonText);
                server.getMessageQueue().add(new QueueMessage(msg, user));
                synchronized (server) {
                    server.notify();
                }
            }
        } catch (IOException ex) {
            disconnect(ex);
        }
        disconnect();
    }

    /**
     * sends a message for specific user
     *
     * @param jsonBody the JSONBody of the message to sent
     */
    public void sendMessage(JSONBody jsonBody) {
        String json = Multiplex.serialize(JSONMessage.build(jsonBody));
        writer.println(json);
        writer.flush();
    }

    /**
     * The connection is closed and other users get notified that the user left.
     */
    protected void disconnect() {
        disconnect(null);
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
            if (ex != null) logger.warn(ex.getMessage());
            server.removeUser(user);
            logger.warn("Closed the connection with address:   " + socket.getRemoteSocketAddress());
            try {
                socket.close();
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
    }
}
