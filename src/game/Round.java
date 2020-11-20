package game;


import card.Card;

import java.util.*;

public class Round {
    private List<Player> activePlayers;
    private Map<Player, List<Card>> playedCards;
    public Stack <Card> cardDeck;
    private Stack <Card> faceUpCards;
    private Card firstCardRemoved = null;

    public Round(Player firstplayer, Stack<Card> deck, ArrayList<Player> activePlayers){
        // carddeck should be created here otherwise we would get  error
        //remove() function cannot be called in removeDeckCard
        // secondly it would be better to create a deck and shuffle after each round
        //dealing should be also done here because after creating object of round all the things would be taken care of making, shuffing and dealing
        // delete this message
        this.cardDeck = deck;
        this.activePlayers=activePlayers;
        shuffleDeck(cardDeck);
        firstCardRemoved = pop();
        if(activePlayers.size()==2){
            removeThreeMore(cardDeck);
        }

    }

    /**
     * this method is called by the GameBoard to start a round after it is created
     */
    public void play() {


    }

    /**
     * Shuffles the deck of Gameboard in each new round.
     */
    public Stack<Card> shuffleDeck(Stack<Card> cardDeck){
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
     * @param cardDeck the created cardeck
     * @return the three removed cards
     */
    public Stack<Card> removeThreeMore(Stack<Card> cardDeck){
        faceUpCards = new Stack<Card>();
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

    public void drawCards(){

    }

    public void discardCards(){

    }



    /**
     * Check if the round is finished
     * @return true when round is finished
     */
    public boolean isRoundFinished(){
        //A round ends if the deck is empty at the end of a player’s turn
        if (cardDeck.empty()) return true;
        //A round also ends if all players but one are out of the round, in which case the remaining player wins
        if (activePlayers.size() < 2) return true; // one player has won
        return false;
    }
/*
    Player setWinner(){
        Player winner = activePlayerList.get(0);
        for (int i = 1; i<= activePlayerList.size(); i++) {
            if (winner.getSumValue() < activePlayerList.get(i).getSumValue()) {
                winner = activePlayerList.get(i);
            } else if (winner.getSumValue() == activePlayerList.get(i).getSumValue()) {
                if (winner.getSumDiscarded() < activePlayerList.get(i).getSumDiscarded()) {
                    winner = activePlayerList.get(i);

                } else {
                    //
                }
            }
        }
        return winner;
    }*/
    /**
     * this methods returns the winner of the round
     * @return winner of the round
     */
    public Player getRoundWinner() {
        Player winner = null;
        return winner;
    }
}
