package server;

import java.util.Date;

public class User {
    private volatile static UserThread thread;
    private volatile static String name = "Unnamed user";
    private volatile static Date lastDate = new Date(0); //TODO set low date

    public User(UserThread thread, String name) {
        this.thread = thread;
        this.name = name;
    }

    public User() {

    }

    public UserThread getThread() {
        return thread;
    }

    public void setThread(UserThread thread) {
        this.thread = thread;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getLastDate() {
        return lastDate;
    }

    public void setLastDate(Date lastDate) {
        this.lastDate = lastDate;
    }

    public void message(String message) {
        thread.sendMessage(message);
    }
}
