package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 */
public class ReaderThread extends Thread{
    private ChatClient client;

    private Socket socket;
    private BufferedReader reader;

    public ReaderThread(Socket socket,ChatClient client) {
        this.socket = socket;
        this.client = client;
    }

    /**
     *
     */
    @Override
    public void run() {
        while (true){
            try {
                InputStream inStream = socket.getInputStream();
                Scanner inScanner = new Scanner(inStream);
                while (inScanner.hasNext()){
                    String input = inScanner.next();
                    System.out.println(input);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                String text = reader.readLine();
                System.out.println(text);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
