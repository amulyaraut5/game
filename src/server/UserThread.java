package server;

import java.io.*;
import java.net.Socket;

/**
 * Handles connection for each connected client,
 * therefore the server is able to handle multiple clients at the same time.
 *
 * @author vossa,
 */

public class UserThread extends Thread {
    private final Socket socket;
    private final ChatServer server;
    private PrintWriter userOut;
    private BufferedReader reader;

    private boolean exit = false;

    private static volatile User user; //Connected user, which data has to be filled in logIn()

    public UserThread(Socket socket, ChatServer server, User user) {
        this.socket = socket;
        this.server = server;
        this.user = user;

        this.user.setThread(this);

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
     * The user disconnects by typing "bye".
     */
    @Override
    public void run() {
        //before each method call it is checked if run() should be exited.
        if (!exit) logIn();
        if (!exit) welcome();

        String clientMessage = "";
        String serverMessage;
        try {
            while (!exit && !clientMessage.equals("bye")) {
                clientMessage = reader.readLine();
                serverMessage = "[" + user.getName() + "]: " + clientMessage;
                server.communicate(serverMessage, user);
            }
        } catch (IOException ex) {
            disconnect(ex);
        }
        if (!exit) disconnect();
    }

    /**
     * The user is asked to enter a name to log in.
     * If the name already exists in the list of assigned usernames, the user is asked to try again.
     * It also makes sure that the user enters something and not a empty String.
     *
     * @return the entered and accepted username
     */
    private void logIn() {
        sendMessage("Enter your username:");
        try {
            while (true) {
                String userName = reader.readLine();
                if (userName.isBlank()) {
                    sendMessage("You might not have entered a username. Please try again:");
                } else if (!server.isAvailable(userName)) {
                    sendMessage("This username is already taken. Please try a different username:");
                } else {
                    synchronized (user) {
                        //TODO synchronize change in user
                        user.setName(userName);
                    }
                    break;
                }
            }
        } catch (IOException ex) {
            disconnect(ex);
        }
        // while (true) {
        //TODO test if date is in correct dateformat
        //if yes: user.setLastDate(lastDate);
        // }
    }

    /**
     * Sends welcome message to the user and notifies all other users.
     */
    private void welcome() {
        sendMessage("Welcome " + user.getName() + "!");
        sendMessage("Type \"bye\" to leave the room.");
        server.communicate(user.getName() + " joined the room.", user);
    }

    /**
     * The connection is closed and other users get notified that the user left.
     */
    private void disconnect() {
        sendMessage("Bye " + user.getName());
        server.removeUser(user);
        server.communicate(user.getName() + " left the room.", user);
        System.out.println("Closed the connection with address:   " + socket.getRemoteSocketAddress());
        try {
            socket.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Method is called if an Exception occurs during connection.
     * The connection is tried to close and other users get notified that the user left.
     *
     * @param ex Exception which occurred
     */
    private void disconnect(Exception ex) {
        exit = true;
        System.err.println("Error in UserThread with address " + socket.getRemoteSocketAddress() + ": " + ex.getMessage());
        server.removeUser(user);
        server.communicate(user.getName() + " left the room.", user);
        System.out.println("Closed the connection with address:   " + socket.getRemoteSocketAddress());
        try {
            socket.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
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

