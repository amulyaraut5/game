package client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ChatClient {

    private int port;
    private String ip;
    private String userName;

    public ChatClient (String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public void establishConnection () {
        try {
            Socket socket = new Socket(ip, port);

            System.out.println("Connection to server successful");

            new ReaderThread(socket, this).start();
            new WriterThread(socket, this).start();

        } catch (IOException e) {
            System.out.println("Connection failed: General I/O exception" + e.getMessage());
            e.printStackTrace();
        } catch (UnknownHostException e) {
            System.out.println("Connection failed: Could not find Server" + e.getMessage());
            e.printStackTrace();
        }
    }
}
