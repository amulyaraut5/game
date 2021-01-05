package game.gameObjects.cards;

import game.gameActions.MoveRobot;
import javafx.scene.image.ImageView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.ImageHandler;

/**
 * @author annika
 */
public class Move1Card extends ProgrammingCard {
    private static final Logger logger = LogManager.getLogger();

    public Move1Card() {
        super("Move 1");
        super.addAction(new MoveRobot());
        drawCardImage();
        logger.info("moved One Tile");
    }

    @Override
    public ImageView drawCardImage() {
        return ImageHandler.createImageView("/programming-cards/move-1-card.png");

    }
}
