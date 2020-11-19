package game;

import cards.Cards;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class Round {
    private List<Player> activePlayers;
    public Map<Player, List<Card>> playedCards;
    public List<Cards> cardDeck; //Remove after getStackCards is created


    public Round(Player firstplayer){
    }


    /**
     * Shuffles the deck of Gameboard in each new round.
     */
    public void shuffleDeck(){ //Maybe just Collections.shuffle(stackCards) in runRound?
        //Objekt und getStackCards?
    }

    /**
     * Method to remove cards from the deck at the beginning of a round
     * - depending on the number of players.
     * (Rules: Remove top card of the deck without looking at it and place it aside.
     * When playing a 2-player game, take 3 more cards from the top of the deck and place them to the side, face up.)
     */
    public void removeDeckCard(List<Player> activePlayers){
    }

    /**
     * Puts the players in order so the winner of the last round starts.
     * @param lastWinner the winner of the last round
     * @param activePlayers all players of the new round
     */
    public void playerOrder(Player lastWinner, List<Player> activePlayers){
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
     * this method is called by the GameBoard to start a round after it is created
     */
    public void play() {
    }

    /**
     * this methods returns the winner of the round
     * @return winner of the round
     */
    public Player getWinner() {
        Player winner = null;
        return winner;
    }
}
