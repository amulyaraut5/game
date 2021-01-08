package game.gameObjects.cards.damage;

import game.Game;
import game.Player;
import game.gameObjects.cards.DamageCard;
import javafx.scene.image.ImageView;
import utilities.ImageHandler;
import utilities.Utilities.CardType;

public class Virus extends DamageCard {

    public Virus() {
        super(CardType.Virus);
    }

    @Override
    public ImageView drawCardImage() {
        return ImageHandler.createImageView("/programming-cards/virus-card.png");
    }

    @Override
    public void handleCard(Game game, Player player) {

    }
}
