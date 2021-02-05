package game.gameObjects.tiles;

import javafx.scene.Node;
import utilities.ImageHandler;
import utilities.enums.AttributeType;

/**
 * @author Simon
 */

public class EnergySpace extends Attribute {

    private int count; //number of energy cubes

    public EnergySpace(int count) {
        this.count = count;
        type = AttributeType.EnergySpace;
    }

    @Override
    public Node createImage() {
        String path;
        if (count > 0) {
            path = "/tiles/energySpace_green.png";
        } else {
            path = "/tiles/energySpace_red.png";
        }

        return ImageHandler.createImageView(path);
    }

    public int getCount() {
        return count;
    }
}