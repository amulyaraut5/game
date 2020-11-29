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
    public ImageView card2Image;
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
    public VBox player0Box;
    public VBox player1Box;
    public VBox player2Box;
    public VBox player3Box;
    public Label roundLabel;

    public Label serverMessage;
    private String userName;
    private ChatClient client;
    private ArrayList <String> playerList = new ArrayList<>();
    private ArrayList <String> userList = new ArrayList<>();
    private String responseServer;

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
        client.sentUserInput("#playerList");
        client.sentUserInput("#play");
        System.out.println("play button clicked");
        playButton.setDisable(true);
        playButton.setText("Have Fun!");
        message = "#join";
    }
    public void handleStartButton(){
        client.sentUserInput("#start");
        System.out.println("start button clicked");
        playButton.setDisable(true);
        playButton.setText("Have Fun!");

    }
    public void increaseRoundLabel(String round){
        roundLabel.setText("Round" + round);
        roundLabel.setVisible(true);
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
    public void begin(){
        client.sentUserInput("userList");
    }
    public void setClient(ChatClient chatClient) {
        client = chatClient;
    }

    public void setUser(String name) {
        userName = name;

    }
    /**
     * this method changes the image of card1
     */
    public void changeImageCard1(String card) {
        Image image = new Image("/images/cards/" +card + ".png");
        card1Image.setImage(image);
    }
    /**
     * this method changes the image of card2
     */
    public void changeImageCard2(String card) {
        Image image = new Image("/images/cards/" +card + ".png");
        card2Image.setImage(image);
    }
    public void chooseCards (String message){
        String card1 = message.split(" ")[4];
        System.out.println(card1);
        changeImageCard1(card1);
        String card2 = message.split(" ")[9];
        changeImageCard2(card2);
        System.out.println(card2);

    }
    public void serverResponse(String response) {
        responseServer += response;
        System.out.println(response);
        serverMessage.setText(responseServer);
        /*if (response.contains("joined the room")) {
            player1Label.setText(response.split(" ", 2)[0]);
        } else {
            serverMessage.setText(response);
        }*/
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

    public void setGamePlayer(String s) {
        if(s.equals("You") || s.equals("You've")){
            s= userName;
        }
        System.out.println("setGame "+s);
        playerList.add(s);
        System.out.println(playerList.size());
        if(playerList.size()==1){
            player0Label.setText(s);
            player0Box.setVisible(true);
        }
        else if(playerList.size()==2){
            player1Label.setText(s);
            player1Box.setVisible(true);
        }
        else if (playerList.size()==3){
            player2Label.setText(s);
            player2Box.setVisible(true);
        }
        else if(playerList.size()==4) {
            player3Label.setText(s);
            player3Box.setVisible(true);
        }

    }

    public void setFormerPlayer(String formerPlayer) {
        System.out.println(formerPlayer + "former Player");
        String[] former = formerPlayer.split(" ");
        for (int i = 0; i<former.length; i++){
            System.out.println(former[i] + "former");
            setGamePlayer(former[i]);
        }

    }

    public void setUserList(String formerUser) {
        System.out.println(formerUser + "former Player");
        String[] former = formerUser.split(" ");
        for (int i = 0; i<former.length; i++){
            System.out.println(former[i] + "former");
            setUserList(former[i]);
        }
    }
}
