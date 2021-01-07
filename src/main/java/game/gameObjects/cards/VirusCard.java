package game.gameObjects.cards;

import javafx.scene.image.ImageView;
import utilities.ImageHandler;

public class VirusCard extends DamageCard{

    public VirusCard() {
        super("Virus");
    }

    @Override
    public ImageView drawCardImage() {
        //TODO
        return ImageHandler.createImageView("/programming-cards/move-3-card.png");
    }
}
