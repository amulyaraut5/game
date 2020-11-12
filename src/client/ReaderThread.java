package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;

public class ReaderThread extends Thread{
    private ChatClient client;

    private Socket socket;
    private BufferedReader bReader;

    public ReaderThread(Socket socket,ChatClient client) {
        this.socket = socket;
        this.client = client;

        try {
            InputStream in = socket.getInputStream();
            bReader = new BufferedReader(new InputStreamReader(in));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true){
            try {
                String text = bReader.readLine();
                System.out.println(text + "\r\n");
            } catch (SocketException e){
                e.printStackTrace();
                break;
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
