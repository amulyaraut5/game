package game.gameObjects.cards.programming;

import game.gameObjects.cards.ProgrammingCard;
import javafx.scene.image.ImageView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.ImageHandler;
import utilities.Utilities.CardType;

/**
 * @author annika
 */
public class TurnLeft extends ProgrammingCard {
    private static final Logger logger = LogManager.getLogger();

    public TurnLeft() {
        card = CardType.TurnLeft;
//        super.addAction(new RotateRobot(Orientation.LEFT));
//        drawCardImage();
//        logger.info("rotated left");
    }

    @Override
    public ImageView drawCardImage() {
        return ImageHandler.createImageView("/cards/programming/TurnLeft-card.png");
    }
}
