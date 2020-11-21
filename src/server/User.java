package server;

import java.util.Date;

/**
 *
 */
public class User {
    private volatile UserThread thread;
    private volatile String name = "Unnamed user";
    private volatile Date lastDate = new Date(0);

    public User() {

    }

    public synchronized void setThread(UserThread thread) {
        this.thread = thread;
    }

    public String getName() {
        return name;
    }

    public synchronized void setName(String name) {
        this.name = name;
    }

    public Date getLastDate() {
        return lastDate;
    }

    public synchronized void setLastDate(Date lastDate) {
        this.lastDate = lastDate;
    }

    public synchronized void message(String message) {
        thread.sendMessage(message);
    }
}
