package client.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.JSONProtocol.JSONMessage;
import utilities.JSONProtocol.body.PlayerAdded;
import utilities.JSONProtocol.body.PlayerStatus;
import utilities.JSONProtocol.body.SendChat;
import utilities.JSONProtocol.body.SetStatus;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class displays the joined and ready users and already has the possibility to chat with other users
 *
 * @author sarah, louis
 */
public class LobbyController extends Controller {
    private static final Logger logger = LogManager.getLogger();


    @FXML
    private TextArea lobbyTextAreaChat;
    @FXML
    private TextField lobbyTextFieldChat;

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
     * the choiceBox where the user can choose if the
     * message should be a direct message or who should be
     * the receiver of the message
     */
    @FXML
    private ChoiceBox<String> directChoiceBox;

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

    public void initialize() {
        directChoiceBox.getItems().add("all");
        directChoiceBox.getSelectionModel().select(0);
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
    }

    public void setTextArea(String messageBody) {
        lobbyTextAreaChat.appendText(messageBody + "\n");
    }

    /**
     * this method displays an user who joined to the lobby
     *
     * @param playerAdded
     */
    public void setJoinedUsers(PlayerAdded playerAdded) {
        String path = "/lobby/" + robotNames[playerAdded.getFigure() - 1] + ".png";
        currentImageView.setImage(new Image(getClass().getResource(path).toString()));
        currentLabel.setText(playerAdded.getName());
        directChoiceBox.getItems().add(playerAdded.getName());
        ImageView imageViewPuffer = currentImageView;
        Label labelPuffer = currentLabel;
        RobotIcon robotIcon = new RobotIcon(robotImageViews.indexOf(currentImageView) + 1, playerAdded, imageViewPuffer, labelPuffer);
        robotIcons.add(robotIcon);
        nextRobot();
    }

    public void setReadyUsersTextArea(PlayerStatus playerStatus) {
        for (RobotIcon robotIcon : robotIcons) {
            if (robotIcon.getUserID() == playerStatus.getId()) {
                String path = "/lobby/" + robotNames[robotIcon.getFigure() - 1];
                if (playerStatus.isReady()) {
                    path +=  "-ready.png";
                } else {
                    path += ".png";
                }
                Image image = new Image(getClass().getResource(path).toString());
                robotIcon.getRobotImageView().setImage(image);
            }
        }
    }

    private void nextRobot() {
        currentImageView = robotImageViews.get(robotImageViews.indexOf(currentImageView) + 1);
        currentLabel = robotLabels.get(robotLabels.indexOf(currentLabel) + 1);

    }

    @FXML
    private void checkBoxAction(ActionEvent event) {
        JSONMessage msg = new JSONMessage(new SetStatus(readyCheckbox.isSelected()));
        client.sendMessage(msg);
    }

    /**
     * send chat Message
     *
     * @param event
     */
    @FXML
    private void submitChatMessage(ActionEvent event) {
        String sendTo = directChoiceBox.getSelectionModel().getSelectedItem();
        logger.info("chose choice: " + sendTo);
        String message = lobbyTextFieldChat.getText();
        JSONMessage jsonMessage;
        if (!message.isBlank()) {
            /*Pattern directPattern = Pattern.compile("^@+");
            Matcher directMatcher = directPattern.matcher(message);

            if (directMatcher.lookingAt()) {
                String destinationUser = (message.split(" ", 2)[0]).substring(1);
                String messageUser = message.split(" ", 2)[1];
                if (!messageUser.isBlank()) //
            */
            if(sendTo.equals("all")){
                jsonMessage = new JSONMessage(new SendChat(message, -1));
                lobbyTextAreaChat.appendText("[You] " + message + "\n");
            } else{
                String destinationUser = sendTo;
                logger.info("playerList contains user" + client.getIDFrom(destinationUser));
                jsonMessage = new JSONMessage(new SendChat(message, client.getIDFrom(destinationUser)));
                lobbyTextAreaChat.appendText("[You] @" + destinationUser + ": " + message + "\n");
            }
            client.sendMessage(jsonMessage);
            } else {
                // TODO
            }
        lobbyTextFieldChat.clear();
        directChoiceBox.getSelectionModel().select(0);
    }

    /**
     * This private class is a data structure to easily connect the different information
     * that are necessary to assign one user to a specific place in the lobby with his image, name etc.
     */
    private class RobotIcon {
        private String userName;
        private int userID;
        private int position;
        private int figure;
        private ImageView robotImageView;
        private Label labelOfUser;

        /**
         * the constructor of RobotIcon where one RobotIcon can be created with the
         * values of one player and the image of the figure he choosed
         * @param position
         * @param playerAdded
         * @param imageViewPuffer
         * @param labelPuffer
         */
        public RobotIcon(int position, PlayerAdded playerAdded, ImageView imageViewPuffer, Label labelPuffer) {
            this.position = position;
            this.userID = playerAdded.getId();
            this.userName = playerAdded.getName();
            this.figure = playerAdded.getFigure();
            this.robotImageView = imageViewPuffer;
            this.labelOfUser = labelPuffer;
        }

        public int getFigure() {
            return figure;
        }

        public String getUserName() {
            return userName;
        }

        public int getUserID() {
            return userID;
        }

        public ImageView getRobotImageView() {
            return robotImageView;
        }

        public Label getLabelOfUser() {
            return labelOfUser;
        }


    }
}



