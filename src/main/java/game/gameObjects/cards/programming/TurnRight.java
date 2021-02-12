package game.gameObjects.cards.programming;

import game.gameObjects.cards.Card;
import javafx.scene.image.ImageView;
import utilities.ImageHandler;
import utilities.enums.CardType;

public class TurnRight extends Card {

    public TurnRight() {
        card = CardType.TurnRight;
    }

    @Override
    public ImageView drawCardImage() {
        return ImageHandler.createImageView("/cards/programming/TurnRight-card.png");
    }
}
