package server;

import game.GameController;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatServer {

    private final int port;
    private ArrayList<UserThread> userThreads = new ArrayList<>();
    private ArrayList<String> userNames = new ArrayList<>();
    private ArrayList<LocalDate> lastDates = new ArrayList<>();

    private GameController gameController = new GameController();

    public ChatServer(int port) {
        this.port = port;
    }


    public static void main(String[] args) {
        int port = 4444;

        ChatServer server = new ChatServer(port);
        server.startServer();
    }

    /**
     * startServer() method opens a channel for the connection between Server and Client
     */
    private void startServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Chat server is waiting for clients to connect to port " + port + ".");
            acceptClients(serverSocket);
        } catch (IOException e) {
            System.err.println("could not connect: " + e.getMessage());
        }
    }
    /**
     * This method accepts the clients request and ChatServer assigns a separate thread to handle multiple clients
     *
     * @param serverSocket socket from which connection is to be established
     */
    public void acceptClients(ServerSocket serverSocket) {
        boolean accept = true;
        while (accept) {
            try {
                Socket client_socket = serverSocket.accept();

                System.out.println("Accepted the connection from address: " + client_socket.getRemoteSocketAddress());
                UserThread newUser = new UserThread(client_socket, this);
                userThreads.add(newUser);
                newUser.start();
            } catch (IOException e) {
                accept = false;
                System.out.println("Accept failed on: " + port);
            }
        }
    }

    /**
     * This method sends a message to each client which is connected to the server except the sender itself
     */
    public void communicate(String message, UserThread sender) {
        Pattern gamePattern = Pattern.compile("^#+");
        Matcher matcher = gamePattern.matcher(message);

        if (matcher.lookingAt()) {
            gameController.readCommand(message, sender);
        } else {
            for (UserThread user : userThreads) {
                if (user != sender) {
                    user.sendMessage(message);
                }
            }
        }
        for (UserThread user : userThreads) {
            if (user != sender) {
                user.sendMessage(message);
            }
        }
    }

    /**
     * send message only to one client
     */
    public void justUser(String message, UserThread thisUser) {
        //TODO sends commands from GameController to the player (e.g. "Please deal card")
        for (UserThread aUser : userThreads) {
            if (aUser == thisUser) {
                aUser.sendMessage(message);
            }
        }
    }
    /**
     * After the UserThread is created and user enters the name, the new user is added to the Set of the names.
     *
     * @param userName userName to be added
     */
    public void addUserName(String userName) {
        userNames.add(userName);
    }
    /**
     * After the UserThread is created and user enters the date, the new date is added to the Set of the dates.
     *
     * @param lastDate lastDate to be added
     */
    public void addDate(LocalDate lastDate) {
        lastDates.add(lastDate);
    }
    /**
     * It checks if the username is already in the list of assigned usernames.
     *
     * @param userName userName to be checked
     * @return True if username is free, false if it´s already assigned
     */
    public boolean checkAvailability(String userName) {
        return (!userNames.contains(userName));
    }
    /**
     * This method removes the username and UserThread from their respective Set.
     * Removing can be done by calling a pre defined method remove().
     *
     * @param userName userName to be removed
     * @param thisUser UserThread to be removed
     */
    public void removeUser(String userName, UserThread thisUser) {
        userNames.remove(userName);
        userThreads.remove(thisUser);
    }
}
