package game.gameObjects.cards.damage;

import game.Game;
import game.Player;
import game.gameObjects.cards.DamageCard;
import javafx.scene.image.ImageView;
import utilities.ImageHandler;
import utilities.Utilities.CardType;

public class Wurm extends DamageCard {

    public Wurm() {
        super(CardType.Wurm);
    }

    @Override
    public ImageView drawCardImage() {
        return ImageHandler.createImageView("/programming-cards/worm-card.png");
    }

    @Override
    public void handleCard(Game game, Player player) {

    }
}
