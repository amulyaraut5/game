package game.gameObjects.tiles;

import game.Game;
import javafx.scene.Node;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.enums.AttributeType;

/**
 * @author Simon
 */

public abstract class Attribute {
    protected static final Logger logger = LogManager.getLogger();

    protected static Game game = Game.getInstance();
    protected AttributeType type;


    public abstract Node createImage();


    public AttributeType getType() {
        return type;
    }
}