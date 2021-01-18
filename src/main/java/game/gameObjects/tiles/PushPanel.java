package game.gameObjects.tiles;

import javafx.scene.Node;
import utilities.ImageHandler;
import utilities.enums.Orientation;
import utilities.enums.AttributeType;

/**
 * @author Amulya
 */

public class PushPanel extends Attribute {

    private int[] registers; //active registers
    private Orientation orientation;

    public PushPanel(Orientation orientation, int[] registers) {
        this.orientation = orientation;
        this.registers = registers;
        this.type = AttributeType.PushPanel;
    }


    @Override
    public Node createImage() {
        String path = "";
        if (registers.length == 1) {
            path = "/tiles/pushPanel/pushPanel_" + registers[0] + "c.png";
        } else if (registers.length == 2) {
            path = "/tiles/pushPanel/pushPanel_24c.png";
        } else if (registers.length == 3) {
            path = "/tiles/pushPanel/pushPanel_135c.png";
        }

        return ImageHandler.createImageView(path, orientation);
    }

    public int[] getRegisters() {
        return registers;
    }

    public Orientation getOrientation() {
        return orientation;
    }
}