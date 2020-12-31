package utilities;

public class Utilities {

    public static final int PORT = 5555;
    public static final Orientation[] UP_LEFT = {Orientation.UP, Orientation.LEFT};
    public static final Orientation[] LEFT_UP = {Orientation.LEFT, Orientation.UP};
    public static final Orientation[] UP_RIGHT = {Orientation.UP, Orientation.RIGHT};
    public static final Orientation[] RIGHT_UP = {Orientation.RIGHT, Orientation.UP};
    public static final Orientation[] DOWN_RIGHT = {Orientation.DOWN, Orientation.RIGHT};
    public static final Orientation[] RIGHT_DOWN = {Orientation.RIGHT, Orientation.DOWN};
    public static final Orientation[] DOWN_LEFT = {Orientation.DOWN, Orientation.LEFT};
    public static final Orientation[] LEFT_DOWN = {Orientation.DOWN, Orientation.RIGHT};

    public enum Orientation {
        UP, DOWN, LEFT, RIGHT
    }

    public enum Rotation {
        LEFT, RIGHT
    }

    public enum Difficulty {
        BEGINNER, ADVANCED, EXTREME
    }

    public enum MessageType {
        HelloClient, HelloServer, Welcome,
        PlayerValues, PlayerAdded, SetStatus,
        PlayerStatus, GameStarted, SendChat,
        ReceivedChat, Error, ConnectionUpdate
    }

}
