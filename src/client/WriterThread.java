package client;

import java.io.*;
import java.net.Socket;

public class WriterThread extends Thread {

    private final PrintWriter writer;
    private OutputStream output;
    private final Socket socket;
    private final ChatClient client;
    private BufferedReader bReader;

    public WriterThread(Socket socket, ChatClient client) {
        this.socket = socket;
        this.client = client;

        try {
            output = socket.getOutputStream();
        } catch (IOException ex) {
            System.out.println("Error getting output stream: " + ex.getMessage());
            ex.printStackTrace();
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

        manageUserName();

        manageUserInput();

        try {
            socket.close();
        } catch (IOException ex) {
            System.out.println("Error writing to server: " + ex.getMessage());
        }
    }

    /**
     * the user writes his userName in the console, manageUserName() reads it and
     * transfers it to the server
     */
    private void manageUserName() {
        String userName = "userName";
        try {
            userName = bReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
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
                e.printStackTrace();
            }
            writer.println(inputUser);

        } while (!inputUser.equals("bye"));

    }
}


