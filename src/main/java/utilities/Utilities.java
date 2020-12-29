package utilities;

public class Utilities {

    public enum Orientation {
        UP, DOWN, LEFT, RIGHT
    }

    public enum Rotation {
        LEFT, RIGHT
    }
    public enum Difficulty{
        BEGINNER, ADVANCED, EXTREME
    }

    public enum MessageType {
        HelloClient, HelloServer, Welcome,
        PlayerValues, PlayerAdded, SetStatus,
        PlayerStatus, GameStarted, SendChat,
        ReceivedChat, Error, ConnectionUpdate
    }

    public static Orientation[] upLeft= {Orientation.UP, Orientation.LEFT};
    public static Orientation[] upRight= {Orientation.UP, Orientation.RIGHT};
    public static Orientation[] downRight= {Orientation.DOWN, Orientation.RIGHT};
    public static Orientation[] downLeft= {Orientation.DOWN, Orientation.LEFT};

    public static final int PORT = 5555;

}
