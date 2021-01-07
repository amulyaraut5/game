package game.gameObjects.cards;

import javafx.scene.image.ImageView;
import utilities.ImageHandler;

public class WormCard extends DamageCard{

    public WormCard() {
        super("Worm");
    }

    @Override
    public ImageView drawCardImage() {
        //TODO
        return ImageHandler.createImageView("/programming-cards/move-3-card.png");
    }
}
