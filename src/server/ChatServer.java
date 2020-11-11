package server;

public class ChatServer {
    private int port;

    public ChatServer(int port) {
    }


    public static void main(String[] args) {
        int port = 4444;

        ChatServer server = new ChatServer(port);
        server.execute();
    }

    public void execute(){
    }
    
    public void communicateAll(String message){
        //delivers a message from one user to the all users
    }

    public void communicateOthers(String message, UserThread exceptMe){
        //delivers a message from one user to others
    }

    public void justUser(String message, UserThread thisUser){
        //sends a message only to one client
    }

    public void addUserName(String userName){
       //store username in newly connected client
    }

    public void removeUser(String userNAme, UserThread thisUser){
        //removes associated username and UserThread when client is disconnected
    }





}
