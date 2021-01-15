package client.view;

import client.model.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import utilities.SoundHandler;
import java.io.PrintWriter;

/**
 * This class allows only host to select a map from the different options
 * for the game.
 * No usage: Because MapSelection Protocol is not given.
 * @author Amulya
 */

public class MapSelectionController extends Controller {
    @FXML
    private CheckBox dizzyHighway;
    @FXML
    private CheckBox riskyCrossing;
    @FXML
    private Button soundsOn;
    @FXML private Button soundsOff;



    // TODO Only host can select the map
    @FXML
    private void choiceBoxActionForMap(ActionEvent event) {

        if (dizzyHighway.isSelected()) {
            PrintWriter writer = Client.getInstance().getWriter();
            writer.println("DizzyHighway");
            writer.flush();

            viewManager.nextScene();

        } else if (riskyCrossing.isSelected()) {
            PrintWriter writer = Client.getInstance().getWriter();
            writer.println("RiskyCrossing");
            writer.flush();
            viewManager.nextScene();
        }
    }



    public void initialize() { }

}