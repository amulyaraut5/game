package client.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import utilities.JSONProtocol.JSONMessage;
import utilities.JSONProtocol.body.Error;
import utilities.JSONProtocol.body.MapSelected;
import utilities.Updatable;

/**
 * TODO
 */
public class MapSelectionController extends Controller implements Updatable {
    private static MapSelectionController mapSelectionController;
    @FXML
    private CheckBox dizzyHighway;
    @FXML
    private CheckBox extraCrispy;
    @FXML
    private Label infoLabel;

    public MapSelectionController() {
        mapSelectionController = this;
    }

    @FXML
    public void initialize() {
        setVisible(false);
        infoLabel.setText("");
    }

    @FXML
    private void choiceBoxActionForMap(ActionEvent event) {

        if (dizzyHighway.isSelected()) {
            viewClient.sendMessage(new MapSelected("DizzyHighway"));
            setDisable(true);
            viewManager.showLobby();
        } else if (extraCrispy.isSelected()) {
            viewClient.sendMessage(new MapSelected("ExtraCrispy"));
            setDisable(true);
            viewManager.showLobby();
        }
    }

    @FXML
    private void switchBackToLobby(ActionEvent event) {
        viewManager.showLobby();
        infoLabel.setText("");
    }

    @Override
    public void update(JSONMessage message) {
        switch (message.getType()) {
            case Error -> {
                Error error = (Error) message.getBody();
                infoLabel.setText(error.getError());
            }
            case SelectMap -> {
                setVisible(true);
                setDisable(false);
            }
        }
    }

    public void setVisible(boolean b) {
        dizzyHighway.setVisible(b);
        extraCrispy.setVisible(b);
    }

    public void setDisable(boolean b) {
        dizzyHighway.setDisable(b);
        extraCrispy.setDisable(b);
    }

    public void setSelected(boolean b) {
        dizzyHighway.setSelected(b);
        extraCrispy.setSelected(b);
    }

    public void setInfoLabel(String string) {
        infoLabel.setText(string);
    }

    public static MapSelectionController getMapSelectionController() {
        return mapSelectionController;
    }
}
