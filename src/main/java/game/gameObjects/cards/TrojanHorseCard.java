package game.gameObjects.cards;

import javafx.scene.image.ImageView;
import utilities.ImageHandler;

public class TrojanHorseCard extends DamageCard{

    public TrojanHorseCard() {
        super("Trojan Horse");
    }

    @Override
    public ImageView drawCardImage() {
        //TODO
        return ImageHandler.createImageView("/programming-cards/move-3-card.png");
    }
}
