package game.gameObjects.cards;

import game.gameActions.Action;
import javafx.scene.image.ImageView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.awt.image.BufferedImage;

/**
 * @author annika
 */
public abstract class ProgrammingCard extends Card {
    /**
     * Logger to log information/warning
     */
    private static final Logger logger = LogManager.getLogger();

    private boolean isHidden;
    private boolean isLocked;
    private String cardName;
    //BufferedImage cardImage;
    private ArrayList<Action> actions = new ArrayList<>();

    ProgrammingCard(String cardName) {
        this.isHidden = false;
        this.isLocked = false;
        this.cardName = cardName;
    }

    @Override
    public void handleCard() {
    }

    public void addAction(Action action) {
        this.actions.add(action);
    }

    /**
     * Place any programming cards still in your hand in the discard pile on your player mat.
     */
    public void placeInDiscardPile(){
    }
    
    /**
     * Returns true if the card is hidden.
     * This is done at the beginning of the round to
     * slowly reveal the cards as the turn progresses.
     * @return True if the card is in hidden state, false if not.
     */
    public boolean isHidden() {
        return isHidden;
    }

    /**
     * Returns true if the card is locked in the register.
     * @return True if the card is in locked state, false if not.
     */
    public boolean isLocked() {
        return isLocked;
    }

    /**
     * Sets the hidden state of the card.
     * @param hidden True to mark the card as hidden, false to reveal it.
     */
    public void setHidden(boolean hidden) {
        isHidden = hidden;
    }

    /**
     * Sets the locked state of the card.
     * @param locked True to mark the card as locked, false to unlock it.
     */
    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public String toString() {
        return cardName;
    }

}