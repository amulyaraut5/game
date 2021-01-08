package game.gameObjects.cards.programming;

import game.gameActions.PowerUpRobot;
import game.gameObjects.cards.ProgrammingCard;
import javafx.scene.image.ImageView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.ImageHandler;
import utilities.Utilities.CardType;

/**
 * @author annika
 */
public class PowerUp extends ProgrammingCard {
    private static final Logger logger = LogManager.getLogger();

    public PowerUp() {
        super(CardType.PowerUp);
        super.addAction(new PowerUpRobot());
        drawCardImage();
        logger.info("took one energy cube");
    }

    @Override
    public ImageView drawCardImage() {
        return ImageHandler.createImageView("/programming-cards/power-up-card.png");
    }
}
