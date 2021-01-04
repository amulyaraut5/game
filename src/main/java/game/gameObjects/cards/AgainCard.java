package game.gameObjects.cards;

import game.gameActions.AgainAction;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * @author annika
 */
public class AgainCard extends ProgrammingCard{
    private static final Logger logger = LogManager.getLogger();

    AgainCard(){
        super("Again");
        super.addAction(new AgainAction());
        drawCardImage();
        logger.info("Repeat the programming in previous register.");
    }

    @Override
    public ImageView drawCardImage() {
        var stream = getClass().getResourceAsStream("/programming-cards/again-card.png");
        var image = new Image(stream);
        return new ImageView(image);
    }
}
