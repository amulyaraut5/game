package game;

import card.Card;
import server.User;

/**
 * For each user who plays the game, a Player object is created.
 * The Player class stores data of the user and player specific data.
 * Player extends from user, thus it is possible to call all methods from user.
 * <p>
 * To send the player a message, use the message(String) method.
 */
public class Player extends User {
    public boolean inGame; // TODO used in card subclass
    public boolean isGuarded; // TODO used in card subclass
    private int numOfTokens;
    private Card currentCard;
    private Card secondCard;

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

        numOfTokens = 0;
    }

    /**
     * Getter for the number of tokens for that player.
     *
     * @return Number of tokens.
     */
    public int getTokenCount() {
        return 0;
    }
    /**
     * Resets the number of tokens for that player to '0'.
     */
    public void resetNumOfTokens() {
        numOfTokens = 0;
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
     * Getter for the current card of player
     * @return current card
     */
    public Card getCard() {
        return currentCard;
    }

    /**
     * Setter for the current card of player
     * @param currentCard new current card
     */
    public void setCurrentCard(Card currentCard) {
        this.currentCard = currentCard;
    }
    /**
     * Getter for the second card of player
     * @return second card
     */
    public Card getSecondcard() {
        return secondCard;
    }

    /**
     * Setter for the second card of player
     * @param secondCard new second card
     */
    public void setSecondCard(Card secondCard) {
        this.secondCard = secondCard;
    }

    //TODO required in card subclasses
    public void setInGame(boolean b) {
    }
}
