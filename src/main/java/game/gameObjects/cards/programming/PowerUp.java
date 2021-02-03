package game.gameObjects.cards.programming;

import game.gameObjects.cards.Card;
import javafx.scene.image.ImageView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.ImageHandler;
import utilities.enums.CardType;

/**
 * @author annika
 */
public class PowerUp extends Card {
    private static final Logger logger = LogManager.getLogger();

    public PowerUp() {
        card = CardType.PowerUp;
//        super.addAction(new PowerUpRobot());
//        drawCardImage();
//        logger.info("took one energy cube");
    }

    @Override
    public ImageView drawCardImage() {
        return ImageHandler.createImageView("/cards/programming/PowerUp-card.png");
    }
}
