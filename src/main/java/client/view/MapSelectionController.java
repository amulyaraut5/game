package client.view;

import client.model.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;



import java.io.PrintWriter;

public class MapSelectionController extends Controller{
    @FXML private CheckBox dizzyHighway;
    @FXML private CheckBox riskyCrossing;

    @FXML
    private void choiceBoxActionForMap(ActionEvent event){

        if(dizzyHighway.isSelected()){
            PrintWriter writer = Client.getInstance().getWriter();
            writer.println("DizzyHighway");
            writer.flush();

            viewManager.nextScene();

        }
        else if(riskyCrossing.isSelected()){
            PrintWriter writer = Client.getInstance().getWriter();
            writer.println("RiskyCrossing");
            writer.flush();
            viewManager.nextScene();
        }
    }
}