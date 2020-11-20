package game;


import card.Card;

import java.util.*;

public class Round {
    private List<Player> activePlayers;
    public ArrayList<Card> cardDeck;
    private ArrayList<Card> faceUpCards;
    private Card firstCardRemoved = null;

    public Round(Player firstplayer){
        // carddeck should be created here otherwise we would get  error
        //remove() function cannot be called in removeDeckCard
        // secondly it would be better to create a deck and shuffle after each round
        //dealing should be also done here because after creating object of round all the things would be taken care of making, shuffing and dealing
        // delete this message
        cardDeck = GameBoard.createDeck();
        shuffleDeck();
        firstCardRemoved = pop();
        removeThreeMore();
    }

    /**
     * this method is called by the GameBoard to start a round after it is created
     */
    public void play() {

    }

    /**
     * Shuffles the deck of Gameboard in each new round.
     */
    public ArrayList<Card> shuffleDeck(){
        Collections.shuffle(cardDeck);
        return cardDeck;
    }

    /**
     * removes first card of the deck
     * @return poped the removed card
     */
    public Card pop(){
        Card poped = cardDeck.get(0);
        cardDeck.remove(poped);
        return poped;
    }

    /**
     * removes three more cards from the deck, if there are only two players.
     * @return the three removed cards
     */
    public ArrayList<Card> removeThreeMore(){
        faceUpCards = new ArrayList<Card>();
         if(activePlayers.size() == 2){
             for(int i = 0; i<3; i++){
                 faceUpCards.add(pop()); //show?
             }
        }
        return faceUpCards;
    }

    /**
     * shows the three removed cards to the players
     */
    public void sendFaceUpCards(){
        //sendet abgedeckte Karten an alle Spieler
    }

    /**
     * deals out the first cards
     */
    public void dealCards(){
        for (Player player : activePlayers){
            player.setCurrentCard(pop());
        }
    }

    public void drawCards(ArrayList<Player> activePlayers){
        while (!isRoundFinished()){
            for (Player currentPlayer : activePlayers){
                currentPlayer.setSecondCard(pop());
                discardCards(currentPlayer);
            }
        }
    }

    public void discardCards(Player currentPlayer){

    }


    /**
     * Check if the round is finished
     * @return true when round is finished
     */
    public boolean isRoundFinished(){
        if (cardDeck.size() <= 1) return true;      // last card has been drawn (none or one card left in stack/list)
        if (activePlayers.size() < 2) return true;  // one player has won
        return false;
    }


    /**
     * this methods returns the winner of the round
     * @return winner of the round
     */
    public Player getRoundWinner() {
        Player winner = null;
        return winner;
    }
}
