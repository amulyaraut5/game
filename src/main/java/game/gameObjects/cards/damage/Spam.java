package game.gameObjects.cards.damage;

import game.Game;
import game.Player;
import game.gameObjects.cards.Card;
import game.gameObjects.cards.DamageCard;
import javafx.scene.image.ImageView;
import utilities.ImageHandler;
import utilities.Utilities.CardType;

/**
 * @author annika
 */
public class Spam extends DamageCard {

    public Spam() {
        super(CardType.Spam);
    }

    /**
     * Handels the action of spam card.
     * The Player has to play the top card of the programming deck in the current register.
     */
    @Override
    public void handleCard(Game game, Player player) {
        //Add spam card back into the spam deck
        game.getSpamDeck().getDeck().add(this);
        //remove top card from programming deck
        Card topCard = game.getProgrammingDeck().pop();

        //exchange spam card and new programming card in the current register
        int spamIndex = player.getRegister().indexOf(this);
        player.getRegister().remove(this);
        player.getRegister().set(spamIndex, topCard);

        //Play the new register card
        topCard.handleCard(game, player);
    }

    @Override
    public ImageView drawCardImage() {
        return ImageHandler.createImageView("/programming-cards/spam-card.png");
    }

}
