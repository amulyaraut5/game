package game.gameObjects.cards;

import game.gameActions.PowerUpRobot;
import javafx.scene.image.ImageView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.ImageHandler;

/**
 * @author annika
 */
public class PowerUpCard extends ProgrammingCard {
    private static final Logger logger = LogManager.getLogger();

    public PowerUpCard() {
        super("Power Up");
        super.addAction(new PowerUpRobot());
        drawCardImage();
        logger.info("took one energy cube");
    }

    @Override
    public ImageView drawCardImage() {
        return ImageHandler.createImageView("/programming-cards/power-up-card.png");
    }
}
