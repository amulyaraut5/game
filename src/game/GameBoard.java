package game;

import card.*;
import server.UserThread;

import java.time.LocalDate;
import java.util.ArrayList;

public class GameBoard extends Thread {
    public ArrayList<Player> playerList;
    public ArrayList<Player> activePlayers; //lists players that are still active in the current round
    public ArrayList<Player> orderedPlayers;
    public Player gameWinner;
    public Round activeRound;
    private boolean started = false;
    private GameController gameController = new GameController();
    private ArrayList<Player> winnerList = new ArrayList<>();
    private volatile String userMessage;
    private volatile UserThread sender;

    public GameBoard() {

    }

    public static ArrayList<Card> createDeck() {
        ArrayList<Card> stackCards = new ArrayList<>();
        // every card just one time: princess, countess, king
        stackCards.add(new PrincessCard(8));
        stackCards.add(new CountessCard(7));
        stackCards.add(new KingCard(6));
        //every card two times: prince, handmaid, baron, priest, guard
        for (int i = 0; i < 2; i++) {
            stackCards.add(new PrinceCard(5));
            stackCards.add(new HandmaidCard(4));
            stackCards.add(new BaronCard(3));
            stackCards.add(new PriestCard(2));
        }
        //guard five times:
        for (int i = 0; i < 5; i++) {
            stackCards.add(new GuardCard(1));
        }
        return stackCards;
    }

    public void message(String message, UserThread sender) {
        //TODO differentiate between getter messages (Tokens) or responses to gamelogic
        if (userMessage == null) {
            userMessage = message;
            this.sender = sender;
        }
    }

    public String readMessage() {
        String message;
        while (userMessage == null) {
            //TODO wait();
        }
        message = userMessage;
        userMessage = null;
        return message;
    }

    public UserThread getSender() {
        return sender;
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

            activeRound = new Round(firstPlayer, deck, playerList, this);
            activeRound.play();
            winnerList = activeRound.getRoundWinner();
            for (Player player : winnerList) {
                player.increaseNumOfTokens();
            }
            if (winnerList.size() == 1) {
                firstPlayer = winnerList.get(0);
            } else {
                firstPlayer = compareDates(winnerList);
            }
        }
        gameWinner = firstPlayer;
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

    public void rotatePlayers() {
    }

    /**
     * in list orderedPlayers the Player who´s allowed to play first is on index 0
     *
     * @param plList      is the playerList who needs a new order
     * @param firstplayer is the Player who needs to be on index 0
     */
    public void playerOrder(ArrayList<Player> plList, Player firstplayer) {
        orderedPlayers.add(firstplayer);
        int indexFirstPlayer = plList.indexOf(firstplayer);
        for (int i = indexFirstPlayer++; i < plList.size(); i++) {
            orderedPlayers.add(plList.get(i));
        }
        for (int i = 0; i < indexFirstPlayer; i++) {
            orderedPlayers.add(plList.get(i));
        }
    }

    /**
     * It creates a Player and adds it to the list of joined player
     *
     * @param user     Thread of the user
     * @param username
     */
    public void addUser(UserThread user, String username, LocalDate lastDate) {
        Player player = new Player(user, username, lastDate);
        playerList.add(player);
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
            } //3 player -> 5 token
            else if ((playerList.size() == 3) && (player.getTokenCount() >= 5)) {
                win = true;
            }  //4 player -> 4 token
            else if ((playerList.size() == 4) && (player.getTokenCount() >= 4)) {
                win = true;
            }
        }
        return win;
    }

    /**
     * kicks one player from the current round
     *
     * @param p
     */
    public void kickPlayer(Player p) {

    }
}



