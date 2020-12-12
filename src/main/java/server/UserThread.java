package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import json.Message;

import java.io.*;
import java.net.Socket;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handles connection for each connected client,
 * therefore the server is able to handle multiple clients at the same time.
 *
 * @author vossa,
 */

public class UserThread extends Thread {
    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy");
    private final User user; //Connected user, which data has to be filled in logIn()
    private final Socket socket;
    private final Server server;
    private PrintWriter userOut;
    private BufferedReader reader;
    private boolean exit = false;

    public UserThread(Socket socket, Server server, User user) {
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
     * Turns a String into a date or null
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
        if (!exit) logIn();
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

                 if (directMatcher.lookingAt()) {
                    if (!server.communicateDirect(clientMessage, user)) {
                        user.message("Your request does not correspond to the required format. Please try again. @<name> <message>");
                    }
                } else if (clientMessage.equals("userList")) {
                    String userList = "userList: ";
                    for (User user : server.getUsers()) {
                        userList += user.getName();
                        userList += " ";
                    }
                    user.message(userList);
                } else {
                    serverMessage = "[" + user + "]: " + clientMessage;
                    server.communicate(serverMessage, user);
                }
            }
        } catch (IOException ex) {
            if (!exit) disconnect(ex);
        }
        if (!exit) disconnect();
    }

    /**
     * prints a message for specific user
     *
     * @param message the message to be sent
     */
    public void sendMessage(String message) {
        if (!message.isBlank()) userOut.println(message);
    }

    /**
     * Server processes the message which is send
     */
    protected void processMessage(User user, String msg) {
        JsonObject jsonObject = new JsonParser().parse(msg).getAsJsonObject();

        if (jsonObject.has("TextMessage")) {
            jsonObject = jsonObject.getAsJsonObject("TextMessage");
            sendMessage(jsonObject);
        }
    }

    /**
     * sends a message to the client
     * if Receiver contains user the message will be send only to these users
     * otherwise it will be send to all users
     */
    private void sendMessage(JsonObject jsonObject) {
        Gson gson = new Gson();
        if (jsonObject.has("Message")) {
            String strMessage = jsonObject.get("Message").getAsString();
            Message textMessage = gson.fromJson(strMessage, Message.class);
            server.communicate(textMessage.getMessage(), user);
        } else {
            //nothing
        }
    }

    /**
     * creating an object TextMessage and set the message
     */
    private Message getTextMessage(String text) {
        Message message = new Message();
        message.setMessage(text);
        return message;
    }

    /**
     * The user is asked to enter a name to log in and to enter the date where he last dated.
     * If the name already exists in the list of assigned usernames, the user is asked to try again.
     */
    private void logIn() {
        while (!exit) {
            try {
                String message = reader.readLine();
                if (message != null) {
                    String userName = message.substring(0, message.indexOf(" "));
                    String dateText = message.substring(message.indexOf(" ") + 1);

                    if (turnIntoDate(dateText) == null) {
                        sendMessage("#login " + "Please check your Date! (dd.mm.yyyy)");
                    } else if (!server.isAvailable(userName)) {
                        sendMessage("#login " + "This username is already taken!");
                    } else {
                        user.setName(userName);
                        sendMessage("#login " + "successful");
                        return;
                    }
                }
            } catch (IOException ex) {
                if (!exit) disconnect(ex);
            }
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
                      '#create' to play the LoveLetter game.""");
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
        System.err.println("Error in UserThread with address " + socket.getRemoteSocketAddress() + ": " + ex.getMessage());
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
