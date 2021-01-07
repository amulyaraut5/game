package game.gameObjects.cards;

import game.Game;
import game.Player;
import javafx.scene.image.ImageView;
import utilities.ImageHandler;

public class TrojanHorseCard extends DamageCard{

    public TrojanHorseCard() {
        super("Trojan Horse");
    }

    /**
     * Handels the action of trojan horse card.
     * The Player has to play the top card of the programming deck and draws two spam cards.
     */
    @Override
    public void handleCard(Game game, Player player) {
        game.getSpamDeck().getDeck().add(this);
        Card topCard = game.getProgrammingDeck().pop();

        //exchange spam card and new programming card in the current register
        int spamIndex = player.getRegister().indexOf(this);
        player.getRegister().remove(this);
        player.getRegister().set(spamIndex, topCard);

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
