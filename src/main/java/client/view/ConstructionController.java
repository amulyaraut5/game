package client.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * This class represents the construction phase where the player should set their starting point.
 * It shows the instructions for doing that.
 *
 * @author TODO
 */
public class ConstructionController extends Controller {
    /**
     * The label which displays the current task.
     */
    @FXML
    private Label taskLabel;

    /**
     * This manages which instruction gets displayed depending of who is the current player.
     *
     * @param isThisPlayer player is currentPlayer or not
     */
    public void currentPlayer(boolean isThisPlayer) {
        if (isThisPlayer) taskLabel.setText("Please choose your starting point by clicking on the map");
        else taskLabel.setText("Please wait for other players");
    }
}
