package game.gameObjects.cards.programming;

import game.gameObjects.cards.ProgrammingCard;
import javafx.scene.image.ImageView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.ImageHandler;
import utilities.enums.CardType;

/**
 * @author annika
 */
public class UTurn extends ProgrammingCard {
    private static final Logger logger = LogManager.getLogger();

    public UTurn() {
        card = CardType.UTurn;
//        super.addAction(new RotateRobot(Orientation.RIGHT));
//        super.addAction(new RotateRobot(Orientation.RIGHT));
//        drawCardImage();
//        logger.info("performed U-Turn");
    }

    @Override
    public ImageView drawCardImage() {
        return ImageHandler.createImageView("/cards/programming/UTurn-card.png");
    }
}
