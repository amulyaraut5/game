package game;

import server.UserThread;

import java.util.ArrayList;

public class GameController {

    private boolean startedGame;
    private boolean runningGame;

    public GameController () {

    }

    public void readCommand (String message, UserThread sender) {
        //verwaltet jegliche commands, die vom server gesendet werden
    }

    /**
     * überprüft, ob bereits ein Spiel gestartet wurde
     */
    public boolean isStarted () {
        return startedGame;
    }

    public boolean isRunning () {
        return runningGame;
    }

    public void playGame (UserThread sender, String userName) {

        GameBoard gameBoard = new GameBoard();
        // überprüfen, ob Spiel bereits erstellt wurde
        // nein, dann neues GameBoard
        // ja, dann überprüfen, ob Spiel schon gestartet wurde
        // isStarted() überprüft, ob Spiel bereits gestartet
        // wenn ja, Rückmeldung an User
        // wenn nein, Rückmeldung an user, dass er joinen kann.
    }

    public void joinGame (UserThread sender, String userName) {
        // ob  wie viele Spieler bereits beigetreten sind
        // wenn <4, dann addUser(userThread, userName)
    }

    public void startGame () {
        // ruft startGame Methode in Board auf
        // Boolean Wert zur verwaltung, ob gestartet oder nicht
    }

    public void sendMessage (String message, ArrayList<Player> playerList) {
        //method to send something from GameBoard to GameController and then just to the playing users
    }

}
