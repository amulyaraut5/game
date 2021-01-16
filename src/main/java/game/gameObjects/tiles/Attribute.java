package game.gameObjects.tiles;

import game.Game;
import game.Player;
import javafx.scene.Node;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.Orientation;
import utilities.Utilities.AttributeType;

/**
 * @author Amulya
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