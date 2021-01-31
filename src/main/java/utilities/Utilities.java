package utilities;

import utilities.enums.Orientation;

public abstract class Utilities {

    //Connection
    public static final int PORT = 5570;
    public static final double PROTOCOL = 1.0;

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

    //Map
    public static final int MAP_HEIGHT = 10;
    public static final int MAP_WIDTH = 13;
    public static final int FIELD_SIZE = 60;

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

    //Game
    public static final int MIN_PLAYERS = 2;
    public static final int MAX_PLAYERS = 6;
    public static final int ENERGY_BANK = 48;

    //
    public static final int[][] STANDARD_PRIORITY_MAP = new int[][]{
            {25, 30, 35, 40, 45, 50, 55, 60, 65, 70, 75, 80, 85},
            {30, 35, 40, 45, 50, 55, 60, 65, 70, 75, 80, 85, 90},
            {35, 40, 45, 50, 55, 60, 65, 70, 75, 80, 85, 90, 95},
            {40, 45, 50, 55, 60, 65, 70, 75, 80, 85, 90, 95, 100},
            {35, 40, 45, 50, 55, 60, 65, 70, 75, 80, 85, 90, 95},
            {30, 35, 40, 45, 50, 55, 60, 65, 70, 75, 80, 85, 90},
            {25, 30, 35, 40, 45, 50, 55, 60, 65, 70, 75, 80, 85},
            {20, 25, 30, 35, 40, 45, 50, 55, 60, 65, 70, 75, 80},
            {15, 20, 25, 30, 35, 40, 45, 50, 55, 60, 65, 70, 75},
            {10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 60, 65, 70},
    };

}
