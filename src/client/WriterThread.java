package client;

import java.io.PrintWriter;
import java.net.Socket;

public class WriterThread {

    private PrintWriter writer;
    private Socket socket;
    private ChatClient client;

    public WriterThread(Socket socket, ChatClient client) {
            this.socket = socket;
            this.client = client;
    }
    public void run(){

    }
    public static void main (String [] args){
        System.out.println("Test");
    }
}
