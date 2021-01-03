package game.gameObjects.cards;

import javafx.scene.image.ImageView;

public class PermUpgradeCard extends UpgradeCard {

    @Override
    public ImageView drawCardImage() {
        return null;
    }

    /**
     * Once you’ve purchased a permanent upgrade, place it on your player mat.
     * In the case of most upgrades, the card’s attributes will apply to your robot for the remainder of the game.
     */
    @Override
    public void handleCard() {

    }

}