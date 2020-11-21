package game;


import card.Card;

import java.util.ArrayList;
import java.util.Collections;

public class Round {
    public ArrayList<Card> cardDeck; //Remove after getStackCards is created
    private ArrayList<Player> activePlayers;
    private ArrayList<Card> faceUpCards;
    private Card firstCardRemoved = null;
    private Player currentPlayer;
    private GameBoard gameBoard;
    private String first = "";
    private String second = "";

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
    }

    /**
     * this method is called by the GameBoard to start a round after it is created
     */
    public void play() {
        for (Player p : activePlayers) {
            p.setCurrentCard(pop());
        }
        while (!isRoundFinished()) {
            Card playedCard = null;
            System.out.println("It's your turn, " + currentPlayer);
            playedCard = chooseCard();
            handleTurn(playedCard);//handlecard is called
        }
    }

    public synchronized void handleTurn(Card card) {
        card.handlecard(this.currentPlayer);
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
     * deals out the first cards
     */
    public void dealCards() {
        for (Player player : activePlayers) {
            player.setCurrentCard(pop());
        }
    }

    /**
     * current player can choose between a new card or his old card
     */
    public Card chooseCard() {
        Card card = null;
        //TODO choose which card currentPlayer.
        Card secondCard = pop();
        first = currentPlayer.getCard().toString();
        second = secondCard.toString();
        currentPlayer.message(first + "or" + second); //TODO Get both names
        String message = gameBoard.readResponse();
        gameBoard.getSender();
        //TODO check if sender is currentplayer
        //if choosen card is second card, change second card with currentcard
        return card;

    }

    public void discardCards(Player currentPlayer) {
        //remove old handmaid effect
        //currentPlayer.setPlayedHandmaid(false); 
        Card chosenCard = null;
        //if player has countess in hand check for prince or king
        if (first == "Countess" &&
                (second == "King" || second == "Prince")) {
            if (chosenCard == currentPlayer.getSecondcard()) {
                //send Message to player: "You have to choose Countess, please try again."
            }
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
        ArrayList<Player> winnerList = new ArrayList<Player>();
        Player winner = activePlayers.get(0);
        //A round also ends if all players but one are out of the round, in which case the remaining player wins.
        if (activePlayers.size() == 1) {
            winnerList.add(winner);
            return winnerList;
        } else { //A round ends if the deck is empty at the end of a player’s turn
            for (int i = 1; i <= activePlayers.size(); i++) {
                //The player with the highest number in their hand wins the round.
                if (winner.getCard().getCardValue() < activePlayers.get(i).getCard().getCardValue()) {
                    winnerList.get(i); //TODO
                    //In case of a tie, players add the numbers on the cards in their discard pile. The highest total wins.
                } else if (winner.getCard().getCardValue() == activePlayers.get(i).getCard().getCardValue()) {
                    if (winner.getCard().getCardValue() < activePlayers.get(i).getCard().getCardValue()) {
                        winner = activePlayers.get(i);//TODO
                    } else {
                        //
                    }
                }
            }
        }
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
     *
     * @param player last player that played a card.
     */
    public Player nextPlayer(Player player) {
        int temp = this.activePlayers.indexOf(player);
        Player next;
        if (temp < (activePlayers.size() - 1)) {
            next = this.activePlayers.get(temp + 1);
        } else {
            next = this.activePlayers.get(0);
        }
        return next;
    }

    /**
     * this method kicks one player from the game.
     *
     * @param player player that will get kicked.
     */
    public void kickPlayer(Player player) {
        this.activePlayers.remove(player);
    }

    public  ArrayList<Player> getActivePlayers(){
        return this.activePlayers;
    }
}
