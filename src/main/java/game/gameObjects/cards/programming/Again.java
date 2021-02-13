package game.gameObjects.cards.programming;

import game.gameObjects.cards.Card;
import javafx.scene.image.ImageView;
import utilities.ImageHandler;
import utilities.enums.CardType;

public class Again extends Card {

    public Again() {
        card = CardType.Again;
    }

    @Override
    public ImageView drawCardImage() {
        return ImageHandler.createImageView("/cards/programming/Again-card.png");
    }
}
