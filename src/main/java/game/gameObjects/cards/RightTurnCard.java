package game.gameObjects.cards;

import game.gameActions.RotateRobot;
import javafx.scene.image.ImageView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.ImageHandler;
import utilities.Utilities.Orientation;

/**
 * @author annika
 */
public class RightTurnCard extends ProgrammingCard {
    private static final Logger logger = LogManager.getLogger();

    RightTurnCard() {
        super("Right Turn");
        super.addAction(new RotateRobot(Orientation.RIGHT));
        drawCardImage();
        logger.info("rotated right");
    }

    @Override
    public ImageView drawCardImage() {
        return ImageHandler.createImageView("/programming-cards/right-turn-card.png");
    }
}
