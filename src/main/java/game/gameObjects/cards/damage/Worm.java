package game.gameObjects.cards.damage;

import game.gameObjects.cards.DamageCard;
import javafx.scene.image.ImageView;
import utilities.ImageHandler;
import utilities.enums.CardType;

/**
 * @author annika
 */
public class Worm extends DamageCard {

    public Worm() {
        card = CardType.Worm;
    }

    @Override
    public ImageView drawCardImage() {
        return ImageHandler.createImageView("/cards/programming/Worm-card.png");
    }
}
