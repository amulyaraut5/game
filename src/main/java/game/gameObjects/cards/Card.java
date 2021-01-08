package game.gameObjects.cards;

import game.Game;
import game.Player;
import javafx.scene.image.ImageView;
import utilities.Utilities.CardType;

public abstract class Card {

    private CardType card;

    public Card(CardType card) {
        this.card = card;
    }

    public abstract ImageView drawCardImage();

    /**
     * Abstract method which is different for every card subclasses.
     * In the subclasses it handles the features of each individual card.
     */
    abstract public void handleCard(Game game, Player player);

    /**
     * Gets the card name.
     *
     * @return returns the respective name of card
     */
    public CardType getName() {
        return card;
    }

    public String toString() {
        return card.toString();
    }
}



