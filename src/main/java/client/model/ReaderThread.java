package client.model;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


import java.io.*;
import java.net.Socket;

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
                JsonElement jelement = JsonParser.parseReader(new StringReader(text));
                JsonObject json = jelement.getAsJsonObject();
                JSONMessage jsonMessage = new JSONMessage(json.get("type").toString(), json.get("messagebody").toString());
                if(text==null){
                    throw new IOException();
                }
                if (jsonMessage.getMessageType().equals("\"serverMessage\"")){
                    String messagebody = jsonMessage.getMessageBody().toString();
                    System.out.println(messagebody);
                }
            } catch (IOException e) {
                if (!isInterrupted()) client.disconnect(e);
                break;
            }
        }
    }
}
