package game.gameObjects.cards;

import game.Game;
import game.Player;
import javafx.scene.image.ImageView;
import utilities.ImageHandler;

public class VirusCard extends DamageCard{

    public VirusCard() {
        super("Virus");
    }

    @Override
    public ImageView drawCardImage() {
        return ImageHandler.createImageView("/programming-cards/virus-card.png");
    }

    @Override
    public void handleCard(Game game, Player player) {

    }
}
