package server;

import java.time.LocalDate;

/**
 *
 */
public class User {
    private volatile UserThread thread;
    private volatile String name = "Unnamed user";
    private volatile LocalDate lastDate = LocalDate.MIN;

    public User() {
    }

    public UserThread getThread() {
        return thread;
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

    public LocalDate getLastDate() {
        return lastDate;
    }

    public synchronized void setLastDate(LocalDate lastDate) {
        this.lastDate = lastDate;
    }

    public synchronized void message(String message) {
        thread.sendMessage(message);
    }
}
