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
        } catch (IOException ex) {
            if (!isInterrupted()) client.disconnect(ex);
        }
    }

    /**
     *
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
