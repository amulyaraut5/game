package game.gameObjects.cards;

import game.gameActions.AgainAction;
import javafx.scene.image.ImageView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.ImageHandler;


/**
 * @author annika
 */
public class AgainCard extends ProgrammingCard {
    private static final Logger logger = LogManager.getLogger();

    public AgainCard() {
        super("Again");
        super.addAction(new AgainAction());
        drawCardImage();
        logger.info("Repeat the programming in previous register.");
    }

    @Override
    public ImageView drawCardImage() {
        return ImageHandler.createImageView("/programming-cards/again-card.png");
    }
}
