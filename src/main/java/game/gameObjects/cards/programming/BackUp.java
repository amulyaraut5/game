package game.gameObjects.cards.programming;

import game.gameObjects.cards.Card;
import javafx.scene.image.ImageView;
import utilities.ImageHandler;
import utilities.enums.CardType;

public class BackUp extends Card {

    public BackUp() {
        card = CardType.BackUp;
    }

    @Override
    public ImageView drawCardImage() {
        return ImageHandler.createImageView("/cards/programming/BackUp-card.png");
    }
}
