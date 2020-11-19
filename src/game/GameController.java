package game;

import server.UserThread;

import java.util.ArrayList;

public class GameController {
    private int playerCount;

    public GameController () {

    }

    public void readCommand (String message, UserThread sender) {
        //verwaltet jegliche commands, die vom server gesendet werden
    }

    public boolean isStarted () {
        //überprüft, ob bereits ein Spiel gestartet wurde
        return true;
    }

    public void playGame (UserThread sender, String userName) {

        GameBoard gameBoard = new GameBoard();
        // isStarted() überprüft, ob Spiel bereits gestartet
        // wenn ja, Rückmeldung an User
        // wenn nein, überprüfen, wie viele Spieler bereits beigetreten sind
        // wenn <4, dann addUser(userThread, userName)
    }

    public void sendMessage (String message, ArrayList<Player> playerList) {
        //method to send something from GameBoard to GameController and then just to the playing users
    }

}
