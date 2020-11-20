package game;


import card.Card;

import java.util.*;

public class Round {
    private List<Player> activePlayers;
    private Map<Player, List<Card>> playedCards;
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
        shuffleDeck(cardDeck);
        firstCardRemoved = pop();
        removeThreeMore(cardDeck);
    }

    /**
     * this method is called by the GameBoard to start a round after it is created
     */
    public void play() {


    }

    /**
     * Shuffles the deck of Gameboard in each new round.
     */
    public ArrayList<Card> shuffleDeck(ArrayList<Card> cardDeck){
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
    public ArrayList<Card> removeThreeMore(ArrayList<Card> cardDeck){
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

    public void dealCards(){

    }

    public void drawCards(){

    }

    public void discardCards(){

    }

    /**
     * Number of players that are not out yet.
     *  @return number of players that are still in the game
     */
    public int numPlayerStillInRound(){ //Needs inGame-method in Player
        int number = 0;
        return number;
    }

    /**
     * Check if the round is finished
     * @return true when round is finished
     */
    public boolean isRoundFinished(){
        if (cardDeck.size() <= 1) return true;         // last card has been drawn (none or one card left in stack/list)
        if (numPlayerStillInRound() < 2) return true; // one player has won
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
