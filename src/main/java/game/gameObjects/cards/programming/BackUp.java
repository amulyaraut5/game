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
public class BackUp extends Card {
    private static final Logger logger = LogManager.getLogger();

    public BackUp() {
        card = CardType.BackUp;
//        super.addAction(new MoveRobotBack());
//        drawCardImage();
//        logger.info("moved back");
    }

    @Override
    public ImageView drawCardImage() {
        return ImageHandler.createImageView("/cards/programming/BackUp-card.png");
    }
}
