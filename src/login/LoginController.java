package login;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import view.Controller;
import view.Game;

import java.io.IOException;
import java.time.LocalDate;

public class LoginController {
    public Button okButton;
    public TextField textUserName;
    public DatePicker datePicker;
    public LocalDate date;
    private String username = "default";

    public void handleLogIn(javafx.event.ActionEvent actionEvent) throws IOException {
        try{
            username = textUserName.getText();
        } catch(Exception e){
            username = "n";
        }
        date = datePicker.getValue();
        System.out.println(username + date.toString() );
        Parent gameView = new FXMLLoader().load(getClass().getResource("/view/game.fxml"));
        Scene gameViewScene = new Scene(gameView);
        //this line gets the Stage information
        Stage window = (Stage) okButton.getScene().getWindow();

        window.setScene(gameViewScene);
        window.show();
    }


    }
