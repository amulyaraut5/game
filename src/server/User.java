package server;

import java.time.LocalDate;

/**
 * for every new client, a User is created.
 * An instance of this class has a reference to the UserThread connected with the specific user.
 * Every user does have a name and date of birth.
 * It is possible to sent a message to the user with message(String message).
 *
 * @author simon
 */
public class User {
    /**
     * The thread which handles the connection for each connected client.
     */
    private volatile UserThread thread;
    /**
     * The name of the user.
     */
    private volatile String name = "Unnamed user";
    /**
     * The birth date of the user. (Initialised with the minimum supported LocalDate.)
     */
    private volatile LocalDate birthDate = LocalDate.MIN;

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
     * Test if two different objects of User are the same user.
     * (i.e. if they have the same UserThread and therefore are connected to the same client.)
     * For example if Player a is this user
     *
     * @param a another user
     * @return true if they are the same user, otherwise false
     */
    public boolean equals(User a) {
        return this.thread == a.thread;
    }

    /**
     * Outputs a message to the user via its UserThread.
     *
     * @param message the message to be sent
     */
    public synchronized void message(String message) {
        thread.sendMessage(message);
    }

    /**
     * Returns a string representation of the user, i.e. the user's name.
     *
     * @return the name of the user
     */
    @Override
    public String toString() {
        return name;
    }

    /**
     * Gets the thread of the user.
     *
     * @return the related thread
     */
    public UserThread getThread() {
        return thread;
    }

    /**
     * Sets the thread of the user.
     *
     * @param thread the related thread
     */
    public synchronized void setThread(UserThread thread) {
        this.thread = thread;
    }

    /**
     * Gets the name of the user.
     *
     * @return the name of user
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the user.
     *
     * @param name the name of user
     */
    public synchronized void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the users date of birth
     *
     * @return time of the user's last date
     */
    public LocalDate getBirthDate() {
        return birthDate;
    }

    /**
     * Sets the users date of birth
     *
     * @param lastDate date of the user's birth
     */
    public synchronized void setBirthDate(LocalDate lastDate) {
        this.birthDate = lastDate;
    }
}
