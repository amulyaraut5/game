package server;

import java.io.*;
import java.net.Socket;

public class UserThread extends Thread {
    private final Socket socket;
    private final ChatServer server;
    private PrintWriter userOut;
    private BufferedReader reader;

    private boolean exit = false;

    private String userName;

    public UserThread(Socket socket, ChatServer server) {
        this.socket = socket;
        this.server = server;

        try {
            InputStream input = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));
            OutputStream output = socket.getOutputStream();
            userOut = new PrintWriter(output, true);
        } catch (IOException ex) {
            disconnect(ex);
        }
    }

    /**
     * The method runs a loop of reading messages from the user and sending them to all other users.
     * The user disconnects by typing "bye"
     */
    @Override
    public void run() {
        try {
            if (!exit) {
                logIn();
            }
            if (!exit) {
                welcome();
            }

            String clientMessage = "";
            String serverMessage;
            while (!exit && !clientMessage.equals("bye")) {
                clientMessage = reader.readLine();
                serverMessage = "[" + userName + "]: " + clientMessage;
                server.communicate(serverMessage, this);
            }
            if (!exit) {
                disconnect();
            }
        } catch (IOException ex) {
            disconnect(ex);
        }
    }

    /**
     * The user is asked to enter a name to log in.
     * If the name already exists in the list of assigned usernames, the user is asked to try again.
     *
     * @return the entered and accepted username
     */
    private String logIn() {
        sendMessage("Enter your username:");
        try {
            userName = reader.readLine();
            while (!server.checkAvailability(userName)) {
                sendMessage("This username is already taken or you might not have entered the username. Please try again.");
                userName = reader.readLine();
            }
        } catch (IOException ex) {
            disconnect(ex);
        }
        return userName;
    }

    /**
     * The connection is closed and other users get notified.
     */
    private void disconnect() {
        sendMessage("Bye " + userName);
        server.removeUser(userName, this);
        server.communicate(userName + " left the room.", this);
        System.out.println("Closed the connection with address: " + socket.getRemoteSocketAddress());
        try {
            socket.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Method is called if an Exception occurs during connection.
     * The connection is tried to close and other users get notified.
     *
     * @param ex Exception which occurred
     */
    private void disconnect(Exception ex) {
        exit = true;
        System.err.println("Error in UserThread: " + ex.getMessage());
        server.removeUser(userName, this);
        server.communicate(userName + " left the room.", this);
        System.out.println("Closed the connection with address: " + socket.getRemoteSocketAddress());
        try {
            socket.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Sends welcome message to the user and notifies all other users.
     */
    private void welcome() {
        server.addUserName(userName);
        sendMessage("Welcome " + userName + "!");
        server.communicate(userName + " joined the room.", this);
    }

    /**
     * prints a message for specific user
     *
     * @param message the message to be sent
     */
    public void sendMessage(String message) {
        userOut.println(message);
    }
}

