package client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ChatClient {

    private int port;
    private String hostname;
    private String userName;

    public ChatClient(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    public static void main(String[] args) {

        String hostname = "localhost";
        int port = 4444;

        ChatClient client = new ChatClient(hostname, port);
        client.establishConnection();
    }

    /**
     * This method establishes the connection between the server and the client using the assigned hostname and port.
     * If this was successful it creates a ReadThread and a WriterThread which handle the communication.
     */

    public void establishConnection() {
        try {
            Socket socket = new Socket(hostname, port);

            System.out.println("Connection to server successful");

            new ReaderThread(socket, this).start();
            new WriterThread(socket, this).start();

        } catch (UnknownHostException e) {
            System.out.println("Connection failed: Could not find Server" + e.getMessage());
        } catch (IOException e) {
            System.out.println("Connection failed: General I/O exception" + e.getMessage());
        }
    }

    void setUserName(String userName) {
        this.userName = userName;
    }

}
