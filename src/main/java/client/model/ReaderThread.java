package client.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.JSONProtocol.JSONMessage;
import utilities.JSONProtocol.Multiplex;
import utilities.JSONProtocol.body.Error;
import utilities.JSONProtocol.body.*;
import utilities.Utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * It reads (for the client) the servers input constantly and prints it out on the console.
 *
 * @author sarah,
 */
public class ReaderThread extends Thread {
    /**
     * Logger to log information/warning
     */
    private static final Logger logger = LogManager.getLogger();
    /**
     * client is the related ChatClient which starts an instance of ReaderThread.
     */
    private final Client client;
    /**
     * BufferedReader which is wrap around the InputStream of the socket.
     */
    private BufferedReader bReader;

    private int playerId;

    /**
     * Constructor of ReaderThread initializes the attributes socket and client
     * and creates a BufferedReader which is wrap upon the InputStream of the Socket.
     *
     * @param socket Socket connected to the server
     * @param client Instance of ChatClient which handles the connection and disconnection to the server
     */
    public ReaderThread(Socket socket, Client client) {
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
                logger.debug("Protocol received: " + text);
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

        Utilities.MessageType type = message.getType();

        switch (type) {
            case HelloClient:
                HelloClient hc = (HelloClient) message.getBody();
                //logger.info("\n Received Protocol: " + type + "\n Protocol#: " + hc.getProtocol());
                client.connect(hc);
                break;
            case Welcome:
                Welcome wc = (Welcome) message.getBody();
                playerId = wc.getPlayerId();
                String labelMessage = "\n Received Protocol: " + type.toString() + "\n ID: " + wc.getPlayerId();
                client.sendToMain(labelMessage, "loginController");
                //logger.info(labelMessage);
                break;
            case PlayerAdded:
                PlayerAdded pa = (PlayerAdded) message.getBody();
                logger.info("Player Added: " + pa.getId());
                if (pa.getId() != playerId)
                    client.sendToMain(pa.getName(), "playerAdded");
                break;
            case Error:
                Error error = (Error) message.getBody();
                logger.info("Error Message: " + error.getError());
                break;
            case PlayerStatus:
                PlayerStatus playerStatus = (PlayerStatus) message.getBody();
                //TODO extract player id
                //if playerStatus.
                logger.info("PlayerStatus: " + playerStatus.isReady());

                break;
            case ReceivedChat:
                ReceivedChat receivedChat = (ReceivedChat) message.getBody();
                logger.info(receivedChat.getMessage());
                client.sendToMain(receivedChat.getFrom() + ": " + receivedChat.getMessage(), "receivedChat");
            default:
                logger.info("Something went wrong");
                //TODO
        }
    }
}
