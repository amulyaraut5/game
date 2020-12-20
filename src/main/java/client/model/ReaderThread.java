package client.model;

import Utilities.JSONProtocol.JSONMessage;
import Utilities.JSONProtocol.Multiplex;
import Utilities.JSONProtocol.connection.HelloClient;
import Utilities.JSONProtocol.connection.Welcome;

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
 * @author simon
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
     * @param message
     * @throws ClassNotFoundException
     */
    private void handleMessage(JSONMessage message) throws ClassNotFoundException {

        String type = message.getMessageType();

        //Object messageBody = message.getMessageBody();

        switch (type) {
            case "HelloClient":
                logger.info("Received Protocol:");
                logger.info("Protocol: " + type);
                //System.out.println(messageBody);
                HelloClient hc = (HelloClient) message.getMessageBody();
                logger.info("Protocol: " + hc.getProtocol());
                client.connect(hc);
                break;
            case "Welcome":
                logger.info("Protocol: " + type);
                Welcome wc = (Welcome) message.getMessageBody();
                logger.info("ID: " + wc.getMessage() + wc.getId());
                break;

        }

    }
}
