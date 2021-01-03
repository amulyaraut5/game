package game.gameObjects.tiles;

import game.Player;
import game.gameActions.MoveRobot;
import javafx.scene.image.ImageView;
import utilities.Utilities.Orientation;

/**
 * @author Amulya
 */

public class PushPanel extends Attribute {


    private int[] registerValue; //active registers
    private Orientation orientation; // push direction

    public PushPanel(Orientation orientation, int[] registerValue) {
        this.orientation = orientation;
        this.registerValue = registerValue;
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

        for (int i : registerValue) {

            if (i == player.getCurrentRegister()) {
                new MoveRobot().doAction(orientation, player);

            } else {
                // Do nothing
                // Print message saying that this push panel has no effect for current register.
            }
        }
    }

    @Override
    public ImageView createImage() {
        return new ImageView();
    }

}