package utilities;

import com.google.gson.annotations.SerializedName;

public class Utilities {

    public static final int PORT = 5557;

    public static final Orientation[] UP_LEFT = {Orientation.UP, Orientation.LEFT};
    public static final Orientation[] LEFT_UP = {Orientation.LEFT, Orientation.UP};
    public static final Orientation[] UP_RIGHT = {Orientation.UP, Orientation.RIGHT};
    public static final Orientation[] RIGHT_UP = {Orientation.RIGHT, Orientation.UP};
    public static final Orientation[] DOWN_RIGHT = {Orientation.DOWN, Orientation.RIGHT};
    public static final Orientation[] RIGHT_DOWN = {Orientation.RIGHT, Orientation.DOWN};
    public static final Orientation[] DOWN_LEFT = {Orientation.DOWN, Orientation.LEFT};
    public static final Orientation[] LEFT_DOWN = {Orientation.DOWN, Orientation.RIGHT};

    public enum Orientation {
        @SerializedName("up") UP,
        @SerializedName("down") DOWN,
        @SerializedName("left") LEFT,
        @SerializedName("right") RIGHT;

        private Orientation oppositeOrientation;

        static {
            Orientation.UP.oppositeOrientation = Orientation.DOWN;
            Orientation.DOWN.oppositeOrientation = Orientation.UP;
            Orientation.LEFT.oppositeOrientation = Orientation.RIGHT;
            Orientation.RIGHT.oppositeOrientation = Orientation.LEFT;
        }

        public Orientation getOpposite(){
            return oppositeOrientation;
        }
    }


    public enum Rotation {
        @SerializedName("left") LEFT,
        @SerializedName("right") RIGHT
    }

    public enum Difficulty {
        BEGINNER, ADVANCED, EXTREME
    }

    public enum MessageType {
        HelloClient, HelloServer, Welcome,
        PlayerValues, PlayerAdded, SetStatus,
        PlayerStatus, GameStarted, SendChat,
        ReceivedChat, Error, ConnectionUpdate,
        ActivePhase, CardPlayed, CardSelected,
        CardsYouGotNow, CurrentCards, CurrentPlayer,
        DiscardHand, DrawDamage, Movement, NotYourCards,
        PickDamage, PlayCard, SelectCard, SelectDamage,
        SelectionFinished, SetStartingPoint, ShufflingCoding,
        StartingPointTaken, TimerEnded, TimerStarted,
        YourCards, PlayerTurning, PlayIt, Reboot, Energy,
        GameWon, CheckPointReached
    }
}
