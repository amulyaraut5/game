package game.gameObjects.cards;

import javafx.scene.image.ImageView;
import utilities.ImageHandler;

/**
 * @author annika
 */
public class SpamCard extends DamageCard {

    public SpamCard() {
        super("Spam");
    }


    @Override
    public ImageView drawCardImage() {
        //TODO
        return ImageHandler.createImageView("/programming-cards/move-3-card.png");
    }
}
