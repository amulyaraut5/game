package game.gameObjects.cards.damage;

import game.Game;
import game.Player;
import game.gameObjects.cards.Card;
import game.gameObjects.cards.DamageCard;
import javafx.scene.image.ImageView;
import utilities.ImageHandler;
import utilities.enums.CardType;

/**
 * @author annika
 */
public class Spam extends DamageCard {

    public Spam() {
        card = CardType.Spam;
    }

    @Override
    public ImageView drawCardImage() {
        return ImageHandler.createImageView("/cards/programming/Spam-card.png");
    }

}
