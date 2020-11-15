package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChatServer {
    private final int port;
    private ArrayList<UserThread> userThreads = new ArrayList<>();
    private ArrayList<String> userNames = new ArrayList<>();

    public ChatServer(int port) {
        this.port = port;
    }


    public static void main(String[] args) {
        int port = 4444;

        ChatServer server = new ChatServer(port);
        server.start_Server();
    }

    /**
     * start_Server() method opens a channel for the connection between Server and Client
     */
    private void start_Server() {
        try {
            ServerSocket server_socket = new ServerSocket(port);
            System.out.println("Chat server is waiting for clients to connect to port " + port + ".");
            acceptClients(server_socket);
        } catch (IOException e) {
            System.err.println("could not connect: " + e.getLocalizedMessage());
            e.printStackTrace();
        }

    }

    /**
     * This method accepts the clients request and ChatServer assigns a separate thread to handle multiple clients
     *
     * @param server_socket socket from which connection is to be established
     */
    public void acceptClients(ServerSocket server_socket) {
        boolean accept = true;
        while (accept) {
            Socket client_socket = null;
            try {
                client_socket = server_socket.accept();

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
        for (UserThread user : userThreads) {
            if (user != sender) {
                user.sendMessage(message);
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
     * It checks if the username is already in the list of assigned usernames
     * It also makes sure that the user enters something and not a empty String.
     *
     * @param userName userName to be checked
     * @return True if username is free, false if it´s already assigned
     */
    public boolean checkAvailability(String userName) {
        if (userName.isBlank()) {
            return false;
        }
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
