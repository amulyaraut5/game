package client.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.JSONProtocol.JSONBody;
import utilities.JSONProtocol.body.PlayerValues;


/**
 * This class controls the loginView.fxml view, it takes the name and the chosen robot, sends it to
 * client and switches to the game view
 *
 * @author sarah,
 */
public class LoginController extends Controller {
    private static final Logger logger = LogManager.getLogger();
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
     * the listView for choosing one robot, it stores different ImageViews
     */
    @FXML
    private ListView listView;
    /**
     * it stores the imageViews of the different robots,
     * so that name and id from the choosed robot
     * can be recognized
     */
    private ObservableList<ImageView> robotImageViewList = FXCollections.observableArrayList();
    /**
     * this list stores the different robots (with name and id)
     */
    private ObservableList<RobotPrivate> robotList = FXCollections.observableArrayList();

    /**
     *
     */
    public void close() {
        //client.disconnect(); TODO disconnect client on closure of window
    }

    /**
     * by initializing the view the listView gets filled with the imageViews of the robots and
     * it makes sure that only one item of the listView can get clicked
     */
    public void initialize() {
        createRobotList();
        listView.setItems(robotImageViewList);
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

    }

    /**
     * This method creates a list with the robots and additional it
     * adds imageViews for each robot to another list
     */
    private void createRobotList() {
        ImageView robot;
        Image robotImage;
        double scaleSize = 50;
        for (int i = 0; i < robotNames.length; i++) {
            String path = "/lobby/" + robotNames[i] + ".png";
            robot = new ImageView(new Image(getClass().getResource(path).toString()));
            robot.setFitHeight(scaleSize);
            robot.setFitWidth(scaleSize);

            RobotPrivate robotPrivate = new RobotPrivate(robotNames[i], i);
            robotList.add(robotPrivate);
            robotImageViewList.add(robot);
        }

    }

    /**
     * This method gets called by clicking on the button, it checks if the username is
     * valid and if a robot is selected and then it sends a PlayerValues protocol message
     * and switches to the gameStage
     *
     * @param event
     */
    @FXML
    private void fxButtonClicked(ActionEvent event) {
        labelResponse.setText("");
        userName = textUserName.getText();
        int chosenRobot = listView.getSelectionModel().getSelectedIndex();
        if (userName.isBlank()) labelResponse.setText("Please insert a Username!");
        else if (chosenRobot < 0) labelResponse.setText("You have to choose a robot!");
        else if (client.playerListContains(robotList.get(chosenRobot).getRobotID()))
            labelResponse.setText("This robot is already taken!"); //TODO catch also in server and send error protocol
        else {
            labelResponse.setText("you chose " + robotList.get(chosenRobot).getRobotName() + " with id " + robotList.get(chosenRobot).getRobotID());
            JSONBody jsonBody = new PlayerValues(userName, robotList.get(chosenRobot).getRobotID());
            client.sendMessage(jsonBody);
            viewManager.nextScene();

        }
    }

    public void setImageViewDisabled(int figure) {
        //TODO set cell of figure not selectable with cellfactory from initialize method
        //robotImageViewList.get(figure).setDisable(true);
        //robotImageViewList.get(figure).setMouseTransparent(true);
    }


    /**
     * @param taken
     */
    public void serverResponse(boolean taken) {
        //TODO username can't be already taken, only robot could be taken
        if (taken) {
            labelResponse.setText("Already taken, try again");
        } else {
            viewManager.nextScene();
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
     * @param message
     */
    public void write(String message) {
        labelResponse.setText(message);
    }


}