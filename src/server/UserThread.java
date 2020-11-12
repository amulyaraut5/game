package server;

import java.io.*;
import java.net.Socket;

public class UserThread extends Thread{
    private Socket socket;
    private ChatServer server;
    private PrintWriter userOut;

  private UserThread(Socket socket, ChatServer server){
      this.socket = socket;
      this.server = server;
  }
    /**
     * This method starts a new thread when ever a client gets connected to the server, therefore the server is able to handle multiple clients at the same time.
     * It sends a welcome-message when a new user enters the chatroom and notifies all other users.
     * It runs a loop of reading messages sent from the user and sending them to all other users until the user types "bye" to disconnect.
     * The other users get notified when a user disconnects and the connection is closed.
     */
    @Override
    public void run() {
      try {
          InputStream input = socket.getInputStream();
          BufferedReader reader = new BufferedReader(new InputStreamReader(input));

          OutputStream output = socket.getOutputStream();
          userOut = new PrintWriter(output, true);

          String userName = reader.readLine();
          server.addUserName(userName);

          String serverMessage = "Welcome " + userName;
          server.justUser(serverMessage, this);

          serverMessage = userName + " joined the room.";
          server.communicateOthers(serverMessage, this);

          String clientMessage;

          do {
              clientMessage = reader.readLine();
              serverMessage = userName + ": " + clientMessage;
              String youMessage = "You: " + clientMessage;

              server.communicateOthers(serverMessage, this);
              server.justUser(youMessage,this);


          } while (!clientMessage.equals("bye"));
          server.removeUser(userName, this);
          socket.close();

          serverMessage = userName + " left the room.";
          server.communicateOthers(serverMessage, this);

      } catch (IOException ex) {
          System.out.println("Error occurred in UserThread: " + ex.getMessage());
          ex.printStackTrace();
      }

    }

}
