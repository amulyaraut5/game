package client;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * This class reads the input of the user, first gets the username and
 * then it reads messages until the user types bye.
 *
 * @author sarah
 */
public class WriterThread extends Thread {
    /** client is the related ChatClient which starts an instance of ReaderThread.*/
    private final ChatClient client;
    /** the printWriter which writes messages onto the socket connected to the server.*/
    private PrintWriter writer;
    /** BufferedReader to read input.*/
    private BufferedReader bReader;

    /**
     * Constructor of WriterThread initializes the attributes socket and client
     * and creates a BufferedReader for the console input of System.in
     * and creates a PrintWriter which is wrap upon the OutputStream of the Socket.
     *
     * @param socket Socket connected to the server
     * @param client Instance of ChatClient which handles the connection and disconnection to the server
     */
    public WriterThread(Socket socket, ChatClient client) {
        this.client = client;

        try {
            writer = new PrintWriter(socket.getOutputStream(), true);
            bReader = new BufferedReader(new InputStreamReader(System.in));
        } catch (IOException ex) {
            if (!isInterrupted()) client.disconnect(ex);
        }
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
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("type", "usermessage");
                jsonObject.addProperty("messagebody", userInput);
                writer.println(jsonObject.toString());
            }
        }


    }
}
