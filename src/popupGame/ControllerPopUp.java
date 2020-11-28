package popupGame;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerPopUp implements Initializable {

    public Button closePopUp;
    
    public void handleOkButton(javafx.event.ActionEvent actionEvent) {
        Stage window = (Stage) closePopUp.getScene().getWindow();
        window.close();
    }
    String player0 = "0";
    String player1 = "1";
    String player2 = "2";
    String player3 = "3";
    ObservableList playerList = FXCollections.observableArrayList(player0, player1, player2, player3);
    public ChoiceBox playerBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        playerBox.setItems(playerList);
        playerBox.setValue("Choose a player");
    }

    //ObservableList playerList = FXCollections.observableArrayList("Baron", "Countess",
    //        "Guard", "Handmaid", "King", "Priest", "Prince", "Princess");
}
