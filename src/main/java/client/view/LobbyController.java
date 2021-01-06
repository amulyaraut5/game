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


    public Label chatMessageLabel;
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

    /**
     * The messages received from other users are printed
     * in the chatTextArea
     * @param messageBody
     */
    public void setTextArea(String messageBody) {
        lobbyTextAreaChat.appendText(messageBody + "\n");
    }

    /**
     * this method displays an user who joined to the lobby
     * with its choosed robot, name and also the name is added to the choicebox
     * so that other users in lobby can send direct messages.
     * Also
     *
     * @param playerAdded
     */
    public void setJoinedUsers(PlayerAdded playerAdded) {
        String path = "/lobby/" + robotNames[playerAdded.getFigure() - 1] + ".png";
        String newName = playerAdded.getName() + " " + playerAdded.getID();
        currentImageView.setImage(new Image(getClass().getResource(path).toString()));
        currentLabel.setText(newName);
        directChoiceBox.getItems().add(newName);
        ImageView imageViewPuffer = currentImageView;
        Label labelPuffer = currentLabel;
        RobotIcon robotIcon = new RobotIcon(robotImageViews.indexOf(currentImageView) + 1, playerAdded, imageViewPuffer, labelPuffer);
        robotIcons.add(robotIcon);
        nextRobot();
    }

    /**
     * The robot image of the user who clicked the ready button gets changed. Now the icon has a pink
     * background to signal the ready status.
     * @param playerStatus
     */
    public void displayPlayerStatus(PlayerStatus playerStatus) {
        for (RobotIcon robotIcon : robotIcons) {
            if (robotIcon.getUserID() == playerStatus.getId()) {
                String path = "/lobby/" + robotNames[robotIcon.getFigure() - 1];
                if (playerStatus.isReady()) path +=  "-ready.png";
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
     * @param event
     */
    @FXML
    private void checkBoxAction(ActionEvent event) {
        JSONMessage msg = new JSONMessage(new SetStatus(readyCheckbox.isSelected()));
        client.sendMessage(msg);
    }

    /**
     * send a chat Message, either private or to everyone
     *
     * @param event
     */
    @FXML
    private void submitChatMessage(ActionEvent event) {
        chatMessageLabel.setText("");
        String sendTo = directChoiceBox.getSelectionModel().getSelectedItem();
        logger.info("chose choice: " + sendTo);
        String message = lobbyTextFieldChat.getText();
        JSONMessage jsonMessage;
        if (!message.isBlank()) {
            if(sendTo.equals("all")){
                jsonMessage = new JSONMessage(new SendChat(message, -1));
                lobbyTextAreaChat.appendText("[You] " + message + "\n");
            } else {
                String [] userInformation = sendTo.split(" ");
                String destinationUser = "";
                for(int i = 0; i<userInformation.length-1; i++) destinationUser += userInformation[i] + " ";
                String idUser = userInformation[userInformation.length-1];
                destinationUser = destinationUser.substring(0, destinationUser.length()-1);
                logger.info("playerList contains user " + client.getIDFrom(destinationUser) + " id is " + idUser);
                jsonMessage = new JSONMessage(new SendChat(message, Integer.parseInt(idUser)));
                lobbyTextAreaChat.appendText("[You] @" + destinationUser + ": " + message + "\n");
            }
            client.sendMessage(jsonMessage);
            } else {
                chatMessageLabel.setText("Your message was empty.");
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
            this.userID = playerAdded.getID();
            this.userName = playerAdded.getName() + " " + playerAdded.getID();
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



