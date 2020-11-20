package game;

import server.UserThread;

import java.util.ArrayList;

public class GameController {

    private boolean startedGame = false;
    private boolean runningGame = false;
    private int playerCount = 0;
    private GameBoard gameboard;

    public GameController () {

    }

    /**
     * reads and distributes all incoming commands regarding the game
     */
    public void readCommand (String message, UserThread sender) {
        //TODO: help
        //TODO: score
    }

    /**
     * Reacts to command "create".
     * This method creates a new GameBoard if not already done and adds the User to the ArrayList.
     * If a GameBoard was already created the User gets a message to join the game.
     */
    public void create (UserThread sender, String userName) {
        if (!startedGame) {
            GameBoard gameBoard = new GameBoard();
            startedGame = true;
            //TODO: join für den user aufrufen.
        } else if (startedGame && !runningGame) {
            //TODO: message: Soemone has already created a game. Type join if you want to join the game.
        } else if (startedGame && runningGame) {
            //TODO: message: You're friends have started without you. Just wait and join in the next round.
        }
    }

    /**
     * Reagiert auf das command join.
     * Diese Methode überprüft, ob schon ein GameBoard exisitert, wenn nein, Aufforderung "play".
     * Dann wird überprüft, ob das Spiel bereits gestartet wurde.
     * Wenn nicht, wird der Spieler hinzugefügt, wenn <4 Spieler.
     * Falls Spiel gestartet, Hinweis.
     */

    public void join (UserThread sender, String userName) {
        if (startedGame && !runningGame && playerCount < 4) {
            //TODO: message: You've joined the game.
            //TODO: addUser
            playerCount++;
        } else if (!startedGame) {
            //message: please start a game
        } else if (runningGame) {
            //message: The game is already running
        } else if (playerCount >= 4) {
            //message: game already full
        }
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
        if (startedGame && !runningGame && (playerCount >= 2)) {
            gameboard.playGame();
            runningGame = true;
        } else if (!startedGame) {
            //message: please start a game
        } else if (runningGame) {
            //message: The game is already running
        } else if (playerCount > 2) {
            //message: not enough players to start the game yet
        }
    }

    /**
     * method that resets all game controlling variables when a Game is ended
     */
    public void reset () {
        startedGame = false;
        runningGame = false;
        playerCount = 0;
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
