package game.gameObjects.cards;

import game.gameActions.MoveRobot;
import javafx.scene.image.ImageView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.ImageHandler;

/**
 * @author annika
 */
public class Move2Card extends ProgrammingCard {
    private static final Logger logger = LogManager.getLogger();

    Move2Card() {
        super("Move 2");
        super.addAction(new MoveRobot());
        super.addAction(new MoveRobot());
        //load image
        drawCardImage();
        //ODER:
        /*
        try {
            super.cardImage = ImageIO.read(new File("programming-cards/move-2-card.png"));
        } catch (java.io.IOException | NullPointerException e){
            logger.warn("Move 2 card image could not be read");
        }
         */
        logger.info("moved 2 Tiles");
    }

    @Override
    public ImageView drawCardImage() {
        return ImageHandler.createImageView("/programming-cards/move-2-card.png");
    }
}
