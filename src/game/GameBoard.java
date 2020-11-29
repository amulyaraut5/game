package game;

import card.*;
import server.User;

import java.util.ArrayList;

/**
 * In a newly created game, an instance of the GameBoard class is created in which the
 * deck of cards is created and a winner of the game is determined by creating
 * and running instances of the Round class until one player has won.
 *
 * @author sarah,
 */
public class GameBoard extends Thread {
    private final GameController gameController;
    private final ArrayList<Player> playerList = new ArrayList<>();
    private Round activeRound;
    private Player gameWinner;
    private ArrayList<Player> gameWinnerList = new ArrayList<>();


    public GameBoard(GameController gameController) {
        this.gameController = gameController;
    }

    /**
     * It fills a list with all 16 required cards
     *
     * @return created and already filled deck
     */
    public static ArrayList<Card> createDeck() {
        ArrayList<Card> deck = new ArrayList<>();
        // every card just one time: princess, countess, king
        deck.add(new PrincessCard());
        deck.add(new CountessCard());
        deck.add(new KingCard());
        //every card two times: prince, handmaid, baron, priest, guard
        for (int i = 0; i < 2; i++) {
            deck.add(new PrinceCard());
            deck.add(new HandmaidCard());
            deck.add(new BaronCard());
            deck.add(new PriestCard());
        }
        //guard five times:
        for (int i = 0; i < 5; i++) {
            deck.add(new GuardCard());
        }
        return deck;
    }

    /**
     * It returns a String that contains information about the token score of each user
     *
     * @param user
     */
    public void getScorePlayer(User user) {
        StringBuilder score = new StringBuilder();
        score.append("Tokens | Player\n");
        score.append("---------------");
        for (Player pl : playerList) {
            score.append("\n    ").append(pl.getTokenCount()).append("  | ").append(pl);
        }
        user.message(score.toString());
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
        ArrayList<Player> winnerList;
        int i = 0;
        while (!gameWon()) {
            gameController.communicateAll("Round " + ++i + " begins! Good luck;)");
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
                gameController.communicate("The round has ended. Winner of the round: " + firstPlayer.getName(), firstPlayer);
                firstPlayer.message("The round has ended. You won the round! Congratulations!");
                } else {
                firstPlayer = compareDates(winnerList);
                gameController.communicateAll("The round has ended. The winners are: " + winnerList.toString());
                }
        }
        gameController.communicate("Congratulations, " + gameWinner.getName() + " won the game! " +
                "\nType #play to create a new game.", gameWinner);
        gameWinner.message("Congratulations! You won the game! \n Type #play to create a new game.");
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
     * checks whether some Player already has won the whole game
     * If multiple players have enough tokens they play a tiebreaker game.
     * If someone has won, the reset Method from gameController is called which enables the start
     * of a new game.
     *
     * @return winGame
     */
    private boolean gameWon() {

        boolean win = false;
        //you win the game if you have enough token
        for (Player player : playerList) {
            if (playerList.size() == 1) {
                gameWinnerList.add(player);
            }
            //2 player -> 7 token
            else if ((playerList.size() == 2) && (player.getTokenCount() >= 7)) {
                gameWinnerList.add(player);
            } //3 player -> 5 token
            else if ((playerList.size() == 3) && (player.getTokenCount() >= 5)) {
                gameWinnerList.add(player);
            }  //4 player -> 4 token
            else if ((playerList.size() == 4) && (player.getTokenCount() >= 4)) {
                gameWinnerList.add(player);
            }
            if (gameWinnerList.size() == 1) {
                win = true;
                gameWinner = gameWinnerList.get(0);
            } else if (gameWinnerList.size() > 1) {
                //TODO: compare card value
                tieBreakerRound();
                win = true;
                gameWinner = gameWinnerList.get(0);
            }
        }
        return win;
    }

    /**
     * Whenever multiple players have enough tokens in one round to win the game, they play a tiebreaker round
     * to determine the gameWinner.
     */
    public void tieBreakerRound () {
        Player firstPlayer = compareDates(gameWinnerList);
        ArrayList<Card> deck = createDeck();
        ArrayList<Player> winnerList;
        boolean tieBreak = false;
        while (!tieBreak) {
            gameController.communicateAll("The tie-breaker round begins! Good luck;)");
            activeRound = new Round(firstPlayer, new ArrayList(deck), new ArrayList(gameWinnerList), this);
            activeRound.play();
            winnerList = activeRound.getRoundWinner();
            this.activeRound = null;
            for (Player resetPlayers : gameWinnerList) {
                resetPlayers.resetRound();
            }
            if (winnerList.size() == 1) {
               tieBreak = true;
            } else {
                firstPlayer = compareDates(winnerList);
                gameController.communicateAll("The round has ended. The winners are: " + winnerList.toString());
                gameWinnerList = winnerList;
                tieBreakerRound();
            }
        }

    }

    public Round getActiveRound() {
        return activeRound;
    }

    public void deliverMessage(String message, Player player) {
        gameController.communicate(message, player);
    }
}



