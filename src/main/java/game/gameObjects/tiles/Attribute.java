package game.gameObjects.tiles;

import javafx.scene.Node;
import utilities.enums.AttributeType;

/**
 * @author Simon
 */

public abstract class Attribute {

    protected AttributeType type;

    public abstract Node createImage();

    public AttributeType getType() {
        return type;
    }
}