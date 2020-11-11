package client;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class WriterThread {

    private PrintWriter writer;
    private Socket socket;
    private ChatClient client;

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

    public void start() {
        Scanner scan = new Scanner(System.in);

        System.out.println("Enter your name");
        String userName = scan.nextLine();
        client.setUserName(userName);
        writer.println(userName);

        String text;

        do {
            //System.out.print("You: ");
            text = scan.next();
            writer.println(text);

        } while (!text.equals("bye"));

    }
}


