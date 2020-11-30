package game;

import card.*;
import server.User;

import java.util.ArrayList;

/**
 * In a newly created game, an instance of the GameBoard class is created in which the
 * deck of cards is created and a winner of the game is determined by creating
 * and running instances of the Round class until one player has won. It also determines the player
 * who begins each round (first date date/ last winner of round)
 *
 * @author sarah,
 */
public class GameBoard extends Thread {
    /**
     * gameController is the related GameController which delivers and translates the users inputs
     * towards the game
     */
    private final GameController gameController;
    /**
     * playerList is a list of current players (max. 4)
     */
    private final ArrayList<Player> playerList = new ArrayList<>();
    /**
     * activeRound is an instance of the current round, e.g. to get the winner of the round
     */
    private Round activeRound;
    /**
     * gameWinner is the winner of the game at the end, who gets declared in the method gameWon()
     */
    private Player gameWinner;
    /**
     * gameWinnerList is necessary to declare the game winner/winners, because there have to be a lot
     * of comparisons in the gameWon() method
     */
    private ArrayList<Player> gameWinnerList = new ArrayList<>();

    /**
     * the constructor which sets the gameController
     *
     * @param gameController
     */
    public GameBoard(GameController gameController) {
        this.gameController = gameController;
    }

    /**
     * It fills a list with all 16 required cards
     *
     * @return created and already filled deck
     */
    private static ArrayList<Card> createDeck() {
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
     * @param user who asked for the information
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

    /**
     * run method to start the Thread and start the game
     */
    @Override
    public void run() {
        playGame();
    }

    /**
     * method to play rounds and declare the winner of each round and
     * increase their token number until someone has won the whole game.
     * In the end it resets the whole gameBoard by calling the reset() method
     * of GameController
     */
    private void playGame() {
        Player firstPlayer = compareDates(playerList);
        ArrayList<Card> deck = createDeck();
        ArrayList<Player> winnerList;
        int i = 0;
        while (!gameWon()) {
            gameController.communicateAll("Round " + ++i + " begins! Good luck;)");
            activeRound = new Round(firstPlayer, new ArrayList<>(deck), new ArrayList<>(playerList), this);
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
    private Player compareDates(ArrayList<Player> plList) {
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
                tieBreakerRound();
                win = true;
                gameWinner = gameWinnerList.get(0);
            }
        }
        return win;
    }

    /**
     * Whenever multiple players have enough tokens in one round to win the game, they play a tiebreaker round
     * until the gameWinner is determined.
     */
    private void tieBreakerRound() {
        Player firstPlayer = compareDates(gameWinnerList);
        ArrayList<Card> deck = createDeck();
        ArrayList<Player> winnerList;
        boolean tieBreak = false;
        while (!tieBreak) {
            gameController.communicateAll("The tie-breaker round begins! Good luck;)");
            activeRound = new Round(firstPlayer, new ArrayList<>(deck), new ArrayList<>(gameWinnerList), this);
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

    /**
     * it returns the active round
     *
     * @return activeRound
     */
    public Round getActiveRound() {
        return activeRound;
    }

    /**
     * Method to send in game messages to every player except one specified player.
     *
     * @param message in game message that should be send
     * @param player  specified player that doesn't receive the message
     */

    public void deliverMessage(String message, Player player) {
        gameController.communicate(message, player);
    }

    /**
     * If the user which disconnected is a player in the playerList, he gets removed from it.
     * If the player is currently in a round, he gets removed from it in activeRound.removePlayer().
     *
     * @param user
     */
    public void removePlayer(User user) {
        for (Player pl : playerList) {
            if (User.isSameUser(pl, user)) {
                playerList.remove(pl);
                if (activeRound != null && activeRound.getActivePlayers().contains(pl)) {
                    activeRound.removePlayer(pl);
                }
                break;
            }
        }
    }
}



