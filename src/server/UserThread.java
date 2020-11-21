package server;

import java.io.*;
import java.net.Socket;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handles connection for each connected client,
 * therefore the server is able to handle multiple clients at the same time.
 *
 * @author vossa,
 */

public class UserThread extends Thread {
    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MM yy");
    private final User user; //Connected user, which data has to be filled in logIn()
    private final Socket socket;
    private final ChatServer server;
    private PrintWriter userOut;
    private BufferedReader reader;
    private boolean exit = false;

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
        if (!exit) logInName();
        if (!exit) logInDate();
        if (!exit) welcome();

        String clientMessage = "";
        String serverMessage;
        try {
            while (!exit && !clientMessage.equals("bye")) {
                clientMessage = reader.readLine();

                Pattern gamePattern = Pattern.compile("^#+");
                Matcher matcher = gamePattern.matcher(clientMessage);

                Pattern directPattern = Pattern.compile("^@+");
                Matcher directMatcher = directPattern.matcher(clientMessage);

                if (matcher.lookingAt()) {
                    server.communicateGame(clientMessage, user);
                } else if (directMatcher.lookingAt()) {
                    server.communicateDirect(clientMessage, user);
                } else {
                    serverMessage = "[" + user.getName() + "]: " + clientMessage;
                    server.communicate(serverMessage, user);
                }
            }
        } catch (IOException ex) {
            disconnect(ex);
        }
        if (!exit) disconnect();
    }

    /**
     * prints a message for specific user
     *
     * @param message the message to be sent
     */
    public void sendMessage(String message) {
        userOut.println(message);
    }

    /**
     * The user is asked to enter a name to log in.
     * If the name already exists in the list of assigned usernames, the user is asked to try again.
     * It also makes sure that the user enters something and not a empty String.
     */
    private void logInName() {
        sendMessage("Enter your username:");
        try {
            while (true) {
                String userName = reader.readLine();
                if (userName.isBlank()) {
                    sendMessage("You might not have entered a username. Please try again:");
                } else if (!server.isAvailable(userName)) {
                    sendMessage("This username is already taken. Please try a different username:");
                } else {
                    user.setName(userName);
                    break;
                }
            }
        } catch (IOException ex) {
            disconnect(ex);
        }
    }
    /**
     * The user is asked to enter the date where he last dated.
     * If the date is not valid he has to do it again.
     */
    private void logInDate() {
        String datePuffer;
        //TODO if exception is thrown because date is not valid, the user should be asked to try again
        try {
            sendMessage("I am curious. When was the last time you were on a date? (dd mm yy)");
            datePuffer = reader.readLine();

            while (turnIntoDate(datePuffer) == null || !datePuffer.matches("^\\d?\\d \\d{2} \\d{2}$")) {
                sendMessage("This date is not in the correct format. Please try again. ");
                datePuffer = reader.readLine();
            }
            user.setLastDate(LocalDate.parse(datePuffer, formatter));
        } catch (IOException ex) {
            disconnect(ex);
        }
    }

    /**
     * Turns a String into a date or null
     * @param date as String
     * @return null if String is not a valid date or the date
     */
    public static LocalDate turnIntoDate (String date){
        try {
            return LocalDate.parse(date, formatter);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    /**
     * Sends welcome message to the user and notifies all other users.
     */
    private void welcome() {
        sendMessage("Thank you! Welcome " + user.getName() + "!");
        sendMessage("Type: \"bye\" to leave the room.");
        sendMessage("      \"#help\" to list all commands.");
        sendMessage("      \"#create\" to play the LoveLetter game.");
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
}

