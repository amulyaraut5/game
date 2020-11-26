package view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
        Parent popup = FXMLLoader.load(getClass().getResource("/popupGame/popup.fxml"));
        Scene popupScene = new Scene(popup);
        //this line gets the Stage infomation
        Stage window = (Stage) card2.getScene().getWindow();

        window.setScene(popupScene);
        window.show();
    }
}
