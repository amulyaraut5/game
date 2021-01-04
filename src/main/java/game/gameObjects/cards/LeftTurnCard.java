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
public class LeftTurnCard extends ProgrammingCard{
    private static final Logger logger = LogManager.getLogger();

    LeftTurnCard(){
        super("Left Turn");
        super.addAction(new RotateRobot(Orientation.LEFT));
        drawCardImage();
        logger.info("rotated left");
    }

    @Override
    public ImageView drawCardImage() {
        var stream = getClass().getResourceAsStream("/programming-cards/left-turn-card.png");
        var image = new Image(stream);
        return new ImageView(image);
    }
}
