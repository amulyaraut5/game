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
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import view.Controller;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ControllerPopUp implements Initializable {

    public Button closePopUp;

    String player0 = " ";
    String player1 = "1";
    String player2 = "2";
    String player3 = "3";
    public ChoiceBox playerBox = new ChoiceBox();
    public ChoiceBox cardListBox = new ChoiceBox();
    ObservableList playerList = FXCollections.observableArrayList(" ");
    ObservableList cardList = FXCollections.observableArrayList("Baron", "Countess",
            "Guard", "Handmaid", "King", "Priest", "Prince", "Princess");

    public void handleOkButton(javafx.event.ActionEvent actionEvent) {
        Stage window = (Stage) closePopUp.getScene().getWindow();
        window.close();
    }
    public void setPlayer(ObservableList<String> playerL){
        playerList.addAll(playerL);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        playerBox.setValue(player0);
        playerBox.setItems(playerList);
        cardListBox.setValue("Baron");
        cardListBox.setItems(cardList);

    }

}
