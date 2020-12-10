package game.gameObjects.cards;

public class ProgrammingCard extends Card {

    private boolean isHidden;
    private boolean isLocked;

    @Override
    public void handleCard() {
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

}