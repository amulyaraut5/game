package utilities;

import com.google.gson.annotations.SerializedName;

public abstract class Utilities {

    public static final int PORT = 5558;

    public static final Orientation[] UP_LEFT = {Orientation.UP, Orientation.LEFT};
    public static final Orientation[] LEFT_UP = {Orientation.LEFT, Orientation.UP};
    public static final Orientation[] UP_RIGHT = {Orientation.UP, Orientation.RIGHT};
    public static final Orientation[] RIGHT_UP = {Orientation.RIGHT, Orientation.UP};
    public static final Orientation[] DOWN_RIGHT = {Orientation.DOWN, Orientation.RIGHT};
    public static final Orientation[] RIGHT_DOWN = {Orientation.RIGHT, Orientation.DOWN};
    public static final Orientation[] DOWN_LEFT = {Orientation.DOWN, Orientation.LEFT};
    public static final Orientation[] LEFT_DOWN = {Orientation.LEFT, Orientation.DOWN};

    public static final Orientation[] UP_DOWN = {Orientation.UP, Orientation.DOWN};
    public static final Orientation[] DOWN_UP = {Orientation.DOWN, Orientation.UP};
    public static final Orientation[] RIGHT_LEFT = {Orientation.RIGHT, Orientation.LEFT};
    public static final Orientation[] LEFT_RIGHT = {Orientation.LEFT, Orientation.RIGHT};


    //Cards
    public static final int SPAM_CARDCOUNT = 36;
    public static final int TROJANHORSE_CARDCOUNT = 12;
    public static final int VIRUS_CARDCOUNT = 18;
    public static final int WORM_CARDCOUNT = 6;
    public static final int MOVE1_CARDCOUNT = 5;
    public static final int MOVE2_CARDCOUNT = 3;
    public static final int MOVE3_CARDCOUNT = 1;
    public static final int BACKUP_CARDCOUNT = 1;
    public static final int TURNLEFT_CARDCOUNT = 3;
    public static final int TURNRIGHT_CARDCOUNT = 3;
    public static final int UTURN_CARDCOUNT = 1;
    public static final int AGAIN_CARDCOUNT = 2;
    public static final int POWERUP_CARDCOUNT = 1;

    public enum CardType {
        MoveI, MoveII, MoveIII, TurnLeft, TurnRight, UTurn, BackUp, PowerUp, Again,
        Spam, Wurm, Virus, Trojaner
    }

    public enum Rotation {
        @SerializedName("left") LEFT,
        @SerializedName("right") RIGHT
    }


    public enum Difficulty {
        BEGINNER, ADVANCED, EXTREME;

    }

    public enum Phase {
        CONSTRUCTION(0), UPGRADE(1), PROGRAMMING(2), ACTIVATION(3);
        private final int phase;

        Phase(int phase) {
            this.phase = phase;
            Orientation a = Orientation.UP;
        }
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

    public enum AttributeType {
        Antenna, Belt, ControlPoint, Empty, EnergySpace, Gear,
        Laser, Pit, PushPanel, Reboot, RotatingBelt, Wall
    }
}
