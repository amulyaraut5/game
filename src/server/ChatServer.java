package server;

public class ChatServer {

    
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
