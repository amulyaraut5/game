package game.gameObjects.cards.damage;

import game.Game;
import game.Player;
import game.gameActions.RebootAction;
import game.gameObjects.cards.DamageCard;
import game.gameObjects.tiles.RestartPoint;
import javafx.scene.image.ImageView;
import utilities.ImageHandler;
import utilities.enums.Orientation;
import utilities.enums.CardType;

/**
 * @author annika
 */
public class Worm extends DamageCard {
    private RestartPoint restartPoint;

    public Worm() {
        card = CardType.Worm;
    }

    /**
     * When programming a worm damage card, the player must immediately reboot the robot.
     *
     * @param game
     * @param player
     */
    @Override
    public void handleCard(Game game, Player player) {
        Orientation robotOrientation = player.getRobot().getOrientation();
        new RebootAction().doAction(robotOrientation, player);

        //Add worm card back into the worm deck
        game.getSpamDeck().getDeck().add(this);
    }

    @Override
    public ImageView drawCardImage() {
        return ImageHandler.createImageView("/cards/programming/worm-card.png");
    }

}
