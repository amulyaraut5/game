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

    /**
     * Handels the action of spam card.
     * The Player has to play the top card of the programming deck in the current register.
     */
    @Override
    public void handleCard(Game game, Player player) {
    }

    @Override
    public ImageView drawCardImage() {
        return ImageHandler.createImageView("/cards/programming/Spam-card.png");
    }

}
