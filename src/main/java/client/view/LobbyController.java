package client.view;

import client.ViewManager;
import client.model.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.JSONProtocol.JSONMessage;
import utilities.JSONProtocol.body.*;
import utilities.JSONProtocol.body.gameStarted.Field;
import utilities.JSONProtocol.body.gameStarted.Maps;

import java.util.ArrayList;

/**
 * This class displays the joined and ready users and already has the possibility to chat with other users
 *
 * @author sarah, louis
 */
public class LobbyController extends Controller {
    private static final Logger logger = LogManager.getLogger();


    @FXML
    public TextArea lobbyTextAreaChat;
    @FXML
    public TextField lobbyTextFieldChat;

    @FXML
    public CheckBox readyCheckbox;

    public TextArea joinedUsersTextArea;

    public TextArea readyUsersTextArea;

    public ImageView robot1ImageView;
    public ImageView robot2ImageView;
    public ImageView robot3ImageView;
    public ImageView robot4ImageView;
    public ImageView robot5ImageView;
    public ImageView robot6ImageView;
    private ImageView currentImageView;

    public Label robot1Label;
    public Label robot2Label;
    public Label robot3Label;
    public Label robot4Label;
    public Label robot5Label;
    public Label robot6Label;
    private Label currentLabel;

    private ArrayList<ImageView> robotImageViews = new ArrayList<>();
    private ArrayList<Label> robotLabels = new ArrayList<>();
    private ArrayList<RobotIcon> robotIcons = new ArrayList<>();

    public void initialize(){
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
    private void nextRobot(){
        currentImageView = robotImageViews.get(robotImageViews.indexOf(currentImageView)+1);
        currentLabel = robotLabels.get(robotLabels.indexOf(currentLabel)+1);

    }
    @FXML
    private void checkBoxAction(ActionEvent event) {
        JSONMessage msg = new JSONMessage(new SetStatus(readyCheckbox.isSelected()));
        client.sendMessage(msg);
    }

    /**
     * send chat Message
     * @param event
     */
    @FXML
    private void submitChatMessage(ActionEvent event) {
            String message = lobbyTextFieldChat.getText();
            if (!message.isBlank()) {
                lobbyTextAreaChat.appendText("[You]: " + message + "\n");
                JSONMessage msg = new JSONMessage(new SendChat(message, -1));
                client.sendMessage(msg);
            }
            lobbyTextFieldChat.clear();
        }

    public  void setTextArea(String messageBody){
        lobbyTextAreaChat.appendText(messageBody + "\n");
    }
    /**
     * this method displays an user who joined to the lobby
     *
     * @param playerAdded
     */
    public void setJoinedUsersTextArea(PlayerAdded playerAdded) {
        currentImageView.setImage(new Image("/choose-robot-" + robotNames[playerAdded.getFigure()-1] + ".png"));
        currentLabel.setText(playerAdded.getName());
        ImageView imageViewPuffer = currentImageView;
        Label labelPuffer = currentLabel;
        RobotIcon robotIcon = new RobotIcon(robotImageViews.indexOf(currentImageView)+1, playerAdded,imageViewPuffer, labelPuffer );
        robotIcons.add(robotIcon);
        nextRobot();
    }
    public void setReadyUsersTextArea(PlayerStatus playerStatus){
                for (RobotIcon robotIcon: robotIcons){
                    if(robotIcon.getUserID()==playerStatus.getId()){
                        String link;
                        if(playerStatus.isReady()){
                            link = "/choose-robot-" + robotNames[robotIcon.getFigure()-1] + "-ready.png";
                        } else {
                            link = "/choose-robot-" + robotNames[robotIcon.getFigure()-1] + ".png";
                        }
                        robotIcon.getImageViewofRobot().setImage(new Image(link));
                    }
                }
    }

    private class RobotIcon{
        String userName;
        int userID;
        int position;
        int figure;
        ImageView imageViewofRobot;
        Label labelOfUser;

        public RobotIcon(int position, PlayerAdded playerAdded, ImageView imageViewPuffer, Label labelPuffer) {
            this.position =position;
            this.userID = playerAdded.getId();
            this.userName = playerAdded.getName();
            this.figure = playerAdded.getFigure();
            this.imageViewofRobot = imageViewPuffer;
            this.labelOfUser = labelPuffer;
        }
        public int getFigure(){
            return figure;
        }
        public String getUserName() {
            return userName;
        }

        public int getUserID() {
            return userID;
        }

        public ImageView getImageViewofRobot() {
            return imageViewofRobot;
        }

        public Label getLabelOfUser() {
            return labelOfUser;
        }

        public void setLabelOfUser(Label labelOfUser) {
            this.labelOfUser = labelOfUser;
        }

    }
}



