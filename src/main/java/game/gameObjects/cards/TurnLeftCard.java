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
public class TurnLeftCard extends ProgrammingCard {
    private static final Logger logger = LogManager.getLogger();

    public TurnLeftCard() {
        super("Left Turn");
        super.addAction(new RotateRobot(Orientation.LEFT));
        drawCardImage();
        logger.info("rotated left");
    }

    @Override
    public ImageView drawCardImage() {
        return ImageHandler.createImageView("/programming-cards/left-turn-card.png");
    }
}
