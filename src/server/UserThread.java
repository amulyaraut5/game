package server;

import java.io.*;
import java.net.Socket;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

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

    private String userName = "Unnamed user";
    private String datePuffer = "Datepuffer";
    private LocalDate lastDate;
    public static DateTimeFormatter formatter  = DateTimeFormatter.ofPattern("dd MM yy");

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
     * The user disconnects by typing "bye".
     */
    @Override
    public void run() {
        //before each method call it is checked if run() should be exited.
        if (!exit) logInName();

        if (!exit) logInDate();


        if (!exit) welcome();

        String clientMessage = "";
        String serverMessage;
        try {
            while (!exit && !clientMessage.equals("bye")) {
                clientMessage = reader.readLine();
                serverMessage = "[" + userName + "]: " + clientMessage;
                server.communicate(serverMessage, this);
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
    private String logInName() {
        sendMessage("Enter your username:");
        try {
            while (true) {
                userName = reader.readLine();
                if (userName.isBlank()) {
                    sendMessage("You might not have entered a username. Please try again:");
                } else if (!server.checkAvailability(userName)) {
                    sendMessage("This username is already taken. Please try a different username:");
                } else {
                    return userName;
                }
            }
        } catch (IOException ex) {
            disconnect(ex);
        }
        return userName;
    }
    private LocalDate logInDate(){

        try {
            sendMessage ("I am curious. When was the last time you were on a date? (dd MM yy)");

            datePuffer = reader.readLine();

            while(/*!isValid(datePuffer) ||*/ !datePuffer.matches("^\\d?\\d \\d{2} \\d{2}$")) {
                sendMessage ("This date is not in the correct format. Please try again. ");
                datePuffer = reader.readLine();
            }
            return lastDate = LocalDate.parse(datePuffer, formatter);

        } catch (IOException ex) {
            sendMessage("nö");
            disconnect(ex);
        }
        return lastDate;
        }
    //TODO: no valid dates doesnt count
    public static boolean isValid(String date) {
        boolean valid = false;
        try {
            // ResolverStyle.STRICT for 30, 31 days checking, and also leap year.
            LocalDate.parse(date, formatter.withResolverStyle(ResolverStyle.STRICT));
            valid = true;
        } catch (DateTimeParseException e) {
            e.printStackTrace();
            valid = false;
        }
        return valid;
    }
    /**
     * Sends welcome message to the user and notifies all other users.
     */
    private void welcome() {
        server.addUserName(userName);
        server.addDate(lastDate);
        sendMessage("Welcome " + userName + "!");
        sendMessage("Type \"bye\" to leave the room.");
        server.communicate(userName + " joined the room.", this);
    }

    /**
     * The connection is closed and other users get notified that the user left.
     */
    private void disconnect() {
        sendMessage("Bye " + userName);
        server.removeUser(userName, this);
        server.communicate(userName + " left the room.", this);
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
        server.removeUser(userName, this);
        server.communicate(userName + " left the room.", this);
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

