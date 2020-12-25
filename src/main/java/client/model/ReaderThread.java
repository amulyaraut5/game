package client.model;

import utilities.JSONProtocol.JSONMessage;
import utilities.JSONProtocol.Multiplex;
import utilities.JSONProtocol.connection.HelloClient;
import utilities.JSONProtocol.connection.Welcome;
import utilities.Utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * It reads (for the client) the servers input constantly and prints it out on the console.
 *
 */
public class ReaderThread extends Thread {
    /**
     * client is the related ChatClient which starts an instance of ReaderThread.
     */
    private final Client client;
    /**
     * Logger to log information/warning
     */
    Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    /**
     * BufferedReader which is wrap around the InputStream of the socket.
     */
    private BufferedReader bReader;

    /**
     * Constructor of ReaderThread initializes the attributes socket and client
     * and creates a BufferedReader which is wrap upon the InputStream of the Socket.
     *
     * @param socket Socket connected to the server
     * @param client Instance of ChatClient which handles the connection and disconnection to the server
     */
    public ReaderThread(Socket socket, Client client) {
        logger.setLevel(Level.ALL);
        this.client = client;

        try {
            InputStream in = socket.getInputStream();
            bReader = new BufferedReader(new InputStreamReader(in));
        } catch (IOException ex) {
            if (!isInterrupted()) client.disconnect(ex);
        }
    }

    /**
     * run() method to start Thread and read input from the server and print it out on the console
     */
    @Override
    public void run() {

        while (!isInterrupted()) {
            try {
                String text = bReader.readLine();
                if (text == null) {
                    throw new IOException();
                }
                // After the reader object reads the serialized message from the socket it is then
                // deserialized and handled in handleMessage method.

                JSONMessage jsonMessage = Multiplex.deserialize(text);
                handleMessage(jsonMessage);
            } catch (IOException | ClassNotFoundException e) {
                if (!isInterrupted()) client.disconnect(e);
                break;
            }
        }
    }

    /**
     * Based on the messageType the various protocol are differentiated and Object class type
     * is downcasted to respective class.
     *
     * @param message
     * @throws ClassNotFoundException
     */
    private void handleMessage(JSONMessage message) throws ClassNotFoundException {

        Utilities.MessageType type = message.getMessageType();

        switch (type) {
            case HelloClient:
                HelloClient hc = (HelloClient) message.getMessageBody();
                logger.info("\n Received Protocol: " + type + "\n Protocol#: " + hc.getProtocol());
                client.connect(hc);
                break;
            case Welcome:
                Welcome wc = (Welcome) message.getMessageBody();
                String labelMessage = "\n Received Protocol: " + type.toString() + "\n ID: " + wc.getPlayerId();
                client.printMessage(labelMessage);
                logger.info(labelMessage);
                break;
            case Error:
                logger.info("error!");
                break;
            default:
                logger.info("Something went wrong");
                //TODO
        }
    }
}
