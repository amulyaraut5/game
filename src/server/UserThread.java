package server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.net.Socket;

public class UserThread extends Thread{
    private Socket socket;
    private ChatServer server;
    private PrintWriter userOut;
    private BufferedReader reader;

    private String userName;

  public UserThread(Socket socket, ChatServer server){
      this.socket = socket;
      this.server = server;
      this.userName = userName;

      try {
          InputStream input = socket.getInputStream();
          reader = new BufferedReader(new InputStreamReader(input));
          OutputStream output = socket.getOutputStream();
          userOut = new PrintWriter(output, true);
      } catch(IOException ex){
          System.out.println("Error occurred in UserThread: " + ex.getMessage());
          ex.printStackTrace();
      }
  }

    /**
     * The method starts new thread when a client gets connected, therefore server is able to handle multiple clients at the same time.
     * It runs a loop of reading messages sent from the user and sending them to all other users.
     * The other users get notified when a user disconnects by typing "bye" and the connection is closed.
     */
    @Override
    public void run() {
        try {
            logIn();
            welcome();
            String clientMessage;
            String serverMessage;
            do {
                clientMessage = reader.readLine();
                serverMessage = "[" + userName + "] : " + clientMessage;
                server.communicate(serverMessage, this);

            } while (!clientMessage.equals("bye"));
            sendMessage("Bye " + userName);
            server.removeUser(userName, this);
            socket.close();

            serverMessage = userName + " left the room.";
            server.communicate(serverMessage, this);

        } catch (IOException ex) {
            System.out.println("Error in UserThread: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public String logIn() {
        sendMessage("Enter your username");
        try {
            userName = reader.readLine();
            while (server.checkUserNames(userName)) {
                sendMessage("This username is already taken please try another one");
                userName = reader.readLine();return userName;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userName;
    }

    public void welcome(){
        server.addUserName(userName);
        sendMessage("Welcome " + userName + "!");
        String serverMessage = userName + " joined the room.";
        server.communicate(serverMessage, this);
    }

      //prints a message for one specific user
    public void sendMessage(String message){
          userOut.println(message);
        }
    }

