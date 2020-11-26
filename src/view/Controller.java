package view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOException;

import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Window;


public class Controller  {
    //public static VBox player0;

    public static void main (String [] args){

    }

    //public VBox player0;
    public  Button playButton;
    public  void handlePlayButton() {
        System.out.println("play button clicked");
        playButton.setTextFill(Color.GRAY);
        playButton.setText("Have Fun!");
    }
    public Button card1;
    public ImageView card1Image;

    public void changeImageCard1(){
        Image image = new Image("/images/king.png");
        card1Image.setImage(image);
    }
    //public void changeImage();
    //public static ActionEvent event;
    public static void playerSetDisabled(ActionEvent event, VBox player) {
        // Button was clicked, do something...
        player.setDisable(true);
    }


    /**
     * This method changes the Scene
     */
    public Button card2;
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
    public Button ruleCardButton;
    public void handleruleCardButton(javafx.event.ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/popupGame/ruleCard.fxml"));
        Stage popup = new Stage();
        popup.setScene(new Scene(root));
        //for pop-up:
        popup.initModality(Modality.APPLICATION_MODAL);
        //reminde popup-window of its "owner"/ gets the popup-window infomation
        popup.initOwner(ruleCardButton.getScene().getWindow());
        //show pop-up and wait until it is dismissed
        popup.showAndWait();
    }
}
