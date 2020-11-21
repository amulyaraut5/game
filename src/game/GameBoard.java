package game;

import card.*;
import server.User;
import server.UserThread;

import java.time.LocalDate;
import java.util.ArrayList;

public class GameBoard extends Thread {
    public ArrayList<Player> playerList;
    public Player gameWinner;
    public Round activeRound;
    private boolean started = false;
    private GameController gameController = new GameController();
    private ArrayList<Player> winnerList = new ArrayList<>();
    private volatile String userResponse;
    private volatile User sender;

    public GameBoard() {

    }

    public static ArrayList<Card> createDeck() {
        ArrayList<Card> stackCards = new ArrayList<>();
        // every card just one time: princess, countess, king
        stackCards.add(new PrincessCard("Princess",8));
        stackCards.add(new CountessCard("Countess", 7));
        stackCards.add(new KingCard("King",6));
        //every card two times: prince, handmaid, baron, priest, guard
        for (int i = 0; i < 2; i++) {
            stackCards.add(new PrinceCard("Prince",5));
            stackCards.add(new HandmaidCard("Handmaid", 4));
            stackCards.add(new BaronCard("Baron",2));
            stackCards.add(new PriestCard("Priest",2));
        }
        //guard five times:
        for (int i = 0; i < 5; i++) {
            stackCards.add(new GuardCard("Guard", 1));
        }
        return stackCards;
    }

    public void incomingResponse(String message, User sender) {
        //TODO differentiate between getter messages (Tokens) or responses to gamelogic
        if (userResponse == null) {
            userResponse = message;
            this.sender = sender;
        }else{
            sender.message("It's not your turn"); //TODO turn has to be in Round
        }
    }

    public String readResponse() {
        String message;
        while (userResponse == null) {
            //TODO wait();
        }
        message = userResponse;
        userResponse = null;
        return message;
    }

    /**
     *
     * @param user user who needs to be checked whether he already joined
     * @return if user has already joined
     */
    public boolean playerAlreadyJoined(User user){
        String username = user.getName();
        for(Player pl : playerList){
            if(pl.getName() == username) return true;
        }
        return false;
    }

    public User getSender() {
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
     * It creates a Player and adds it to the list of joined player
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



