package client.model;

import Utilities.JSONProtocol.JSONMessage;
import Utilities.JSONProtocol.connection.HelloClient;
import com.google.gson.Gson;

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
        Gson gson = new Gson();
        while (!isInterrupted()) {
            try {
                String text = bReader.readLine();
                if (text == null) {
                    throw new IOException();
                }
                JSONMessage jsonMessage = JSONMessage.deserialize(text);

                handleMessage(jsonMessage);
            } catch (IOException e) {
                if (!isInterrupted()) client.disconnect(e);
                break;
            }
        }
    }

    private void handleMessage(JSONMessage msg) throws IOException {
        String type = msg.getType();

        switch (type) {
            case "HelloClient":

                System.out.println("Received Protocol:");
                HelloClient.MessageBody messageBody = new HelloClient.MessageBody();
                System.out.println(msg.getType());
                System.out.println("Protocol: "+ messageBody.getProtocol());

                break;
        }       // show message in chatbox instead of console
        /*
        String type = msg.getType().toString();
        String test = msg.serialize();

        switch (type) {
            case "serverMessage":
                logger.info("serverMessage" + " " + test);//client.chatMessage((String) msg.getBody());
            case "chatMessage":
                logger.info("chatMessage" + " " + test);//client.chatMessage((String) msg.getBody());
            case "userNameTaken":
                logger.info("userNameTaken" + " " + test);//client.callServerResponse(msg.getBody());
        }

         */
    }
}
