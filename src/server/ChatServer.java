package server;

import game.GameController;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChatServer {
    private static final ArrayList<User> users = new ArrayList<>(10);
    private final int port;
    private final GameController gameController = new GameController(this);

    public ChatServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) {
        int port = 4444;

        ChatServer server = new ChatServer(port);
        server.start();
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    /**
     * start() method opens a channel for the connection between Server and Client
     */
    private void start() {
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
                User user = new User();
                users.add(user);
                UserThread thread = new UserThread(client_socket, this, user);
                thread.start();
            } catch (IOException e) {
                accept = false;
                System.out.println("Accept failed on: " + port);
            }
        }
    }

    /**
     * This method sends a message to each client which is connected to the server except the sender itself
     */
    public void communicate(String message, User sender) {

        for (User user : users) {
            if (user != sender) {
                user.message(message);
            }
        }
    }

    public boolean communicateDirect(String message, User sender) {
        String userName = (message.split(" ", 2)[0]);
        String destinationUser = userName.substring(1);
        String messageUser = message.split(" ", 2)[1];
        if (messageUser.length() == 0) return false;
        for (User user : users) {
            if (user.getName().equals(destinationUser)) {
                user.message("[" + sender + " (direct)]: " + messageUser);
                return true;
            }
        }
        return false;
    }

    public void communicateGame(String message, User sender) {
        gameController.readCommand(message, sender);
    }

    public void communicateAll(String message) {
        for (User user : users)
            user.message(message);
    }

    /**
     * It checks if the username is already used of another user.
     *
     * @param userName userName to be checked
     * @return True if username is free, false if it´s already assigned
     */
    public synchronized boolean isAvailable(String userName) {
        for (User u : users) {
            if (u.getName().equals(userName)) {
                return false;
            }
        }
        return true;
    }

    /**
     * This method removes a user from the list of users.
     *
     * @param user User to be removed
     */
    public void removeUser(User user) {
        users.remove(user);
    }
}
