package game.gameObjects.cards.programming;

import game.gameObjects.cards.Card;
import javafx.scene.image.ImageView;
import utilities.ImageHandler;
import utilities.enums.CardType;

public class TurnLeft extends Card {

    public TurnLeft() {
        card = CardType.TurnLeft;
    }

    @Override
    public ImageView drawCardImage() {
        return ImageHandler.createImageView("/cards/programming/TurnLeft-card.png");
    }
}
