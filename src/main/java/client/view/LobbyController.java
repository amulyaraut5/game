package client.view;
import client.Main;
import client.model.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import utilities.JSONProtocol.JSONMessage;
import utilities.JSONProtocol.body.SetStatus;
import utilities.Utilities;

import static utilities.Utilities.PORT;




public class LobbyController {

    private Stage lobbyStage;

    private Client client;

    private String userName;

    @FXML
    private CheckBox readyCheckbox;


    @FXML
    private void checkBoxAction() {

        JSONMessage msg = new JSONMessage(new SetStatus(readyCheckbox.isSelected()));
        client.sendMessage(msg);



    }

    public void setStage(Stage lobbyStage) {
        this.lobbyStage = lobbyStage;
    }

    public void close() {
        //TODO
    }
}
