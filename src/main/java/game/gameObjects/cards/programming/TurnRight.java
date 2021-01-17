package game.gameObjects.cards.programming;

import game.gameActions.RotateRobot;
import game.gameObjects.cards.ProgrammingCard;
import javafx.scene.image.ImageView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.ImageHandler;
import utilities.Orientation;
import utilities.Utilities.CardType;

/**
 * @author annika
 */
public class TurnRight extends ProgrammingCard {
    private static final Logger logger = LogManager.getLogger();

    public TurnRight() {
        super(CardType.TurnRight);
        super.addAction(new RotateRobot(Orientation.RIGHT));
        drawCardImage();
        logger.info("rotated right");
    }

    @Override
    public ImageView drawCardImage() {
        return ImageHandler.createImageView("/cards/programming/TurnRight-card.png");
    }
}
