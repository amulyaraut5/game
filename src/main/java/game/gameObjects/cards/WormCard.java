package game.gameObjects.cards;

import game.Game;
import game.Player;
import game.gameObjects.tiles.Reboot;
import game.round.Round;
import javafx.scene.image.ImageView;
import utilities.Coordinate;
import utilities.ImageHandler;

/**
 * @author annika
 */
public class WormCard extends DamageCard{
    private Reboot reboot;

    public WormCard() {
        super("Worm");
    }

    @Override
    public void handleCard(Game game, Player player) {
        //Draw two spam cards
        for (int i = 0; i < 2; i++) {
            Card spamCard = game.getSpamDeck().pop();
            player.getDiscardedProgrammingDeck().getDeck().add(spamCard);
        }
        //discard cards in registers on discard pile
        player.getRegister().addAll(player.getDiscardedProgrammingDeck().getDeck());
        player.getRegister().clear();
        //Out of the round, must wait until the next round to program the robot again.
        //game.getActiveRound().getPlayerList().remove(player);
        player.freeze();
        //Robot is placed on reboot token
        Coordinate rebootTile = reboot.getPosition(); //?
        player.getRobot().setPosition(rebootTile);
        //TODO The player can turn the robot to face any direction.
    }

    @Override
    public ImageView drawCardImage() {
        return ImageHandler.createImageView("/programming-cards/worm-card.png");
    }

}
