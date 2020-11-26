package view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOException;

public class Controller {

    /**
     * This method changes the Scene
     */
    public void changeSceneCard(ActionEvent event) throws IOException {
        Parent popup = FXMLLoader.load(getClass().getResource("popup.fxml"));
        Scene popupScene = new Scene(popup);

        //this line gets the Stage infomation
        Stage window = (Stage) card2.getScene().getWindow();

        window.setScene(popupScene);
        window.show();
    }

}
