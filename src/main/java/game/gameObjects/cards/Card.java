package game.gameObjects.cards;

import javafx.scene.image.ImageView;
import utilities.enums.CardType;

public abstract class Card {

    protected CardType card;

    public abstract ImageView drawCardImage();

    public String toString() {
        return card.toString();
    }

    /**
     * Gets the card name.
     *
     * @return returns the respective name of card
     */
    public CardType getName() {
        return card;
    }
}



