package client.view;

import client.Main;
import client.model.Client;
import Utilities.JSONProtocol.JSONMessage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static Utilities.Utilities.PORT;

public class LoginController {
    private Stage loginStage;
    private Client client;
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

    @FXML
    private ListView listView;

    private ObservableList<ImageView> robotList =FXCollections.observableArrayList ();


    private Main main;

    public LoginController() {
    }

    public void setStage(Stage loginStage) {
        this.loginStage = loginStage;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void close() {
        //client.disconnect(); TODO disconnect client on closure of window
    }
    public void initialize(){
        createRobotList();
        labelResponse.setText("test");
        listView.setItems(robotList);

    }
   public void begin() {
       client = new Client(this, "localhost", PORT);
    }
    private void createRobotList(){
        ObservableList<String> robotNames = FXCollections.observableArrayList ("hulkX90", "hammerbot", "smashbot",
                "twonky", "spinbot", "zoombot");
        ImageView robot;
        Image robotImage;
        double scaleSize = 50;
        for (String robotName: robotNames){
            robotImage = new Image("/choose-robot-" + robotName +".png");
            robot = new ImageView(robotImage);
            robot.setFitHeight(scaleSize);
            robot.setFitWidth(scaleSize);
            robotList.add(robot);
        }


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
                //TODO
                String dateText = date.format(DateTimeFormatter.ofPattern("dd.MM.yy"));//TODO send date also over JSON
            } catch (IllegalArgumentException ex) {
                labelResponse.setText("Please check your Date! (dd.mm.yyyy)");
            }
        }
    }

    public void serverResponse(boolean taken) {
        if (!taken) {
            main.showGameStage();
            //loginStage.close();
        } else {
            labelResponse.setText("Already taken, try again");
        }
    }

    /**
     * Methods gets called by the ChatClient if no connection to the server could be established.
     */
    public void noConnection() {
        okButton.setDisable(true);
        labelResponse.setText("No connection to the server!");
    }

    public void write(String message) {

        labelResponse.setText(message);
    }
    public Label getLabelResponse (){
        return labelResponse;
    }

    public void setMain(Main main) {
        this.main = main;
    }
}