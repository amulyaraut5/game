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

    Move3Card() {
        super("Move 3");
        super.addAction(new MoveRobot());
        super.addAction(new MoveRobot());
        super.addAction(new MoveRobot());
        //load image
        drawCardImage();
        //ODER:
        /*
        try {
            super.cardImage = ImageIO.read(new File("programming-cards/move-3-card.png"));
        } catch (java.io.IOException | NullPointerException e){
            logger.warn("Move 3 card image could not be read");
        }
         */
        logger.info("moved 3 Tiles");
    }

    @Override
    public ImageView drawCardImage() {
        return ImageHandler.createImageView("/programming-cards/move-3-card.png");
    }
}
