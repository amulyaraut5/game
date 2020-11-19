package game;

import server.UserThread;

import java.util.ArrayList;

public class GameController {

    private boolean startedGame;
    private boolean runningGame;

    public GameController () {

    }

    /**
     * verwaltet jegliche commands, die vom server gesendet werden
     */
    public void readCommand (String message, UserThread sender) {
    }

    /**
     * überprüft, ob bereits ein Spiel erstellt wurde
     */
    public boolean isStarted () {
        return startedGame;
    }

    /**
     * überprüft, ob bereits ein Spiel gestartet wurde
     */
    public boolean isRunning () {
        return runningGame;
    }
    /**
     * Reagiert auf das command play.
     * Diese Methode erstellt ein neues Gamebord, falls noch keines erstellt wurde.
     * Danach wird User darauf hingewiesen, dass er joinen kann
     * Wurde bereits ein Gameboard erstellt, wird der User darauf hingewiesen, dass er joinen kann.
     */
    public void create (UserThread sender, String userName) {

        GameBoard gameBoard = new GameBoard();

    }

    /**
     * Reagiert auf das command join.
     * Diese Methode überprüft, ob schon ein GameBoard exisitert, wenn nein, Aufforderung "play".
     * Dann wird überprüft, ob das Spiel bereits gestartet wurde.
     * Wenn nicht, wird der Spieler hinzugefügt, wenn <4 Spieler.
     * Falls Spiel gestartet, Hinweis.
     */

    public void join (UserThread sender, String userName) {

    }

    public void start () {
        // ruft startGame Methode in Board auf
        // Boolean Wert zur verwaltung, ob gestartet oder nicht
    }

    public void sendMessage (String message, ArrayList<Player> playerList) {
        //method to send something from GameBoard to GameController and then just to the playing users
    }

}
