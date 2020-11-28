package login;

import client.ChatClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import view.Controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LoginController {
    private static Stage loginStage;
    private LocalDate date;
    private String userName;
    private ChatClient client;

    @FXML
    private TextField textUserName;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Label labelResponse;

    public LoginController() {
        client = new ChatClient("localhost", 4444);
        client.setLoginController(this);
    }

    public static void setStage(Stage loginStage) {
        LoginController.loginStage = loginStage;
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
        if (response.equals("successful")) {
            startGameView();
            loginStage.close();
        } else {
            labelResponse.setText(response);
        }
    }

    private void startGameView() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/game.fxml"));
        Parent gameView = null;
        try {
            gameView = (Parent) loader.load();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        Controller controller = loader.getController();
        controller.setUser(userName);
        controller.setClient(client);
        client.setController(controller);
        Stage gameStage = new Stage();
        gameStage.setTitle("Love Letter");
        gameStage.setScene(new Scene(gameView));
        gameStage.setResizable(false);
        gameStage.show();
    }
}