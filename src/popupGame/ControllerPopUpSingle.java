package popupGame;

import client.ChatClient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import view.Controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ControllerPopUpSingle implements Initializable {

    public Button closePopUp;
    public ChatClient client;
    String player0 = " ";
    private Controller controller;
    public String actualCard;
    public boolean playerChoosed = false;
    public ChoiceBox <String> playerBox = new ChoiceBox();
    ObservableList playerList = FXCollections.observableArrayList(" ");

    public Label userMessage;
    /**
     * this methods sets the chatClient for the popupcontroller
     * @param chatClient
     */
    public void setClient(ChatClient chatClient) {
        client = chatClient;
    }

    public void handleOkPlayerButton(javafx.event.ActionEvent actionEvent) {
        String player = playerBox.getValue();
        if (player.equals(null)) {
            userMessage.setVisible(true);
            userMessage.setText("Please select a player");
        }
        else {
            userMessage.setVisible(false);
            client.sentUserInput("#choose "+ player);
            playerChoosed= true;
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


    }
    public void setController(Controller controller){
        this.controller = controller;
    }

}
