package client.view;

import game.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.JSONProtocol.body.PlayerStatus;
import utilities.JSONProtocol.body.SetStatus;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class displays the joined and ready users and already has the possibility to chat with other users
 *
 * @author sarah, louis
 */
public class LobbyController extends Controller {
    private static final Logger logger = LogManager.getLogger();

    @FXML
    private BorderPane chatPane;

    @FXML
    private CheckBox readyCheckbox;
    /**
     * ImageView of the first possible user
     */
    @FXML
    private ImageView robot1ImageView;
    /**
     * ImageView of the second possible user
     */
    @FXML
    private ImageView robot2ImageView;
    /**
     * ImageView of the third possible user
     */
    @FXML
    private ImageView robot3ImageView;
    /**
     * ImageView of the fourth possible user
     */
    @FXML
    private ImageView robot4ImageView;
    /**
     * ImageView of the fifth possible user
     */
    @FXML
    private ImageView robot5ImageView;
    /**
     * ImageView of the sixth possible user
     */
    @FXML
    private ImageView robot6ImageView;
    /**
     * Label of the first possible user
     */
    @FXML
    private Label robot1Label;
    /**
     * Label of the second possible user
     */
    @FXML
    private Label robot2Label;
    /**
     * Label of the third possible user
     */
    @FXML
    private Label robot3Label;
    /**
     * Label of the fourth possible user
     */
    @FXML
    private Label robot4Label;
    /**
     * Label of the fifth possible user
     */
    @FXML
    private Label robot5Label;
    /**
     * Label of the sixth possible user
     */
    @FXML
    private Label robot6Label;
    /**
     * the currentImageView that gets filled if the next user
     * joins (all robotImageViews before  have already been filled)
     */
    private ImageView currentImageView;
    /**
     * the Label that gets filled if the next user
     * joins (all Labels before have already been filled)
     */
    private Label currentLabel;

    /**
     * In robotImageViews the different ImageViews that can be assigned
     * are stored together
     */
    private ArrayList<ImageView> robotImageViews = new ArrayList<>();
    /**
     * In robotLabels the different Labels that can be assigned
     * are stored together
     */
    private ArrayList<Label> robotLabels = new ArrayList<>();
    /**
     * In robotIcons
     */
    private ArrayList<RobotIcon> robotIcons = new ArrayList<>();

    /**
     * this method gets called automatically by constructing view
     * it adds the different ImageViews and Labels to lists and also
     * sets the default of the choiceBox to all. Additionally the current imageView
     * and label are assigned
     */
    @FXML
    public void initialize() {
        robotImageViews.add(robot1ImageView);
        robotImageViews.add(robot2ImageView);
        robotImageViews.add(robot3ImageView);
        robotImageViews.add(robot4ImageView);
        robotImageViews.add(robot5ImageView);
        robotImageViews.add(robot6ImageView);

        robotLabels.add(robot1Label);
        robotLabels.add(robot2Label);
        robotLabels.add(robot3Label);
        robotLabels.add(robot4Label);
        robotLabels.add(robot5Label);
        robotLabels.add(robot6Label);

        currentImageView = robot1ImageView;
        currentLabel = robot1Label;
        GameViewController.setLobbyController(this);
    }

    public void attachChatPane(Pane chat) {
        chat.setPrefWidth(chatPane.getPrefWidth());
        chat.setPrefHeight(chatPane.getPrefHeight());
        chatPane.setCenter(chat);
    }

    /**
     * this method displays an user who joined to the lobby
     * with its choosed robot, name and also the name is added to the choicebox
     * so that other users in lobby can send direct messages.
     * Also
     */
    public void setJoinedUsers(Player player, boolean thisUser) {
        String path = "/lobby/" + robotNames[player.getFigure()] + ".png";
        String newName = player.getName() + " " + player.getID();
        currentImageView.setImage(new Image(getClass().getResource(path).toString()));

        currentLabel.setText(newName);
        ImageView imageViewPuffer = currentImageView;
        Label labelPuffer = currentLabel;

        int position = robotImageViews.indexOf(currentImageView) + 1;
        RobotIcon robotIcon = new RobotIcon(position, player, imageViewPuffer, labelPuffer, thisUser);
        addRobotIcon(robotIcon);
        robotIcons.add(robotIcon);


        nextRobot();
    }


    /**
     * The robot image of the user who clicked the ready button gets changed. Now the icon has a pink
     * background to signal the ready status.
     *
     * @param playerStatus
     */
    public void displayStatus(PlayerStatus playerStatus) {
        for (RobotIcon robotIcon : robotIcons) {
            if (robotIcon.getUserID() == playerStatus.getId()) {
                String path = "/lobby/" + robotNames[robotIcon.getFigure()];
                if (playerStatus.isReady()) path += "-ready.png";
                else path += ".png";
                Image image = new Image(getClass().getResource(path).toString());
                robotIcon.getRobotImageView().setImage(image);
            }
        }
    }

    /**
     * The next free place in the lobby is presented by the next current Label and
     * ImageView.
     */
    private void nextRobot() {
        currentImageView = robotImageViews.get(robotImageViews.indexOf(currentImageView) + 1);
        currentLabel = robotLabels.get(robotLabels.indexOf(currentLabel) + 1);

    }

    /**
     * by clicking the ready checkbox a message will be send to the client (and then to the server)
     * to signal the ready status of the user.
     *
     * @param event
     */
    @FXML
    private void checkBoxAction(ActionEvent event) {
        client.sendMessage(new SetStatus(readyCheckbox.isSelected()));
    }


}



