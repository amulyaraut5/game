package server;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private int port;
    private ArrayList<UserThread> userThreads;
    private ArrayList<String> userNames;

    public ChatServer(int port) {
    }


    public static void main(String[] args) {
        int port = 4444;

        ChatServer server = new ChatServer(port);
        server.execute();
    }

    /**
     * start_Server() method opens a channel for the connection between Server and Client
     */
    private  void start_Server(){

        ServerSocket server_socket = null;
        try {
            server_socket = new ServerSocket(port);
            System.out.println("Server is waiting for the connection" + port);
            acceptClients(server_socket);

        } catch (IOException e) {
            System.err.println("could not connect: " + e.getLocalizedMessage());
            e.printStackTrace();
        }

    }
    /**
     * This method accepts the clients request and ChatServer assigns a separate thread to handle multiple clients
     * @param server_socket socket from which connection is to be established
     */
    public void acceptClients(ServerSocket server_socket)  {
        while(true){
            Socket client_socket = null;
            try {
                client_socket = server_socket.accept();

                System.out.println("Accepted the connection from address: " + client_socket.getRemoteSocketAddress());
                UserThread newUser = new UserThread(client_socket, this);
                userThreads.add(newUser);
                newUser.start();
            }catch (IOException e){
                System.out.println("Accept failed on:" + port);
            }
        }
    }
    
    public void communicateAll(String message){
        for (UserThread user: userThreads){
            user.sendMessage(message);
        }
        //delivers a message from one user to the all users
    }

    public void communicateOthers(String message, UserThread exceptMe){
        //delivers a message from one user to others
    }

    public void justUser(String message, UserThread thisUser){
        //sends a message only to one client
    }
    /**
     * After the Userthread is created and user enters the name, the new user is added to the Set of the names.
     *
     * @param userName userName to be added
     */
    public void addUserName(String userName){
        userNames.add(userName);
    }
    /**
     * This method removes the username and userthread from their respective Set .
     * Removing can be done by calling a pre defined method remove().
     * @param userName userName to be removed
     * @param thisUser userthread to be removed
     */
    public void removeUser(String userName, UserThread thisUser){
        boolean b = userNames.remove(userName);
        if (b)
        {userThreads.remove(userName);
            System.out.println(userName + " quitted") ;
        }
    }

    public boolean hasUsers(){
        return (!this.userNames.isEmpty());
    }





}
