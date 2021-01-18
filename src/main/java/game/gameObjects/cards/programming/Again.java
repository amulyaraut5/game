package game.gameObjects.cards.programming;

import game.gameObjects.cards.ProgrammingCard;
import javafx.scene.image.ImageView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.ImageHandler;
import utilities.enums.CardType;

/**
 * @author annika
 */
public class Again extends ProgrammingCard {
    private static final Logger logger = LogManager.getLogger();

    public Again() {
        card = CardType.Again;
//        super.addAction(new AgainAction());
//        drawCardImage();
//        logger.info("Repeat the programming in previous register.");
    }

    @Override
    public ImageView drawCardImage() {
        return ImageHandler.createImageView("/cards/programming/Again-card.png");
    }
}
