package game.gameObjects.cards;

import game.gameActions.MoveRobotBack;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * @author annika
 */
public class MoveBackCard extends ProgrammingCard {
    private static final Logger logger = LogManager.getLogger();

    MoveBackCard() {
    super("Move Back");
    super.addAction(new MoveRobotBack());
    drawCardImage();
    logger.info("moved back");
    }

    @Override
    public ImageView drawCardImage() {
        var stream = getClass().getResourceAsStream("/programming-cards/move-back-card.png");
        var image = new Image(stream);
        return new ImageView(image);
    }
}
