package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class WriterThread extends Thread {

    private PrintWriter writer;
    private Socket socket;
    private ChatClient client;
    private BufferedReader bReader;

    public WriterThread(Socket socket, ChatClient client) {
        this.socket = socket;
        this.client = client;

        try {
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);
        } catch (IOException ex) {
            System.out.println("Error getting output stream: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    @Override
    public void run() {
        Scanner scan = new Scanner(System.in);

        System.out.println("Enter your name");
        String userName = scan.nextLine();
        client.setUserName(userName);
        writer.println(userName);

        String inputUser;

        do {
            //System.out.print("You: ");
            inputUser = scan.next();
            writer.println(inputUser);

        } while (!inputUser.equals("bye"));

        try {
            socket.close();

        } catch (IOException ex) {

            System.out.println("Error writing to server: " + ex.getMessage());
        }

    }
}


