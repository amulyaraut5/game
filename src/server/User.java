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

    /**
     * Test if two different objects of User are the same user.
     * (i.e. if they have the same UserThread and therefore are connected to the same client.)
     * For example if Player a is User b.
     *
     * @param a
     * @param b
     * @return
     */
    public static boolean isSameUser(User a, User b) {
        return a.thread == b.thread;
    }

    public synchronized void message(String message) {
        thread.sendMessage(message);
    }

    @Override
    public String toString() {
        return name;
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
}
