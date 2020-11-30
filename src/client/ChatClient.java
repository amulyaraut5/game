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
public class ChatClient {
    /**
     * hostname of the server is saved here for the socket creation.
     */
    private final String hostname;
    /**
     * port of the server on the named host is saved here for the socket creation.
     */
    private final int port;
    /**
     * Stream socket which get connected to the specified port number on the named host of the server.
     */
    private Socket socket;
    /**
     * The readerThread reads the input of the user from given socket.
     */
    private ReaderThread readerThread;
    /**
     * The writerThread writes the console input of the user from given socket.
     */
    private WriterThread writerThread;

    /**
     * constructor of ChatClient to initialize the attributes hostname and port.
     *
     * @param hostname Hostname of the server.
     * @param port     Port of the server on the named host.
     */
    public ChatClient(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    /**
     * Main method for the client program. A new ChatClient is created
     * and an attempt is made to establish the connection.
     *
     * @param args unused arguments
     */
    public static void main(String[] args) {

        String hostname = "localhost";
        int port = 5444;

        ChatClient client = new ChatClient(hostname, port);
        client.establishConnection();
    }

    /**
     * This method establishes the connection between the server and the client using the assigned hostname and port.
     * If this was successful it creates a ReaderThread and a WriterThread which handle the communication onwards.
     */
    public void establishConnection() {
        try {
            socket = new Socket(hostname, port);

            readerThread = new ReaderThread(socket, this);
            writerThread = new WriterThread(socket, this);
            readerThread.start();
            writerThread.start();

            System.out.println("Connection to server successful.");

        } catch (UnknownHostException e) {
            System.out.println("Connection failed - IP-address of host could not be determined: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Connection failed - General I/O exception: " + e.getMessage());
        }
    }

    /**
     * methods ends the client program. The reader and writer threads get interrupted and the socket is closed.
     */
    public void disconnect() {
        readerThread.interrupt();
        writerThread.interrupt();
        try {
            socket.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Method should be called if a critical exception occurs in the client. (e.g. socket closed)
     * The reader and writer threads get interrupted and the socket is closed.
     *
     * @param ex The exception which occurred
     */
    public void disconnect(Exception ex) {
        readerThread.interrupt();
        writerThread.interrupt();
        System.out.println("The server is no longer reachable: " + ex.getMessage());
        try {
            socket.close();
            System.out.println("The connection with the server is closed.");
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        System.out.println("Type \"bye\" to exit.");
    }
}
