package game.gameObjects.cards.damage;

import game.gameObjects.cards.Card;
import javafx.scene.image.ImageView;
import utilities.ImageHandler;
import utilities.enums.CardType;

/**
 * @author annika
 */
public class Virus extends Card {

    public Virus() {
        card = CardType.Virus;
    }

    @Override
    public ImageView drawCardImage() {
        return ImageHandler.createImageView("/cards/programming/Virus-card.png");
    }

}
