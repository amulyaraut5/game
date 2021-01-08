package game.gameObjects.cards;

import game.Game;
import game.Player;
import game.gameActions.Action;
import utilities.Utilities.CardType;

import java.util.ArrayList;


/**
 * @author annika
 */
public abstract class ProgrammingCard extends Card {

    private boolean isHidden;
    private boolean isLocked;

    private ArrayList<Action> actions = new ArrayList<>();

    public ProgrammingCard(CardType card) {
        super(card);
        this.isHidden = false;
        this.isLocked = false;
    }

    @Override
    public void handleCard(Game game, Player player) {

    }

    public void addAction(Action action) {
        this.actions.add(action);
    }

    /**
     * Place any programming cards still in your hand in the discard pile on your player mat.
     */
    public void placeInDiscardPile() {
    }

    /**
     * Returns true if the card is hidden.
     * This is done at the beginning of the round to
     * slowly reveal the cards as the turn progresses.
     *
     * @return True if the card is in hidden state, false if not.
     */
    public boolean isHidden() {
        return isHidden;
    }

    /**
     * Sets the hidden state of the card.
     *
     * @param hidden True to mark the card as hidden, false to reveal it.
     */
    public void setHidden(boolean hidden) {
        isHidden = hidden;
    }

    /**
     * Returns true if the card is locked in the register.
     *
     * @return True if the card is in locked state, false if not.
     */
    public boolean isLocked() {
        return isLocked;
    }

    /**
     * Sets the locked state of the card.
     *
     * @param locked True to mark the card as locked, false to unlock it.
     */
    public void setLocked(boolean locked) {
        isLocked = locked;
    }


}