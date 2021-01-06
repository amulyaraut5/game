package game.round;

import game.Game;
import game.Player;
import game.gameObjects.cards.Card;
import game.gameObjects.decks.ProgrammingDeck;

import java.util.ArrayList;
import java.util.Map;

public class ProgrammingPhase  {

    private ArrayList<Player> playerList;
    /**
     * timerIsRunning will get true if a player creates an instance of timer
     */
    private boolean timerIsRunning = false;

    /**
     * a player gets removed if he has already chose 5 cards in the time
     */
    private ArrayList<Player> notReadyPlayers = new ArrayList<>();

    /**
     * Programming cards from which the player can choose to program his robot.
     */
    private ArrayList<Card> availableProgrammingCards;

    public ProgrammingPhase(Round round) {
        this.playerList = round.getPlayerList();
    }

    public void startProgrammingPhase() {
        dealProgrammingCards();
    }

    /**
     * every player can look at their programming cards
     */
    private void showCards() {

    }

    /**
     * players get their  cards for programming their robot in this round.
     * If the draw pile has at least 9 cards, the top 9 cards get dealt.
     * If there are less cards, the remaining cards get dealt.
     * the discarded pile is reshuffled and used as the draw pile and the rest of the 9 cards is drawn from
     * the new draw Deck.
     */
    private void dealProgrammingCards() {
        for (Player player : playerList) {
            ProgrammingDeck currentDeck = player.getDrawProgrammingDeck();
            if (currentDeck.size() >= 9) {
                availableProgrammingCards = currentDeck.drawCards(9);
            } else {
                availableProgrammingCards = currentDeck.drawCards(currentDeck.size());
                player.reuseDiscardedDeck();
                availableProgrammingCards.addAll(player.getDrawProgrammingDeck().drawCards(9- currentDeck.size()));
                }
            }
        }

    /**
     * a player can choose 5 cards and then a timer starts.
     * TODO
     */
    private void setRegisterCards(Player player, Map<Integer, Card> mapCards) {
        player.setRegisterAndCards(mapCards);
        if (!timerIsRunning) {
            timerIsRunning = true;
            GameTimer timer = new GameTimer(player);
        }
    }

    /**
     * a method which handles the players who didn´t choose cards in time
     */
    private void timeRunOut() {
        // random set the programming cards of players´ deck to the registerAndCard Map
    }

    /**
     * this method gets called after every Round to reset the attributes
     */
    private void resetProgrammingPhase() {
        timerIsRunning = false;
        notReadyPlayers = null;
    }

}