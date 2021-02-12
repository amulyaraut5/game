package game.gameObjects.cards.programming;

import game.gameObjects.cards.Card;
import javafx.scene.image.ImageView;
import utilities.ImageHandler;
import utilities.enums.CardType;

public class MoveIII extends Card {

    public MoveIII() {
        card = CardType.MoveIII;
    }

    @Override
    public ImageView drawCardImage() {
        return ImageHandler.createImageView("/cards/programming/MoveIII-card.png");
    }
}
