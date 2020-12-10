package game.gameObjects.cards;

public abstract class Card {

    private String nameOfCard;

    /**
     * Abstract method which is different for every card subclasses.
     * In the subclasses it handles the features of each individual card.
     */
    abstract public void handleCard();

    /**
     * Gets the card name.
     * @return returns the respective name of card
     */
    public String getCardName() {
        return nameOfCard;
    }
}



