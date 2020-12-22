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


/**
 * This class controls the loginView.fxml view, it takes the name and the chosed robot, sends it to
 * client and switches to the game view
 * @author sarah,
 */
public class LoginController {
    /**
     * This private class represents a robot with its name and id
     */
    private class RobotPrivate{
        int id = 0;
        String name = "default";

        /**
         * constructor of RobotPrivate
         * @param robotName of the robot
         * @param robotId of the robot
         */
        public RobotPrivate(String robotName, int robotId) {
            this.id = robotId +1;
            this.name = robotName;
        }
        public String getRobotName() {
            return this.name;
        }
        public int getRobotID() {
            return this.id;
        }
    }

    /**
     *  the stage gets saved
     */
    private Stage loginStage;

    /**
     * the client which gets created and who gets informed about actions
     */
    private Client client;

    /**
     * the user can choose an username which gets saved and passed on to the client
     */
    private String userName;

    /**
     * the user can type in its name
     */
    @FXML
    private TextField textUserName;

    /**
     * a label to check if everything works //TODO delete or change purpose
     */
    @FXML
    private Label labelResponse;

    /**
     * the button for checking whether input is valid
     */
    @FXML
    private Button okButton;

    /**
     *  the listView for choosing one robot, it stores different ImageViews
     */
    @FXML
    private ListView listView;


    /**
     * it stores the imageViews of the different robots
     */
    private ObservableList<ImageView> robotImageViewList =FXCollections.observableArrayList ();
    /**
     *
     */
    private ObservableList<RobotPrivate> robotList =FXCollections.observableArrayList ();

    /**
     *
     */
    private String [] robotNames = {"hulkX90", "hammerbot", "smashbot",
            "twonky", "spinbot", "zoombot"};

    /**
     *
     */
    private Main main;

    /**
     *
     */
    public LoginController() {
    }

    /**
     *
     * @param loginStage
     */
    public void setStage(Stage loginStage) {
        this.loginStage = loginStage;
    }


    /**
     *
     */
    public void close() {
        //client.disconnect(); TODO disconnect client on closure of window
    }

    /**
     *
     */
    public void initialize(){
        createRobotList();
        labelResponse.setText("test");
        listView.setItems(robotImageViewList);
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

    }

    /**
     *
     */
   public void begin() {
       client = new Client(this, "localhost", PORT);
   }

    /**
     *
     */
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

    /**
     *
     * @param event
     */
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
                labelResponse.setText("you chose " + robotList.get(choosedRobot).getRobotName() + " with id " + robotList.get(choosedRobot).getRobotID());
                //TODO
            } catch (IllegalArgumentException ex) {
                labelResponse.setText("Please check your Date! (dd.mm.yyyy)");
            }
        }
    }

    /**
     *
     * @param taken
     */
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

    /**
     *
     * @param message
     */
    public void write(String message) {

        labelResponse.setText(message);
    }

    /**
     *
     * @return
     */
    public Label getLabelResponse (){
        return labelResponse;
    }

    /**
     *
     * @param main
     */
    public void setMain(Main main) {
        this.main = main;
    }
}