package game;


import card.Card;
import server.User;

import java.util.ArrayList;
import java.util.Collections;

public class Round {
    private GameBoard gameBoard;

    private ArrayList<Card> cardDeck; //Remove after getStackCards is created
    private ArrayList<Player> activePlayers;
    private ArrayList<Card> faceUpCards;

    private Card firstCardRemoved = null;
    private Player currentPlayer;

    private String first = "";//TODO save strings in method chooseCard()
    private String second = "";

    private volatile String userResponse;
    private volatile User sender;

    public Round(Player firstPlayer, ArrayList<Card> deck, ArrayList<Player> activePlayers, GameBoard gameBoard) {
        //remove() function cannot be called in removeDeckCard
        //dealing should be also done here because after creating object of round all the things would be taken care of making, shuffing and dealing
        // delete this message
        this.currentPlayer = firstPlayer;
        this.cardDeck = deck;
        shuffleDeck();
        this.activePlayers = activePlayers;
        this.gameBoard = gameBoard;
        removeFirstCards();

        Card.setRound(this);
    }

    /**
     * this method executes one round
     */
    public void play() {
        for (Player p : activePlayers) {
            p.setCurrentCard(pop());
        }
        while (!isRoundFinished()) {
            Card playedCard = null;
            currentPlayer.message("It's your turn, " + currentPlayer + "!");
            //Draw card before calling choosecard, to not draw the card multiple times(in case choosecard gets called multiple times)
            Card secondCard = pop();
            playedCard = chooseCard(secondCard);
            while (playedCard == null) {
                playedCard = chooseCard(secondCard);
            }
            currentPlayer.message("You chose " + playedCard.getCardName() + "!");
            handleTurn(playedCard);
        }

    }

    /**
     * Actual turn gets handled.
     *
     * @param card Card that the player chose to play.
     */
    public synchronized void handleTurn(Card card) {
        card.setRound(this);
        card.handleCard(this.currentPlayer);
        nextPlayer();
    }

    /**
     * Gives Messages to the Game-Thread to read.
     * Method should be called if UserThreads get messages from clients,
     * which have to be passed to the GameBoard-Thread.
     * If an incoming response is set, no other responses can be set,
     * until the response is read by the GameBoard-Thread.
     * To read the response in the GameBoard-Thread, readResponse() should be called.
     *
     * @param message response of the player
     * @param sender  User who replied
     */
    public  void writeResponse(String message, User sender) {
        if (User.isSameUser(sender, currentPlayer)) {
            if (userResponse == null) {
                userResponse = message;
                this.sender = sender;
            } else {
                sender.message("You were too fast. The message has not yet been read. Please try again:");
            }
        } else {
            sender.message("It's not your turn, " + sender + "!");
        }
    }

    /**
     * The methods reads the response, which is send from a player.
     * Warning: The method waits and ends, if there is a feedback from the user.
     * if no client responds, the method does not return!
     * The method is interruptible
     *
     * @return response message of the player
     */
    public String readResponse() {
        String message;
        while (userResponse == null && !gameBoard.isInterrupted()) {
            try {
                gameBoard.sleep(50);
            } catch (InterruptedException e) {
            }
        }
        message = userResponse;
        userResponse = null;
        return message;
    }


    /**
     * Shuffles the deck of Gameboard in each new round.
     */
    public ArrayList<Card> shuffleDeck() {
        Collections.shuffle(cardDeck);
        return cardDeck;
    }

    /**
     * removes first card of the deck
     *
     * @return poped the removed card
     */
    public Card pop() {
        Card poped = cardDeck.get(0);
        cardDeck.remove(poped);
        return poped;
    }

    /**
     * removes one card from  the deck and
     * removes three more cards from the deck, if there are only two players and add them to faceUpCards
     *
     * @return the three removed cards
     */
    public ArrayList<Card> removeFirstCards() {
        firstCardRemoved = pop();
        faceUpCards = new ArrayList<Card>();
        if (activePlayers.size() == 2) {
            for (int i = 0; i < 3; i++) {
                faceUpCards.add(pop()); //show?
            }
        }
        return faceUpCards;
    }

    /**
     * shows the three removed cards to the players
     */
    public void sendFaceUpCards() {
        //sendet abgedeckte Karten an alle Spieler
    }

    /**
     * current player can choose between a new card or his old card
     */
    public Card chooseCard(Card secondCard) {
        Card card = null;
        //Card secondCard = pop();
        String first = currentPlayer.getCard().getCardName();
        String second = secondCard.getCardName();
        currentPlayer.message("Which card do you want to play?");
        currentPlayer.message("Type '#choose 1' for " + first + " and '#choose 2' for " + second + ":");
        gameBoard.deliverMessage("It´s " + currentPlayer + "'s turn!", currentPlayer);
        String message = readResponse();
        //TODO change currentCard of active Player
        boolean mustCountess = checkCountess(currentPlayer.getCard(), secondCard);
        if (!User.isSameUser(sender, currentPlayer)) {//TODO move to write Response
            card = null;
            sender.message("Please wait for your turn!");
        }
        if (message.equals("1")) {
            card = currentPlayer.getCard();
            System.out.println(card.getCardName());
            //if (mustCountess && (card.getCardName() != "Countess")) {
            //    currentPlayer.message("You have to play the Countess. Please try again!");
            //    return null;
            //}
            currentPlayer.setCurrentCard(secondCard);
        } else if (message.equals("2")) {
            card = secondCard;
            System.out.println(card.getCardName());
            //if (mustCountess && (card.getCardName() != "Countess")) {
            //    currentPlayer.message("You have to play the Countess. Please try again!");
            //    return null;
            //}
        } else {
            currentPlayer.message("Wrong Input. Please choose card 1 or 2:");
        }
        return card;

    }

    public boolean checkCountess(Card card1, Card card2) {
        if (first == "Countess" &&
                (second == "King" || second == "Prince")) {
            return true;

        }
        if (second == "Countess" &&
                (first == "King" || first == "Prince")) {
            return true;
        }
        return false;
    }

    public void discardCards(Player currentPlayer) {
        //remove old handmaid effect
        currentPlayer.setGuarded(false);
        Card chosenCard = null;
        //if player has countess in hand check for prince or king
        if (first == "Countess" &&
                (second == "King" || second == "Prince")) {
            // if (chosenCard == currentPlayer.getSecondcard()) {
            //send Message to player: "You have to choose Countess, please try again."
            //}
        } else if (second == "Countess" &&
                (first == "King" || first == "Prince")) {
            if (chosenCard == currentPlayer.getCard()) {
                //send Message to player: "You have to choose Countess, please try again."
            }
        }
        if (chosenCard == null) {
            //chosenCard = currentPlayer.chooseCardtoPlay();
            //handleCard(chosenCard);
        }
    }

    /**
     * Check if the round is finished
     *
     * @return true when round is finished
     */
    public boolean isRoundFinished() {
        if (cardDeck.isEmpty()) return true;//round ends if deck is empty at the end of a turn
        //A round also ends if all players but one are out of the round, in which case the remaining player wins
        if (activePlayers.size() < 2) return true; // one player has won
        return false;
    }

    /**
     * this methods returns the winner of the round
     *
     * @return winner of the round
     */
    public ArrayList<Player> getRoundWinner() {
        ArrayList<Player> winnerList = new ArrayList<>();
        ArrayList<Player> waitList = new ArrayList<>();
        Player winner = activePlayers.get(0);
        //A round also ends if all players but one are out of the round, in which case the remaining player wins.
        if (activePlayers.size() == 1) {
            winnerList.add(winner);
            return winnerList;
        } else { //A round ends if the deck is empty at the end of a player’s turn
            for (int i = 1; i < activePlayers.size(); i++) {
                Player currentPlayer = activePlayers.get(i);
                if (winner.getCard().getCardValue() < currentPlayer.getCard().getCardValue()) winner = currentPlayer;
                else if (winner.getCard().getCardValue() == currentPlayer.getCard().getCardValue()) {
                    if (winner.getSumPlayedCards() < currentPlayer.getSumPlayedCards()) winner = currentPlayer;
                    else if (winner.getSumPlayedCards() == currentPlayer.getSumPlayedCards())
                        waitList.add(currentPlayer);
                }
            }
        }
        for (Player player : waitList) {
            if (winner.getCard().getCardValue() < player.getCard().getCardValue()) winner = player;
            else if (winner.getCard().getCardValue() == player.getCard().getCardValue()) {
                if (winner.getSumPlayedCards() < player.getSumPlayedCards()) winner = player;
                else if (winner.getSumPlayedCards() == player.getSumPlayedCards()) winnerList.add(player);
            }
        }
        winnerList.add(winner);
        return winnerList;
    }

    /**
     * this method adds another player to the game.
     *
     * @param player player that's supposed to be added.
     */
    public void addPlayer(Player player) {
        if (activePlayers.size() < 4) {
            activePlayers.add(player);
        } else {
            //send message to player: "The game is full, sorry!"
        }
    }

    /**
     * this method changes the currentPlayer attribute(which determines which player's turn it is).
     */
    public void nextPlayer() {
        int temp = this.activePlayers.indexOf(this.currentPlayer);
        if (temp < (activePlayers.size() - 1)) {
            currentPlayer = activePlayers.get(temp + 1);
        } else {
            currentPlayer = activePlayers.get(0);
        }
    }

    /**
     * this method kicks one player from the game.
     *
     * @param player player that will get kicked.
     */
    public void kickPlayer(Player player) {
        this.activePlayers.remove(player);
    }

    public ArrayList<Player> getActivePlayers() {
        return this.activePlayers;
    }

    public User getSender() {
        return sender;
    }
}
