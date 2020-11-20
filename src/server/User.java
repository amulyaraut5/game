package server;

import java.util.Date;

public class User {
    private UserThread thread;
    private String name = "Unnamed user";
    private Date lastDate = new Date(0); //TODO set low date

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
