package game;

import java.util.List;
import java.util.Map;

public class Round {
    private List<Player> activePlayers;
    public Map<Player, List<Card>> playedCards;

    public Round(){

    }

    /**
     * Shuffles the deck of Gameboard in each new round.
     */
    public void shuffleDeck(){
    }

    /**
     * Method to remove cards from the deck at the beginning of a round
     * - depending on the number of players.
     * (Rules: Remove top card of the deck without looking at it and place it aside.
     * When playing a 2-player game, take 3 more cards from the top of the deck and place them to the side, face up.)
     */
    public void removeDeckCard(){ //(int stackCards.length, activePlayers.length)
    }

    /**
     * Puts the players in order so the winner of the last round starts.
     * @param lastWinner the winner of the last round
     * @param activePlayers all players of the new round
     */
    public void playerOrder(Player lastWinner, List<Player> activePlayers){

    }

    /**
     * check if the round is finished
     * @return true when round is finished
     */
    public boolean isRoundFinished(){
        return false;
    }


    /**
     * Number of players that are not out yet.
     *  @return number of players that are still in the game
     */
    public int numPlayerStillInRound(){ //Needs inGame-method in Player
        int number = 0;
        return number;
    }

    public void runRound(){

    }










}
