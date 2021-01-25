package client.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.JSONProtocol.JSONMessage;
import utilities.JSONProtocol.Multiplex;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Timer;

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
    private BufferedReader reader;

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
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        } catch (IOException ex) {
            if (!isInterrupted()) client.disconnect(ex);
        }
    }

    /**
     * run() method to start Thread and read input from the server
     */
    @Override
    public void run() {
        if(client.isAI()) {
            Timer t = new java.util.Timer();
            t.schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            client.getAiCoordinator().choosePlayerValues();
                            t.cancel();
                        }
                    },
                    1000
            );
        }
        while (!isInterrupted()) {
            try {
                String jsonText = reader.readLine();
                if (jsonText == null) {
                    throw new IOException("Connection closed");
                }
                logger.debug("Protocol received: " + jsonText);

                JSONMessage msg = Multiplex.deserialize(jsonText);
                if (client.isAI()) client.getAiCoordinator().handleMessage(msg);
                else client.handleMessage(msg);

            } catch (IOException e) {
                if (!isInterrupted()) client.disconnect(e);
                break;
            }
        }
    }


}
