package client.view;

import client.ViewManager;
import client.model.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.JSONProtocol.JSONMessage;
import utilities.JSONProtocol.body.GameStarted;
import utilities.JSONProtocol.body.SetStatus;
import utilities.JSONProtocol.body.gameStarted.Field;
import utilities.JSONProtocol.body.gameStarted.Maps;

import java.util.ArrayList;


public class LobbyController extends Controller {
    private static final Logger logger = LogManager.getLogger();

    private Client client;

    @FXML
    private CheckBox readyCheckbox;

    @FXML
    private void checkBoxAction(ActionEvent event) {

        JSONMessage msg = new JSONMessage(new SetStatus(readyCheckbox.isSelected()));
        client.sendMessage(msg);
        ArrayList<Maps> map = new ArrayList<Maps>();
        ArrayList<Field> field = new ArrayList<Field>();
        field.add(new Field());

        map.add(new Maps(0, field));
        client.sendMessage(new JSONMessage(new GameStarted(map)));
    }
}
