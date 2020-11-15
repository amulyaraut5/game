package client;

import java.io.*;
import java.net.Socket;

public class WriterThread extends Thread {

    private PrintWriter writer;
    private OutputStream output;
    private Socket socket;
    private ChatClient client;
    private BufferedReader bReader;

    public WriterThread(Socket socket, ChatClient client) {
        this.socket = socket;
        this.client = client;

        try {
            output = socket.getOutputStream();
            writer = new PrintWriter(output, true);
        } catch (IOException ex) {
            System.out.println("Error getting output stream: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     *
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
     * It reads the userName the user writes in the console and
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


