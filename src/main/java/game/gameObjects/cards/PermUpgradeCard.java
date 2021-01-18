package game.gameObjects.cards;

import game.Game;
import game.Player;
import javafx.scene.image.ImageView;

public abstract class PermUpgradeCard extends UpgradeCard {

    public PermUpgradeCard() {
    }

    @Override
    public ImageView drawCardImage() {
        return null;
    }

    /**
     * Once you’ve purchased a permanent upgrade, place it on your player mat.
     * In the case of most upgrades, the card’s attributes will apply to your robot for the remainder of the game.
     */
    @Override
    public void handleCard(Game game, Player player) {
        super.handleCard(game, player);
    }
}