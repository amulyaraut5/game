package client.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.JSONProtocol.JSONMessage;
import utilities.JSONProtocol.Multiplex;

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

    /**
     * Constructor of ReaderThread initializes the attributes socket and client
     * and creates a BufferedReader which is wrap upon the InputStream of the Socket.
     *
     * @param socket Socket connected to the server
     * @param client Instance of ChatClient which handles the connection and disconnection to the server
     */
    public ReaderThread(Socket socket, Client client) {
        this.client = client;
        setName("ReaderThread");
        setDaemon(true);

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
                    throw new IOException("Connection closed");
                }
                logger.debug("Protocol received: " + text);
                // After the reader object reads the serialized message from the socket it is then
                // deserialized and handled in handleMessage method.

                JSONMessage jsonMessage = Multiplex.deserialize(text);
                client.getBlockingQueue().add(jsonMessage);
            } catch (IOException e) {
                if (!isInterrupted()) client.disconnect(e);
                break;
            }
        }
    }


}
