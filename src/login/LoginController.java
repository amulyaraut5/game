package login;

import client.ChatClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import view.Controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LoginController {
    public LocalDate date;
    private String userName;
    private ChatClient client;

    @FXML
    private TextField textUserName;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Button okButton;

    @FXML
    private Label labelResponse;

    public LoginController() {
        client = new ChatClient("localhost", 4444);
        client.setLoginController(this);
    }

    @FXML
    void handleLogIn(ActionEvent event) {
        labelResponse.setText("");
        userName = textUserName.getText();
        date = datePicker.getValue();
        if (userName.isBlank()) {
            labelResponse.setText("Please insert a Username!");
        } else if (userName.contains(" ")) {
            labelResponse.setText("Spaces are not allowed in usernames!");
        } else if (date == null) {
            labelResponse.setText("Please insert a Date!");
        } else {
            try {
                String dateText = date.format(DateTimeFormatter.ofPattern("dd.MM.yy"));
                client.sentUserInput(userName + " " + dateText);
            } catch (IllegalArgumentException ex) {
                labelResponse.setText("Please check your Date! (dd.mm.yyyy)");
            }
        }
    }

    public void ServerResponse(String response) {
        System.out.println(response);
        if (response.equals("successful")) {
            startGameView();
        } else {
            labelResponse.setText(response);
        }
    }

    private void startGameView() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/game.fxml"));
        Parent gameView = null;
        try {
            gameView = loader.load();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        Controller controller = loader.getController();
        controller.setUser(userName);

        Scene gameViewScene = new Scene(gameView);
        //this line gets the Stage information
        Stage stage = new Stage();
        stage.setScene(gameViewScene);

        stage.show();
        client.setController(controller);
        System.out.println("ja");

    }
}