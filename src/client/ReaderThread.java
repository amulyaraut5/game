package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * It rea
 * @author simon
 */
public class ReaderThread extends Thread {
    /** client is the related ChatClient which starts an instance of ReaderThread*/

    private final ChatClient client;
    /** the socket on which the Reader Threads listens*/
    private final Socket socket;
    /**BufferedReader to read input*/
    private BufferedReader bReader;

    /**
     * constructor of ReaderThread to initialize the attributes socket and the client
     * and to create an InputStream and to initialize the BufferedReader
     * @param socket
     * @param client
     */
    public ReaderThread(Socket socket, ChatClient client) {
        this.socket = socket;
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
                System.out.println(text);
            } catch (IOException e) {
                if (!isInterrupted()) client.disconnect(e);
                break;
            }
        }
    }
}
