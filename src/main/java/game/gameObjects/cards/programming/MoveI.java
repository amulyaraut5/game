package game.gameObjects.cards.programming;

import game.gameActions.MoveRobot;
import game.gameObjects.cards.ProgrammingCard;
import javafx.scene.image.ImageView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.ImageHandler;
import utilities.Utilities.CardType;

/**
 * @author annika
 */
public class MoveI extends ProgrammingCard {
    private static final Logger logger = LogManager.getLogger();

    public MoveI() {
        super(CardType.MoveI);
        super.addAction(new MoveRobot());
        drawCardImage();
        logger.info("moved One Tile");
    }

    @Override
    public ImageView drawCardImage() {
        return ImageHandler.createImageView("/programming-cards/move-1-card.png");

    }
}