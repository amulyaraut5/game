package game.gameObjects.tiles;

import game.Player;
import game.gameActions.MoveRobot;
import javafx.scene.Node;
import utilities.ImageHandler;
import utilities.Orientation;

/**
 * @author Amulya
 */

public class PushPanel extends Attribute {

    private int[] registers; //active registers

    public PushPanel(Orientation orientation, int[] registers) {
        this.orientation = orientation;
        this.registers = registers;
        this.type = "PushPanel";
    }

    /**
     * Push panels push any robots resting on them into the next space in the direction the push
     * panel faces. They activate only in the register that corresponds to the number on them. For
     * example, if you end register two on a push panel labeled “2, 4” you will be pushed. If you end
     * register three on the same push panel, you won’t be pushed.
     */

    @Override
    public void performAction(Player player) {

        for (int i : registers) {

            if (i == player.getCurrentRegister()) {
                new MoveRobot().doAction(orientation, player);

            } else {
                // Do nothing
                // Print message saying that this push panel has no effect for current register.
            }
        }
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
}