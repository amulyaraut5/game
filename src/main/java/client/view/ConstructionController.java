package client.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ConstructionController extends Controller {
    @FXML
    private Label taskLabel;

    public void currentPlayer(boolean isThisPlayer) {
        if (isThisPlayer) taskLabel.setText("Please choose your starting point by clicking on the map");

        else taskLabel.setText("Please wait for other players");
    }
}
