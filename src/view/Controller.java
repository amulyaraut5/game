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
import popupGame.ControllerPopUp;

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
    public TextArea userArea;
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
    public String card1Name = "default";
    public String card2Name;
    private String answerPopUp ="defaultPlayer defaultCard";

    public Label serverMessage;
    private String userName;
    private ChatClient client;
    private ArrayList <String> playerList = new ArrayList<>();
    private ArrayList <String> userList = new ArrayList<>();
    private String responseServer;
    private String showPopUp= "Baron Guard King Prince"; //TODO

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

    /**
     * this method handles the own messages and puts a "You" in front of it
     */
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

    /**
     * this method handles the start button by clicking on it
     */
    public void handleStartButton(){
        client.sentUserInput("#start");
        System.out.println("start button clicked");
        playButton.setDisable(true);
        playButton.setText("Have Fun!");

    }

    /**
     * this method prints out the actual round in a label and also updates the
     * score of each player
     * @param round
     */
    public void increaseRoundLabel(String round){
        roundLabel.setText("Round" + round);
        roundLabel.setVisible(true);
        client.sentUserInput("#score");
    }

    /**
     * this method gets the actual score String and distributes it on the different labels
     * @param score
     */
    public void actualscore(String score) {
        /*String exampleString = "";
        if(playerList.size()==2) {
            String player1 = exampleString.split("---------------", 2)[1];
        }
        System.out.println(score);*/
    }

    /**
     * this method opens a popup window by clicking on card1
     * @throws IOException
     */
    public void changeSceneCard1( ) throws IOException {
        client.sentUserInput("#choose 1");
        openPopUp(card1);
    }
    /**
     * this method opens a popup window by clicking on card 2
     * @throws IOException
     */
    public void changeSceneCard2( ) throws IOException {
        client.sentUserInput("#choose 2");
        openPopUp(card2);
    }

    /**
     * this method opens up a pop up button depending which card gets clicked
     * @param card
     * @throws IOException
     */
    public void openPopUp(Button card) throws IOException {
        if (true/*showPopUp.contains(card1Name)*/) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/popupGame/popup.fxml"));
            Parent root = loader.load();
            ControllerPopUp controllerPopUp = loader.getController();
            controllerPopUp.setPlayer(playerList);
            controllerPopUp.setController(this);
            Stage popup = new Stage();
            popup.setScene(new Scene(root));
            //for pop-up:
            popup.initModality(Modality.APPLICATION_MODAL);
            //reminde popup-window of its "owner"/ gets the popup-window infomation
            popup.initOwner(card.getScene().getWindow());
            //show pop-up and wait until it is dismissed
            popup.showAndWait();
        }
    }

    /**
     * the popUpWindow controller can set the answer of the user
     * and the method sends the answer to the server
     * @param answerPopUp
     */
    public void setAnswer(String answerPopUp) {
        String player = answerPopUp.split(" ")[0];
        String card = answerPopUp.split(" ")[1];
        client.sentUserInput("#choose "+ player);
        //client.sentUserInput("#choose "+ card);
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    /**
     * this methods sets the chatClient for the controller
     * @param chatClient
     */
    public void setClient(ChatClient chatClient) {
        client = chatClient;
    }

    /**
     * the method is called in the loginController to pass the username
     * @param name
     */
    public void setUser(String name) {
        userName = name;

    }

    //change and handle cards
    /**
     * Handles the pop-up of the rule card
     */
    public void handleRuleCardButton(javafx.event.ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/popupGame/ruleCard.fxml"));
        Parent root = loader.load();
        Stage popup = new Stage();
        ControllerPopUp controllerPopUp = loader.getController();

        popup.setScene(new Scene(root));
        //for pop-up:
        popup.initModality(Modality.NONE);
        //reminde popup-window of its "owner"/ gets the popup-window infomation
        popup.initOwner(ruleCardButton.getScene().getWindow());
        //show pop-up and wait until it is dismissed

        popup.showAndWait();


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
    public void chooseCards (String message)  {
        card2Name = message.split(" ")[4];
        changeImageCard1(card2Name);
        card2Name = message.split(" ")[9];
        changeImageCard2(card2Name);
    }

    /**
     * this method transfers the desired answeres of the server to a label on the GUI
     * @param response
     */
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


    /**
     * this method adds messages to the chat window
     * @param message
     */
    public void appendChatMessage(String message) {
        chatWindow.appendText(message + "\n");
    }

    //change and handle players & users
    /**
     * each time a new player joins a new label gets filled
     * @param name who joins
     */
    public void setRoomUser(String name){
        userArea.appendText("\n" + name);
    }

    /**
     * this method adds a player to the playerList, prints the name on the playfield
     * and also sets it visible
     * @param s
     */
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

    /**
     * this method loads the player who logged in before the player clicked play
     * @param formerPlayer
     */
    public void setFormerPlayer(String formerPlayer) {
        System.out.println(formerPlayer + "former Player");
        String[] former = formerPlayer.split(" ");
        for (int i = 0; i<former.length; i++){
            System.out.println(former[i] + "former");
            setGamePlayer(former[i]);
        }

    }

    /**
     * this methods loads the former users
     *
     * @param formerUser
     */
    public void setUserList(String formerUser) { //TODO
        System.out.println(formerUser + "former Player");
        String[] former = formerUser.split(" ");
        for (int i = 0; i<former.length; i++){
            System.out.println(former[i] + "former");
            setUserList(former[i]);
        }
    }



}
