package game.gameObjects.cards;

import game.gameActions.RotateRobot;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.Utilities.Orientation;

/**
 * @author annika
 */
public class RightTurnCard extends ProgrammingCard{
    private static final Logger logger = LogManager.getLogger();

    RightTurnCard(){
        super("Right Turn");
        super.setAction(new RotateRobot(Orientation.RIGHT));
        drawCardImage();
        logger.info("rotated right");
    }

    @Override
    public ImageView drawCardImage() {
        var stream = getClass().getResourceAsStream("/programming-cards/right-turn-card.png");
        var image = new Image(stream);
        return new ImageView(image);
    }
}
