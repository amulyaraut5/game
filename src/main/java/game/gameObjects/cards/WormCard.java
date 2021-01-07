package game.gameObjects.cards;

import game.Game;
import game.Player;
import javafx.scene.image.ImageView;
import utilities.ImageHandler;

public class WormCard extends DamageCard{

    public WormCard() {
        super("Worm");
    }

    @Override
    public ImageView drawCardImage() {
        return ImageHandler.createImageView("/programming-cards/worm-card.png");
    }

    @Override
    public void handleCard(Game game, Player player) {

    }
}
