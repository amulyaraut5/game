package game.gameObjects.cards.damage;

import game.gameObjects.cards.DamageCard;
import javafx.scene.image.ImageView;
import utilities.ImageHandler;
import utilities.enums.CardType;

/**
 * @author annika
 */
public class Trojan extends DamageCard {

    public Trojan() {
        card = CardType.Trojan;
    }

    @Override
    public ImageView drawCardImage() {
        return ImageHandler.createImageView("/cards/programming/trojanhorse-card.png");
    }

}
