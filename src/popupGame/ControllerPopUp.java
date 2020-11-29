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
import javafx.scene.control.Label;
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
    public ChoiceBox <String> playerBox = new ChoiceBox();
    public ChoiceBox <String> cardListBox = new ChoiceBox();
    ObservableList playerList = FXCollections.observableArrayList(" ");
    ObservableList cardList = FXCollections.observableArrayList("Baron", "Countess",
            "Guard", "Handmaid", "King", "Priest", "Prince", "Princess");
    public Label userMessage;
    private Controller controller;

    public void handleOkButton(javafx.event.ActionEvent actionEvent) {
        String player = playerBox.getValue();
        String card = cardListBox.getValue();
        if (player.equals(null) || card.equals(null)) userMessage.setText("Please select a card and a player");
        else {
            this.controller.setAnswer(player +" "+ card);
            Stage window = (Stage) closePopUp.getScene().getWindow();
            window.close();
        }


    }
    public void setPlayer(ArrayList<String> playerL){
        for (String player : playerL){
            playerList.add(player);
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        playerBox.setValue(player0);
        playerBox.setItems(playerList);
        cardListBox.setValue("Baron");
        cardListBox.setItems(cardList);

    }
    public void setController(Controller controller){
        this.controller = controller;
    }

}
