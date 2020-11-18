package game;

import server.UserThread;

public class Player {
    private UserThread user;
    private String userName;

    public Player(UserThread user, String userName) {
        this.user = user;
        this.userName = userName;
    }
}
