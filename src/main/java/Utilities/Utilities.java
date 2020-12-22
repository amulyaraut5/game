package Utilities;

import java.util.ArrayList;

public class Utilities {

    public enum Direction {
        NORTH, EAST, SOUTH, WEST
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

    public static final int PORT = 5555;
}
