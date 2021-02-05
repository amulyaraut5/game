package game.gameObjects.tiles;

import game.Game;
import javafx.scene.Node;
import utilities.enums.AttributeType;

/**
 * @author Simon
 */

public abstract class Attribute {

    protected static Game game = Game.getInstance();
    protected AttributeType type;

    public abstract Node createImage();

    public AttributeType getType() {
        return type;
    }
}