package game;

import card.Card;
import server.User;

import java.util.ArrayList;

/**
 * For each user who plays the game, a Player object is created.
 * The Player class stores data of the user and player specific data.
 * Player extends from user, thus it is possible to call all methods from user.
 * <p>
 * To send the player a message, use the message(String) method.
 *
 * @author simon
 */
public class Player extends User {

    /**Cards that the player played so far.(In one round) **/
    private final ArrayList<Card> playedCards = new ArrayList<>();
    /**True if player is being protected by Handmaid, false otherwise. **/
    private boolean guarded = false;
    /**Number of tokens the player has.(=Number of rounds won in one game) **/
    private int numOfTokens = 0;
    /**Card the player currently has. **/
    private Card currentCard;

    /**
     * Creates new player from given user.
     * The attributes from user are hand over.
     *
     * @param user user to be added
     */
    public Player(User user) {
        setThread(user.getThread());
        setName(user.getName());
        setLastDate(user.getLastDate());
    }

    /**
     * Getter for the number of tokens for that player.
     *
     * @return Number of tokens.
     */
    public int getTokenCount() {
        return numOfTokens;
    }

    /**
     * Increases the number of tokens for that player.
     *
     * @return Number of tokens after the increase.
     */
    public int increaseNumOfTokens() {
        return ++numOfTokens;
    }

    /**
     * Getter for the current card of player.
     *
     * @return current card
     */
    public Card getCard() {
        return currentCard;
    }

    /**
     * Setter for the current card of player.
     *
     * @param currentCard new card
     */
    public void setCurrentCard(Card currentCard) {
        this.currentCard = currentCard;
    }

    /**
     * Adds up the card values of the discarded cards.
     * @return the added up card values
     */
    public int getSumPlayedCards() {
        int sum = 0;
        for (Card card : playedCards) {
            sum += card.getCardValue();
        }
        return sum;
    }


    /**
     * Gets the discarded cards.
     * @return list of cards played by the player.
     */
    public ArrayList<Card> getPlayedCards() {
        return playedCards;
    }

    /**
     * Gets whether the player is protected by a handmaid card.
     * @return whether the player is protected
     */
    public boolean isGuarded() {
        return guarded;
    }

    /**
     * Sets whether the player is protected by a handmaid card.
     * @param guarded whether the player is protected
     */
    public void setGuarded(boolean guarded) {
        this.guarded = guarded;
    }

    /**
     * Method clears the list of played cards in a round, the player is not guarded anymore.
     */
    public void resetRound() {
        setGuarded(false);
        playedCards.clear();
        setCurrentCard(null);
    }

    @Override
    public synchronized void message(String message) {
        super.message("\033[33m" + message + "\033[0m");
    }
}
