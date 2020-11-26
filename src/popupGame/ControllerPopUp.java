package popupGame;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOException;

public class ControllerPopUp {

    public Button closePopUp;
    
    public void handleOkButton(javafx.event.ActionEvent actionEvent) {
        Stage window = (Stage) closePopUp.getScene().getWindow();
        window.close();
    }
}
