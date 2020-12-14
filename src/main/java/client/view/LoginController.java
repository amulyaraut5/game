package client.view;

import client.model.Client;
import Utilities.JSONProtocol.JSONMessage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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
    }

    public static void setStage(Stage loginStage) {
        LoginController.loginStage = loginStage;
    }

    public static void setClient(Client client) {
        LoginController.client = client;
    }

    public void close() {
        //client.disconnect(); TODO disconnect client on closure of window
    }

   public void initialize() {
       client = new Client(this, "localhost", 5445);
    }

    @FXML
    private void fxButtonClicked(ActionEvent event) {
        labelResponse.setText("");
        userName = textUserName.getText();
        date = datePicker.getValue();
        if (userName.isBlank()) labelResponse.setText("Please insert a Username!");
        else if (userName.contains(" ")) labelResponse.setText("Spaces are not allowed in usernames!");
        else if (date == null) labelResponse.setText("Please insert a Date!");
        else {
            try {
                String dateText = date.format(DateTimeFormatter.ofPattern("dd.MM.yy"));//TODO send date also over JSON
                JSONMessage msg = new JSONMessage("checkName", userName);
                client.sendUserInput(msg);
            } catch (IllegalArgumentException ex) {
                labelResponse.setText("Please check your Date! (dd.mm.yyyy)");
            }
        }
    }

    public void serverResponse(boolean taken) throws IOException {
        if (!taken) {
            startLoginView();
            //loginStage.close();
        } else {
            labelResponse.setText("Already taken, try again");
        }
    }

    private void startLoginView() throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GameView.fxml"));
        Parent gameView = loader.load();
        GameViewController controller = loader.getController();
        controller.setClient(client);
        client.setGameViewController(controller);
        //setUser(userName);

        loginStage.getScene().setRoot(gameView);
        //client.setController(controller);
        //loginStage.setScene(new Scene(gameView));
        //gameStage.setTitle("Love Letter");
        //gameStage.setScene(new Scene(gameView));
        //gameStage.setResizable(false);
        //gameStage.show();

        /*gameStage.setOnCloseRequest(event -> {
            //controller.close();
            gameStage.close();
            loginStage.close();
        });*/
    }

    /**
     * Methods gets called by the ChatClient if no connection to the server could be established.
     */
    public void noConnection() {
        okButton.setDisable(true);
        labelResponse.setText("No connection to the server!");
    }
}