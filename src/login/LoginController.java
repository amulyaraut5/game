package login;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

public class LoginController {
    public Button okButton;
    public TextField textUserName;
    public DatePicker datePicker;
    public LocalDate date;
    private String username;
    public void handleLogIn(javafx.event.ActionEvent actionEvent) throws IOException {
        try{
            username = textUserName.getText();
        } catch(Exception e){
            //
        }
        date = datePicker.getValue();
        System.out.println(username + date.toString() );
        Parent gameView = FXMLLoader.load(getClass().getResource("/view/game.fxml"));

        Scene gameViewScene = new Scene(gameView);

        //this line gets the Stage infomation
        Stage window = (Stage) okButton.getScene().getWindow();

        window.setScene(gameViewScene);
        window.show();
    }


    }
