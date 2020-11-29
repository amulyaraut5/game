package client;

import java.io.*;
import java.net.Socket;

/**
 * This class reads the input of the user, first gets the username and
 * then it reads messages until the user types bye
 *
 * @author sarah
 */
public class WriterThread extends Thread {
    /**
     * the printWriter who delivers a method to the reader of the server
     */
    private final PrintWriter writer;
    /**
     * client is the related ChatClient which starts an instance of ReaderThread
     */
    private final ChatClient client;
    /**
     * output is the OutputStream of the socket
     */
    private OutputStream output;
    /**
     * BufferedReader to read input
     */
    private BufferedReader bReader;

    /**
     * constructor to initialize the client, the output, the writer and the bReader
     *
     * @param socket
     * @param client
     */
    public WriterThread(Socket socket, ChatClient client) {
        this.client = client;

        try {
            output = socket.getOutputStream();
        } catch (IOException ex) {
            if (!isInterrupted()) client.disconnect(ex);
        }
        writer = new PrintWriter(output, true);
        bReader = new BufferedReader(new InputStreamReader(System.in));
    }

    /**
     * This method manages reading what the user is typing in its console. Starting with the name, the messages and
     * if the user is typing "bye" the socket will get closed
     */
    @Override
    public void run() {
        if (!isInterrupted()) manageUserInput();
        if (!isInterrupted()) client.disconnect();
    }

    /**
     * It reads input in the console and transfers it to the server
     * as long if the user doesn't write "bye"
     */
    private void manageUserInput() {
        String userInput = "";
        while (!isInterrupted() && !userInput.equals("bye")) {
            try {
                userInput = bReader.readLine();
            } catch (IOException ex) {
                if (!isInterrupted()) client.disconnect(ex);
            }
            if (!userInput.isBlank()) {
                writer.println(userInput);
            }
        }
    }
}


