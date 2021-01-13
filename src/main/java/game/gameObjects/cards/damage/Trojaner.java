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
public class Trojaner extends DamageCard {

    public Trojaner() {
        super(CardType.Trojaner);
    }

    /**
     * Handels the action of trojan horse card.
     * The Player has to play the top card of the programming deck and draws two spam cards.
     */
    @Override
    public void handleCard(Game game, Player player) {
        game.getSpamDeck().getDeck().add(this);
        Card topCard = player.getDrawProgrammingDeck().pop();

        //exchange spam card and new programming card in the current register
        int spamIndex = player.getRegisterCards().indexOf(this);
        player.getRegisterCards().remove(this);
        player.getRegisterCards().set(spamIndex, topCard);

        //Draw two spam cards
        for (int i = 0; i < 2; i++) {
            Card spamCard = game.getSpamDeck().pop();
            player.getDiscardedProgrammingDeck().getDeck().add(spamCard);
        }
        //Play the new register card
        topCard.handleCard(game, player);
    }

    @Override
    public ImageView drawCardImage() {
        return ImageHandler.createImageView("/programming-cards/trojanhorse-card.png");
    }

}
