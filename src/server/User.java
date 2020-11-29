package server;

import java.time.LocalDate;

/**
 * @author simon
 */
public class User {
    /** The thread which handles connection for each connected client. */
    private volatile UserThread thread;
    /** The name of the user. */
    private volatile String name = "Unnamed user";
    /** The date when the user was most recently on a date. (Initialised with the minimum supported LocalDate.) */
    private volatile LocalDate lastDate = LocalDate.MIN;

    /** Constructor for the User TODO is empty? default constructor? */
    public User() {
    }

    /**
     * Test if two different objects of User are the same user.
     * (i.e. if they have the same UserThread and therefore are connected to the same client.)
     * For example if Player a is User b.
     *
     * @param a first user
     * @param b second user
     * @return true if they are the same user, otherwise false
     */
    public static boolean isSameUser(User a, User b) {
        return a.thread == b.thread;
    }

    /**
     * Outputs a message to the user via its UserThread.
     * @param message the message to be sent
     */
    public synchronized void message(String message) {
        thread.sendMessage(message);
    }

    /**
     * Returns a string representation of the user, i.e. the user's name.
     * @return the name of the user
     */
    @Override
    public String toString() {
        return name;
    }

    /**
     * Gets the thread of the user.
     * @return the related thread
     */
    public UserThread getThread() {
        return thread;
    }

    /**
     * Sets the thread of the user.
     * @param thread the related thread
     */
    public synchronized void setThread(UserThread thread) {
        this.thread = thread;
    }

    /**
     * Gets the name of the user.
     * @return the name of user
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the user.
     * @param name the name of user
     */
    public synchronized void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the date when the user was most recently on a date.
     * @return time of the user's last date
     */
    public LocalDate getLastDate() {
        return lastDate;
    }

    /**
     * Sets the date when the user was most recently on a date.
     * @param lastDate date of the user's last date
     */
    public synchronized void setLastDate(LocalDate lastDate) {
        this.lastDate = lastDate;
    }
}
