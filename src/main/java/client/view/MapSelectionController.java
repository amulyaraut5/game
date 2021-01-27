package client.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import utilities.JSONProtocol.JSONBody;
import utilities.JSONProtocol.JSONMessage;
import utilities.JSONProtocol.body.MapSelected;
import utilities.Updatable;


public class MapSelectionController extends Controller implements Updatable {
    @FXML private CheckBox dizzyHighway;
    @FXML private CheckBox riskyCrossing;

    @FXML
    private void choiceBoxActionForMap(ActionEvent event){

        if(dizzyHighway.isSelected()){
            JSONBody jsonBody = new MapSelected("DizzyHighway");
            client.sendMessage(jsonBody);
            viewManager.showLobby();

        }
        else if(riskyCrossing.isSelected()){
            JSONBody jsonBody = new MapSelected("ExtraCrispy");
            client.sendMessage(jsonBody);
            viewManager.showLobby();
        }
    }

    @Override
    public void update(JSONMessage message) {

    }
}
