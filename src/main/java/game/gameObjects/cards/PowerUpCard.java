package game.gameObjects.cards;

import game.gameActions.PowerUpRobot;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author annika
 */
public class PowerUpCard extends ProgrammingCard{
    private static final Logger logger = LogManager.getLogger();

    PowerUpCard() {
        super("Power Up");
        super.setAction(new PowerUpRobot());
        drawCardImage();
        logger.info("took one energy cube");
    }

    @Override
    public ImageView drawCardImage() {
        var stream = getClass().getResourceAsStream("/programming-cards/power-up-card.png");
        var image = new Image(stream);
        return new ImageView(image);
    }
}
