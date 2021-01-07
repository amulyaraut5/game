package game.gameObjects.cards;

import game.Game;
import game.Player;
import game.gameObjects.decks.SpamDeck;
import javafx.scene.image.ImageView;
import utilities.ImageHandler;

/**
 * @author annika
 */
public class SpamCard extends DamageCard {

    public SpamCard() {
        super("Spam");
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
