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

    /**
     * Reagiert auf das command start.
     * Prüft, ob Spieler schon gejoined hat.
     * Prüft, ob ein Spiel bereits gestartet wurde.
     * Wenn nicht, dann wird geprüft, ob ein Spiel schon erstellt wurde.
     * Wenn ja, dann wird geprüft ob schon mind. 2 Spieler da sind.
     * Wenn alles passt, dann wird startGame aus dem Board aufgerufen.
     * Wenn etwas nicht passt, dann Hinweis an den User.
     */
    public void start () {
    //ruft play Funktion aus Board auf
    }

    /**
     * Method to send message from GameBoard to GameController and then just to the playing users
     * @param message
     * @param playerList
     */
    public void sendMessage (String message, ArrayList<Player> playerList) {
        //get User Thread
    }

    /**
     * Method to send message from GameBoard to GameController and then just to one targeted player.
     */
    public void sendPrivateMessage (String message, Player justPlayer) {

    }
}
