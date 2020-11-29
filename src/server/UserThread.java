package server;

import java.io.*;
import java.net.Socket;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handles connection for each connected client.
 * Each UserThread is connected to one client over a specific socket.
 * Therefore the server is able to handle multiple clients at the same time.
 *
 * @author vossa,
 */
public class UserThread extends Thread {
    /** Formatter for printing and parsing date-times, initialised with the pattern dd.mm.yyyy. */
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    /** The connected user. */
    private final User user;
    /** The socket on which the UserThread interacts. */
    private final Socket socket;
    /** Server is the ChatServer which starts an instance of UserThread. */
    private final ChatServer server;
    /** The PrintWriter, which writes the user output. */
    private PrintWriter userOut;
    /** BufferedReader to read input. */
    private BufferedReader reader;
    /** Shows whether the user has left the room (when an exception has occurred during the connection). (false = not exited)*/
    private boolean exit = false;

    /**
     * Constructor of UserThread to assign the socket, the server and the user and to set this thread for the user.
     * To create an InputStream and OutputStream and to initialize the BufferedReader and the PrintWriter.
     * @param socket
     * @param server
     * @param user
     */
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
     * Turns a String into a date or null.
     *
     * @param date as String
     * @return null if String is not a valid date or the date
     */
    public static LocalDate turnIntoDate(String date) {
        try {
            return LocalDate.parse(date, formatter);
        } catch (DateTimeParseException e) {
            return null;
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
        //if (!exit) logInDate(); //TODO remove comment-out before submission on monday
        if (!exit) welcome();

        String clientMessage = "";
        String serverMessage;
        try {
            while (!exit && !clientMessage.equals("bye")) {
                clientMessage = reader.readLine();

                Pattern gamePattern = Pattern.compile("^#+");
                Matcher gameMatcher = gamePattern.matcher(clientMessage);

                Pattern directPattern = Pattern.compile("^@+");
                Matcher directMatcher = directPattern.matcher(clientMessage);

                if (gameMatcher.lookingAt()) {
                    server.communicateGame(clientMessage, user);
                } else if (directMatcher.lookingAt()) {
                    if (!server.communicateDirect(clientMessage, user)) {
                        user.message("Your request does not correspond to the required format. Please try again. @<name> <message>");
                    }
                } else {
                    serverMessage = "[" + user + "]: " + clientMessage;
                    server.communicate(serverMessage, user);
                }
            }
        } catch (IOException ex) {
            disconnect(ex);
        }
        if (!exit) disconnect();
    }

    /**
     * Prints a message for specific user.
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
                if (userName.contains(" ")) sendMessage("Spaces are not allowed in username. Please try again:");
                else if (!server.isAvailable(userName))
                    sendMessage("This username is already taken. Please try a different username:");
                else {
                    user.setName(userName);
                    break;
                }
            }
        } catch (IOException ex) {
            disconnect(ex);
        }
    }

    /**
     * The user is asked to enter the date when he or she was last on a date.
     * If the date is not valid the user has to try again.
     */
    private void logInDate() {
        String datePuffer;
        try {
            sendMessage("I am curious. When was the last time you were on a date? (dd.mm.yyyy)");
            datePuffer = reader.readLine();

            while (turnIntoDate(datePuffer) == null || !datePuffer.matches("^\\d?\\d.\\d{2}.\\d{4}$")) {
                sendMessage("This date is not in the correct format. Please try again. ");
                datePuffer = reader.readLine();
            }
            user.setLastDate(LocalDate.parse(datePuffer, formatter));
        } catch (IOException ex) {
            disconnect(ex);
        }
    }

    /**
     * Sends welcome message to the user and notifies all other users.
     */
    private void welcome() {
        sendMessage("Thank you! Welcome " + user + "!");
        sendMessage("""
                Type: 'bye' to leave the room.\s
                      '@<name>' to send a direct message.\s
                      '#help' to list all commands.\s
                      '#play' to play the LoveLetter game.""");

        server.communicate(user + " joined the room.", user);
    }

    /**
     * The connection is closed and other users get notified that the user left.
     */
    private void disconnect() {
        sendMessage("Bye " + user);
        server.removeUser(user);
        server.communicate(user + " left the room.", user);
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
        System.err.println("Exception in UserThread with address " + socket.getRemoteSocketAddress() + ": " + ex.getMessage());
        server.removeUser(user);
        server.communicate(user + " left the room.", user);
        System.out.println("Closed the connection with address:   " + socket.getRemoteSocketAddress());
        try {
            socket.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}

