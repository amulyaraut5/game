package game.gameObjects.cards;

import game.gameActions.RotateRobot;
import javafx.scene.image.ImageView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.ImageHandler;
import utilities.Orientation;

/**
 * @author annika
 */
public class UTurnCard extends ProgrammingCard {
    private static final Logger logger = LogManager.getLogger();

    public UTurnCard() {
        super("U-Turn");
        super.addAction(new RotateRobot(Orientation.RIGHT));
        super.addAction(new RotateRobot(Orientation.RIGHT));
        drawCardImage();
        logger.info("performed U-Turn");
    }

    @Override
    public ImageView drawCardImage() {
        return ImageHandler.createImageView("/programming-cards/u-turn-card.png");
    }
}
