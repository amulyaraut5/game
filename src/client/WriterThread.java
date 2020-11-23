package client;

import java.io.*;
import java.net.Socket;

/**
 * This class reads the input of the user and first gets the username and
 * then it reads messages until the user types bye
 *
 * @author sarah
 */
public class WriterThread extends Thread {

    private final PrintWriter writer;
    private final Socket socket;
    private final ChatClient client;
    private OutputStream output;
    private BufferedReader bReader;

    public WriterThread(Socket socket, ChatClient client) {
        this.socket = socket;
        this.client = client;

        try {
            output = socket.getOutputStream();
        } catch (IOException ex) {
            if (!isInterrupted()) client.disconnect(ex);
        }
        writer = new PrintWriter(output, true);
    }

    /**
     * This method manages reading what the user is typing in its console. Starting with the name, the messages and
     * if the user is typing "bye" the socket will get closed
     */
    @Override
    public void run() {
        InputStream in = System.in;
        bReader = new BufferedReader(new InputStreamReader(in));

        if (!isInterrupted()) manageUserName();
        if (!isInterrupted()) manageUserInput();

        if (!isInterrupted()) client.disconnect();
    }

    /**
     * the user writes his userName in the console, manageUserName() reads it and
     * transfers it to the server
     */
    private void manageUserName() {
        String userName = "";
        try {
            userName = bReader.readLine();
        } catch (IOException e) {
            if (!isInterrupted()) client.disconnect(e);
        }
        client.setUserName(userName);
        writer.println(userName);
    }

    /**
     * It reads input in the console and transfers it to the server
     * as long if the user doesn't write "bye"
     */
    private void manageUserInput() {
        String inputUser = "";
        do {
            try {
                inputUser = bReader.readLine();
            } catch (IOException e) {
                if (!isInterrupted()) client.disconnect(e);
            }
            writer.println(inputUser);
        } while (!isInterrupted() && !inputUser.equals("bye"));
    }
}


