package game.gameObjects.cards.programming;

import game.gameActions.MoveRobotBack;
import game.gameObjects.cards.ProgrammingCard;
import javafx.scene.image.ImageView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.ImageHandler;
import utilities.Utilities.CardType;

/**
 * @author annika
 */
public class BackUp extends ProgrammingCard {
    private static final Logger logger = LogManager.getLogger();

    public BackUp() {
        super(CardType.BackUp);
        super.addAction(new MoveRobotBack());
        drawCardImage();
        logger.info("moved back");
    }

    @Override
    public ImageView drawCardImage() {
        return ImageHandler.createImageView("/programming-cards/move-back-card.png");
    }
}
