package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * @author simon
 */
public class ReaderThread extends Thread {
    private final ChatClient client;
    private BufferedReader bReader;

    public ReaderThread(Socket socket, ChatClient client) {
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
                if (text != null && !text.isBlank()) client.handleServerMessage(text);
            } catch (IOException e) {
                if (!isInterrupted()) client.disconnect(e);
                break;
            }
        }
    }
}
