package popupGame;

import client.ChatClient;
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
    public ChatClient client;
    String player0 = " ";
    String player1 = "1";
    String player2 = "2";
    String player3 = "3";
    public String actualCard;
    public boolean playerChoosed = false;
    public boolean cardChoosed = false;
    public ChoiceBox <String> playerBox = new ChoiceBox();
    public ChoiceBox <String> cardListBox = new ChoiceBox();
    ObservableList playerList = FXCollections.observableArrayList(" ");
    ObservableList cardList = FXCollections.observableArrayList("Baron", "Countess",
            "Guard", "Handmaid", "King", "Priest", "Prince", "Princess");
    public Label userMessage;
    private Controller controller;
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
            //client.sentUserInput("#choose "+ player);
            playerChoosed= true;

        }
    }
    public void handleOkCardButton(javafx.event.ActionEvent actionEvent) {
        String card = cardListBox.getValue();
        if (card.equals(null)) {
            userMessage.setVisible(true);
            userMessage.setText("Please select a player");
        }
        else {
            if(playerChoosed){
                userMessage.setVisible(false);
                //client.sentUserInput("#choose "+ card);
                cardChoosed= true;
                Stage window = (Stage) closePopUp.getScene().getWindow();
                window.close();

            } else{
                userMessage.setVisible(true);
                userMessage.setText("You have to make clear choices");
            }

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

    public void setType(String cardName) {
        actualCard = cardName;
        if(actualCard.equals("Priest") || actualCard.equals("King")) {
            cardListBox.setVisible(false);
            cardChoosed= true;
        }
    }
}
