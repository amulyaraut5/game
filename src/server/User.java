package server;

import java.util.Date;

public class User {
    private UserThread thread;
    private String name = "Unnamed user";
    private Date lastDate;

    public User(UserThread thread) {
        this.thread = thread;
    }

    public UserThread getThread() {
        return thread;
    }

    public String getName() {
        return name;
    }

    public Date getLastDate() {
        return lastDate;
    }

    public void message(String message){
        thread.sendMessage(message);
    }
}
