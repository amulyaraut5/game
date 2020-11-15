package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 *
 */
public class ReaderThread extends Thread {
    private final ChatClient client;
    private final Socket socket;
    private BufferedReader bReader;

    public ReaderThread(Socket socket, ChatClient client) {
        this.socket = socket;
        this.client = client;

        try {
            InputStream in = socket.getInputStream();
            bReader = new BufferedReader(new InputStreamReader(in));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     */
    @Override
    public void run() {
        while (true) {
            try {
                String text = bReader.readLine();
                System.out.println(text);
                //throw new SocketException();
            } catch (IOException e) {
                System.err.println("The server is no longer reachable: " + e.getMessage());
                try {
                    socket.close();
                    System.out.println("The connection with the server is closed.");
                } catch (IOException ioException) {
                    System.err.println(e.getMessage());
                }
                break;
            }
        }
    }
}
