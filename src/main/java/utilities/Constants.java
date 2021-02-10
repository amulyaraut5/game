package utilities;

import utilities.enums.Orientation;

public final class Constants {
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
    public static final int SPAM_CARDCOUNT = 38;
    public static final int TROJAN_CARDCOUNT = 12;
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

    public static final String CHEAT_LIST = """
                                    
            ------------------------------------------
            Cheats
            ------------------------------------------
            #cheats             |  lists all cheats
            #cheats on/off  |  turns cheats on/off
            #tp <pos>        |  teleports the robot
            #tp <x> <y>     |  teleports the robot
            #r <u,r,d,l>        |  rotates up, right...
            #endTimer         |  ends the timer
            #autoPlay          |  autoplays all PlayIt
            #activateBoard  |  activates the board
            #damage <n>   |  deals spam cards
            #damageDecks  |  shows damage decks
            #emptySpam     |  empties spam deck
            #win                   |  player wins
            #fire          | fires laser
            ------------------------------------------
            """;
    public static final String HOTKEYSLIST = """
                                      
            ------------------------------------------
            Hotkeys
            ------------------------------------------
            click on map       |  teleports the robot
            WASD keys         |  rotates the robot
            ------------------------------------------
            """;

    private Constants() {
    }
}
