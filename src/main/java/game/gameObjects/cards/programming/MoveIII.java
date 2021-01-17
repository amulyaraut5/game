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
public class MoveIII extends ProgrammingCard {
    private static final Logger logger = LogManager.getLogger();

    public MoveIII() {
        super(CardType.MoveIII);
        super.addAction(new MoveRobot());
        super.addAction(new MoveRobot());
        super.addAction(new MoveRobot());
        drawCardImage();
        logger.info("moved 3 Tiles");
    }

    @Override
    public ImageView drawCardImage() {
        return ImageHandler.createImageView("/cards/programming/MoveIII-card.png");
    }
}
