package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChatServer {
    private final int port;
    private List<User> users = Collections.synchronizedList(new ArrayList<User>(10));

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
            System.err.println("could not connect: " + e.getMessage());
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
            try {
                Socket client_socket = server_socket.accept();

                System.out.println("Accepted the connection from address : " + client_socket.getRemoteSocketAddress());
                User user = new User();
                System.out.println(user);
                UserThread thread = new UserThread(client_socket, this, user);
                user.setThread(thread);
                users.add(user);
                thread.start();
            } catch (IOException e) {
                accept = false;
                System.out.println("Accept failed on: " + port);
            }
        }
    }

    /**
     * This method sends a message to each client which is connected to the server
     */
    public void communicate(String message) {
        //TODO checks regex and calls the method readCommand for these messages
        for (User user : users) {
            user.message(message);
        }
    }

    /**
     * This method sends a message to each client which is connected to the server except the sender itself
     */
    public void communicate(String message, User sender) {
        //TODO checks regex and calls the method readCommand for these messages
        for (User user : users) {
            if (user != sender) {
                user.message(message);
            }
        }
    }


    public void addUser(User user) {
        users.add(user);
    }

    /**
     * It checks if the username is already used of another user.
     *
     * @param userName userName to be checked
     * @return True if username is free, false if it´s already assigned
     */
    public synchronized boolean isAvailable(String userName) {
        for (User u : users) {
            if (u.getName() == userName) {
                System.out.println("used");
                return false;
            }
        }
        System.out.println("unused");
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
