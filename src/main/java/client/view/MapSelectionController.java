package client.view;

import client.model.Client;
import game.gameActions.Action;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import utilities.SoundHandler;


import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class MapSelectionController extends Controller {
    @FXML
    private CheckBox dizzyHighway;
    @FXML
    private CheckBox riskyCrossing;
    @FXML
    private Button soundsOn;
    @FXML private Button soundsOff;

    private SoundHandler soundHandler;

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

    @FXML
    private void soundsOnAction(ActionEvent event) {
        this.soundHandler.musicOn();
    }
    @FXML
    private void soundsOffAction(ActionEvent event){
        this.soundHandler.musicOff();
    }
    public void initialize() {
        this.soundHandler = new SoundHandler();
    }

}