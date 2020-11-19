package game;

import server.UserThread;

public class GameController {

    public GameController () {

    }

    public void readCommand (String message, UserThread sender) {

    }

    public boolean isStarted () {
        return true;
    }

    public void playGame (UserThread sender, String userName) {

        GameBoard gameBoard = new GameBoard();

    }


    //play: new GameBoard and then method addUser(userThread, username)

    //es gibt Methode isStarted() in GameBoard um zu prüfen, ob Spiel bereits gestartet ist und man nicht mehr joinen kann
    //und es gibt Methode getPlayerCount() in GameBoard um zu schauen, ob schon mehr als 2 Player und max 4 da sind


    //method to send something from GameBoard to GameController and then just to the playing users
    // sendMessage(String message, ArrayList<Player> playerList)
}
