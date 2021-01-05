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
        //load image
        drawCardImage();
        //ODER:
        /*
        try {
            super.cardImage = ImageIO.read(new File("programming-cards/move-1-card.png"));
        } catch (java.io.IOException | NullPointerException e){
            logger.warn("Move one card image could not be read");
        }
         */
        logger.info("moved One Tile");
    }

    @Override
    public ImageView drawCardImage() {
        return ImageHandler.createImageView("/programming-cards/move-1-card.png");

    }
}
