package game.gameObjects.cards;

import game.Game;
import game.Player;
import javafx.scene.image.ImageView;
import utilities.Utilities.CardName;

public abstract class Card {

    private CardName cardName;

    public Card(String cardName) {
        cardName = "Card";
    }

    public abstract ImageView drawCardImage();

    /**
     * Abstract method which is different for every card subclasses.
     * In the subclasses it handles the features of each individual card.
     */
    abstract public void handleCard(Game game, Player player);

    /**
     * Gets the card name.
     * @return returns the respective name of card
     */
    public CardName getCardName() {
        return cardName;
    }

    public String toString() {
        return cardName.toString();
    }
}



