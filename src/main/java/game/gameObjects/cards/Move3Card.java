package game.gameObjects.cards;

import game.gameActions.MoveRobot;
import javafx.scene.image.ImageView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.ImageHandler;

/**
 * @author annika
 */
public class Move3Card extends ProgrammingCard {
    private static final Logger logger = LogManager.getLogger();

    public Move3Card() {
        super("Move 3");
        super.addAction(new MoveRobot());
        super.addAction(new MoveRobot());
        super.addAction(new MoveRobot());
        drawCardImage();
        logger.info("moved 3 Tiles");
    }

    @Override
    public ImageView drawCardImage() {
        return ImageHandler.createImageView("/programming-cards/move-3-card.png");
    }
}
