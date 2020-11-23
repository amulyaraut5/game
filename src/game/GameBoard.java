package game;

import card.*;
import server.User;

import java.util.ArrayList;

public class GameBoard extends Thread {
    private GameController gameController;
    private Round activeRound;
    private ArrayList<Player> playerList = new ArrayList<>();
    private ArrayList<Player> winnerList = new ArrayList<>();

    private boolean started = false;
    private Player gameWinner;


    public GameBoard(GameController gameController) {
        this.gameController = gameController;
    }

    public static ArrayList<Card> createDeck() {
        ArrayList<Card> stackCards = new ArrayList<>();
        // every card just one time: princess, countess, king
        stackCards.add(new PrincessCard("Princess", 8));
        stackCards.add(new CountessCard("Countess", 7));
        stackCards.add(new KingCard("King", 6));
        //every card two times: prince, handmaid, baron, priest, guard
        for (int i = 0; i < 2; i++) {
            stackCards.add(new PrinceCard("Prince", 5));
            stackCards.add(new HandmaidCard("Handmaid", 4));
            stackCards.add(new BaronCard("Baron", 3));
            stackCards.add(new PriestCard("Priest", 2));
        }
        //guard five times:
        for (int i = 0; i < 5; i++) {
            stackCards.add(new GuardCard("Guard", 1));
        }
        return stackCards;
    }

    public void getScorePlayer(User user) {
        String score = "";
        score += "Tokens | Player\n";
        score += "---------------";
        for (Player pl : playerList) {
            score += "\n    " + pl.getTokenCount() + "  | " + pl;
        }
        user.message(score);
    }

    /**
     * @param user user who needs to be checked whether he already joined
     * @return if user has already joined
     */
    public boolean alreadyJoined(User user) {
        for (Player pl : playerList) {
            if (User.isSameUser(pl, user)) return true;
        }
        return false;
    }

    @Override
    public void run() {
        playGame();
    }

    /**
     * method to play rounds until someone has won the whole game
     */
    public void playGame() {
        Player firstPlayer = compareDates(playerList);
        ArrayList<Card> deck = createDeck();
        while (!gameWon()) {
            activeRound = new Round(firstPlayer, new ArrayList(deck), new ArrayList(playerList), this);
            activeRound.play();
            winnerList = activeRound.getRoundWinner();
            this.activeRound = null;
            for (Player resetPlayers : playerList) {
                resetPlayers.resetRound();
            }
            for (Player player : winnerList) {
                player.increaseNumOfTokens();
            }
            if (winnerList.size() == 1) {
                firstPlayer = winnerList.get(0);
            } else {
                firstPlayer = compareDates(winnerList);
            }
            gameController.communicateAll("The round has ended. Winner of the round: " + firstPlayer.getName());
        }
        gameController.communicateAll("Congratulations, " + gameWinner.getName() + " won the game! " +
                "\nType #create to create a new game.");
        gameController.reset();
    }

    /**
     * it finds out who was last recently on a date
     *
     * @return this player
     */
    public Player compareDates(ArrayList<Player> plList) {
        Player player = plList.get(0);
        for (int i = 1; i <= plList.size() - 1; i++) {
            if (plList.get(i).getLastDate().isAfter(player.getLastDate())) {
                player = plList.get(i);
            }
        }
        return player;

    }

    /**
     * It creates a Player and adds it to the list of joined player
     *
     * @param user User on the server to be added as a player
     */
    public void addPlayer(User user) {
        playerList.add(new Player(user));
    }

    /**
     * It returns how many player already joined
     *
     * @return number of player
     */
    public int getPlayerCount() {
        return playerList.size();
    }

    /**
     * checks if round is over who´s the winner depending on value cards/discarded cards
     *
     * @return winner
     */
    public Player getWinner() {
        return gameWinner;
    }

    /**
     * checks whether some Player already has won the whole game
     * If someone has won, the reset Method from gameController is called which enables the start
     * of a new game.
     *
     * @return winGame
     */
    private boolean gameWon() {

        boolean win = false;
        //you win the game of you have enough token
        for (Player player : playerList) {
            //2 player -> 7 token
            if ((playerList.size() == 2) && (player.getTokenCount() >= 7)) {
                win = true;
                gameWinner = player;
            } //3 player -> 5 token
            else if ((playerList.size() == 3) && (player.getTokenCount() >= 5)) {
                win = true;
                gameWinner = player;
            }  //4 player -> 4 token
            else if ((playerList.size() == 4) && (player.getTokenCount() >= 4)) {
                win = true;
                gameWinner = player;
            }
        }
        return win;
    }

    public Round getActiveRound() {
        return activeRound;
    }

    public void deliverMessage(String message, Player player) {
        gameController.communicate(message, player);
    }

    public void deliverMessageAll(String message) {
        gameController.communicateAll(message);
    }
}



