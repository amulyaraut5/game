package view;

import client.ChatClient;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class Controller implements Initializable {
    public String message;
    public Button submitButton;
    public TextArea chatTextArea;
    public TextArea chatWindow;
    //play button
    public Button playButton;
    //card 1
    public Button card1;
    public ImageView card1Image;
    /**
     * This method changes the Scene
     */
    public Button card2;
    /**
     * Handles the pop-up of the rule card
     */
    public Button ruleCardButton;
    public Label player0Label;
    public Label player1Label;
    public Label player2Label;
    public Label player3Label;
    public Label serverMessage;
    private String userName;
    private ChatClient client;
    private ObservableList playerList;
    private ArrayList <String> userList;

    /**
     * this method disables a player vbox
     *
     * @param event
     * @param player
     */
    public static void playerSetDisabled(ActionEvent event, VBox player) {
        // Button was clicked, do something...
        player.setDisable(true);
    }

    public void chatMessageHandling() {
        String message = chatTextArea.getText();

        chatWindow.appendText("[You]: " + message + "\n");
        client.sentUserInput(message);
        chatTextArea.clear();
    }

    /**
     * this methods changes the play button by clicking on it
     */
    public void handlePlayButton() {
        System.out.println("play button clicked");
        playButton.setDisable(true);
        playButton.setText("Have Fun!");
        message = "#join";

    }

    /**
     * this method changes the image of card1
     */
    public void changeImageCard1() {
        Image image = new Image("/images/king.png");
        card1Image.setImage(image);
    }

    public void changeSceneCard(javafx.event.ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/popupGame/popup.fxml"));
        Stage popup = new Stage();
        popup.setScene(new Scene(root));
        //for pop-up:
        popup.initModality(Modality.APPLICATION_MODAL);
        //reminde popup-window of its "owner"/ gets the popup-window infomation
        popup.initOwner(card2.getScene().getWindow());
        //show pop-up and wait until it is dismissed
        popup.showAndWait();
    }

    /**
     * Handles the pop-up of the rule card
     */
    public void handleRuleCardButton(javafx.event.ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/popupGame/ruleCard.fxml"));
        Stage popup = new Stage();
        popup.setScene(new Scene(root));
        //for pop-up:
        popup.initModality(Modality.NONE);
        //reminde popup-window of its "owner"/ gets the popup-window infomation
        popup.initOwner(ruleCardButton.getScene().getWindow());
        //show pop-up and wait until it is dismissed
        popup.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void setClient(ChatClient chatClient) {
        client = chatClient;
    }

    public void setUser(String name) {
        player0Label.setText(name);
        userName = name;
    }

    public void ServerResponse(String response) {
        System.out.println(response);
        if (response.contains("joined the room")) {
            player1Label.setText(response.split(" ", 2)[0]);
        } else {
            serverMessage.setText(response);
        }
    }
    public TextArea userArea;
    /**
     * each time a new player joins a new label gets filled
     * @param name who joins
     */
    public void setRoomUser(String name){
       userArea.appendText("\n" + name);
    }

    public void appendChatMessage(String message) {
        chatWindow.appendText(message + "\n");
    }
}
