package client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ChatClient {

    private int port;
    private String hostname;
    private String userName;

    public ChatClient (String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    public void establishConnection () {
        try {
            Socket socket = new Socket(hostname, port);

            System.out.println("Connection to server successful");

            new ReaderThread(socket, this).start();
            new WriterThread(socket, this).start();

        } catch (UnknownHostException e) {
            System.out.println("Connection failed: Could not find Server" + e.getMessage());
        } catch (IOException e) {
            System.out.println("Connection failed: General I/O exception" + e.getMessage());
            e.printStackTrace();
        }
    }

    void setUserName (String userName) {
        this.userName = userName;
    }

    String getUserName () {
        return this.userName;
    }

    public static void main(String[] args) {
        if (args.length < 2) return;

        String hostname = args[0];
        int port = Integer.parseInt(args [1]);

        ChatClient client = new ChatClient(hostname, port);
        client.establishConnection();
    }

}
