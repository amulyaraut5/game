package game.gameObjects.cards;

import javafx.scene.image.ImageView;

public class SpamCard extends DamageCard {

    public SpamCard() {
        super("Spam");
    }


    @Override
    public ImageView drawCardImage() {
        return null;
    }
}
