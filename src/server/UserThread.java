package server;

import java.io.*;
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
     * The method runs a loop of reading messages from the user and sending them to all other users.
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

    /**
     * The user is asked to enter a name to log in.
     * If the name already exists in the list of assigned usernames, the user is asked to try again.
     *
     * @return the entered and accepted username
     */
    public String logIn() {
        sendMessage("Enter your username:");
        try {
            userName = reader.readLine();
            while (server.check_for_emptyString(userName)) {
                sendMessage("Empty String is not valid. Please enter your username:");
                userName = reader.readLine();
            }
            while (!server.checkAvailability(userName)) {
                sendMessage("This username is already taken. Please try another one:");
                userName = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userName;
    }

    /**
     * Sends welcome message to the user and notifies all other users.
     */
    public void welcome(){
        server.addUserName(userName);
        sendMessage("Welcome " + userName + "!");
        sendMessage("Type \"bye\" to leave the room.");
        String serverMessage = userName + " joined the room.";
        server.communicate(serverMessage, this);
    }

    /**
     * prints a message for one specific user
     * @param message the message to be sent
     */
    public void sendMessage(String message){
          userOut.println(message);
        }
    }

