package client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * This class creates the socket for the client.
 * It handles the connection and disconnection to the server.
 * In this process it creates or interrupts the WriterThread and ReaderThread.
 *
 * @author janau
 */

public class ChatClient extends Thread{
    private final int port;
    private final String hostname;
    private String userName;
    private Socket socket;
    public ReaderThread readerThread;
    public Writer writer;

    public ChatClient(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }



    /**
     * This method establishes the connection between the server and the client using the assigned hostname and port.
     * If this was successful it creates a ReaderThread and a WriterThread which handle the communication onwards.
     */

    public void establishConnection() {
        try {
            socket = new Socket(hostname, port);

            readerThread = new ReaderThread(socket, this);
            writer = new Writer(socket, this);
            readerThread.start();

            System.out.println("Connection to server successful.");

        } catch (UnknownHostException e) {
            System.out.println("Connection failed - IP-address of host could not be determined: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Connection failed - General I/O exception: " + e.getMessage());
        }
    }

    /**
     *
     */
    public void disconnect() {
        readerThread.interrupt();
        try {
            socket.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     *
     * @param ex
     */
    public void disconnect(Exception ex) {
        readerThread.interrupt();
        System.out.println("The server is no longer reachable: " + ex.getMessage());
        try {
            socket.close();
            System.out.println("The connection with the server is closed.");
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        System.out.println("Type \"bye\" to exit.");
    }

    /**
     *
     * @param userName
     */
    void setUserName(String userName) {
        this.userName = userName;
    }

}
