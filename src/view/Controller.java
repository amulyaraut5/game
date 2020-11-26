package view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOException;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;

public class Controller {
    public static void main (String [] args){

    }
    public  Button playButton;
    public  void handlePlayButton() {
        System.out.println("play button clicked");
        playButton.setTextFill(Color.GRAY);
        playButton.setText("Have Fun!");
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
