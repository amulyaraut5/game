package client.model;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * This class reads the input of the user and first gets the username and
 * then it reads messages until the user types bye
 *
 * @author sarah
 */
public class Writer {
    private final PrintWriter writer;
    private final Client client;
    //TODO
    private OutputStream output;

    public Writer(Socket socket, Client client) {
        this.client = client;

        try {
            output = socket.getOutputStream();
        } catch (IOException ex) {
            client.disconnect(ex);
        }
        writer = new PrintWriter(output, true);
    }

    /**
     * The Methods transfers given input from the client to the server.
     * @param userInput message to the server
     */
    public void sendUserInput(String userInput) {
        if (!userInput.isBlank()) {
            writer.println(userInput);
        }
        if (userInput.equals("bye")) {
            client.disconnect();
        }
    }
}
