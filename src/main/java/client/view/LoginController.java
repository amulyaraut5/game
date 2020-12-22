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
    private class RobotPrivate{
        int id = 0;
        String name = "default";

        public RobotPrivate(String name, int id) {
            this.id = id++;
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        public int getID() {
            return this.id;
        }
    }
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



    private ObservableList<ImageView> robotImageViewList =FXCollections.observableArrayList ();
    private ObservableList<RobotPrivate> robotList =FXCollections.observableArrayList ();

    private String [] robotNames = {"hulkX90", "hammerbot", "smashbot",
            "twonky", "spinbot", "zoombot"};


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
        listView.setItems(robotImageViewList);
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

    }
   public void begin() {
       client = new Client(this, "localhost", PORT);
    }

   private void createRobotList(){
        ImageView robot;
        Image robotImage;
        double scaleSize = 50;
        /*
        for (String robotName: robotNames){
            robotImage = new Image("/choose-robot-" + robotName +".png");
            robot = new ImageView(robotImage);
            robot.setFitHeight(scaleSize);
            robot.setFitWidth(scaleSize);
            robotList.add(robot);
        }*/
        for (int i = 0; i<robotNames.length; i++){
            robotImage = new Image("/choose-robot-" + robotNames[i] +".png");
            robot = new ImageView(robotImage);
            robot.setFitHeight(scaleSize);
            robot.setFitWidth(scaleSize);

            RobotPrivate robotPrivate = new RobotPrivate(robotNames[i], i);
            robotList.add(robotPrivate);
            robotImageViewList.add(robot);
        }




    }
    @FXML
    private void fxButtonClicked(ActionEvent event) {

        labelResponse.setText("");
        userName = textUserName.getText();
        int choosedRobot = listView.getSelectionModel().getSelectedIndex();
        if (userName.isBlank()) labelResponse.setText("Please insert a Username!");
        else if (userName.contains(" ")) labelResponse.setText("Spaces are not allowed in usernames!");
        else if (choosedRobot<0) labelResponse.setText("You have to choose a robot");
        else {
            try {
                labelResponse.setText("you chose " + robotList.get(choosedRobot).getName() + " with id " + robotList.get(choosedRobot).getID());
                //TODO
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