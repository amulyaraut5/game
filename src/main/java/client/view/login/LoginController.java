package client.view.login;

import client.model.Client;
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

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LoginController {
    private static Stage loginStage;
    private static Client client;
    private LocalDate date;
    private String userName;
    @FXML
    private TextField textUserName;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Label labelResponse;

    @FXML
    private Button okButton;

    public LoginController() {
        client = new Client("localhost", 5444);
    }

    public static void setStage(Stage loginStage) {
        LoginController.loginStage = loginStage;
    }

    public static void setClient(Client client) {
        LoginController.client = client;
    }

    public void close() {
        client.disconnect();
    }

   /* public void initialize() {
        if (!client.isConnection()) {
            noConnection();
        }
    } */

    @FXML
    private void handleLogIn(ActionEvent event) {
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
                //client.sentUserInput(userName + " " + dateText);
            } catch (IllegalArgumentException ex) {
                labelResponse.setText("Please check your Date! (dd.mm.yyyy)");
            }
        }
    }

    public void ServerResponse(String response) {
        if (response.equals("successful")) {
            startLoginView();
            loginStage.close();
        } else {
            labelResponse.setText(response);
        }
    }

    private void startLoginView() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/gameView.fxml"));
        Parent gameView = null;
        try {
            gameView = (Parent) loader.load();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        //Controller controller = loader.getController();
        //setUser(userName);
        setClient(client);

        //client.setController(controller);
        Stage gameStage = new Stage();
        gameStage.setTitle("Love Letter");
        gameStage.setScene(new Scene(gameView));
        gameStage.setResizable(false);
        gameStage.show();

        gameStage.setOnCloseRequest(event -> {
            //controller.close();
            gameStage.close();
            loginStage.close();
        });
    }

    /**
     * Methods gets called by the ChatClient if no connection to the server could be established.
     */
    public void noConnection() {
        okButton.setDisable(true);
        labelResponse.setText("No connection to the server!");
    }
}