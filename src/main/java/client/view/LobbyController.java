package client.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
    public Label robot1Label;
    public Label robot2Label;
    public Label robot3Label;
    public Label robot4Label;
    public Label robot5Label;
    public Label robot6Label;
    private ImageView currentImageView;
    private Label currentLabel;

    public VBox textVBox;
    public AnchorPane scrollPaneAnchor;
    public ScrollPane chatScrollPane;
    public VBox scrollVBox;

    public ChoiceBox<String> directChoiceBox;
    private ArrayList<ImageView> robotImageViews = new ArrayList<>();
    private ArrayList<Label> robotLabels = new ArrayList<>();
    private ArrayList<RobotIcon> robotIcons = new ArrayList<>();

    public void initialize() {
        directChoiceBox.getItems().add("all");
        directChoiceBox.getSelectionModel().select(0);
        scrollVBox.setAlignment(Pos.TOP_CENTER);
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
        TextArea chatText = new TextArea(messageBody);
        chatText.setMaxHeight(20);
        chatText.setMaxWidth(110);
        HBox hBox = new HBox(chatText);
        hBox.setAlignment(Pos.TOP_LEFT);
        scrollVBox.getChildren().add(hBox);
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
        String store = "";
        if(message.length()>=10){
            String sub1 = message.substring(0, 10);
            String sub2 = message.substring(11, message.length()-1);
            store = sub1 + "\n" + sub2;
        } else store = message;
        JSONMessage jsonMessage;
        if (!message.isBlank()) {
            /*Pattern directPattern = Pattern.compile("^@+");
            Matcher directMatcher = directPattern.matcher(message);

            if (directMatcher.lookingAt()) {
                String destinationUser = (message.split(" ", 2)[0]).substring(1);
                String messageUser = message.split(" ", 2)[1];
                if (!messageUser.isBlank()) //
            */
            TextArea youMessage = new TextArea(store + "\n");
            HBox hBox = new HBox(youMessage);
            hBox.setAlignment(Pos.TOP_RIGHT);

            //Label label = new Label("[You]: " + message + "\n");
            //label.setAlignment(Pos.TOP_RIGHT);
            // label.setStyle("-fx-font-alignment: right");
            youMessage.setMaxHeight(20);
            youMessage.setMaxWidth(110);
            //chatText.setAlignment(Pos.TOP_RIGHT);
            //chatText.alignmentProperty().setValue(Pos.TOP_RIGHT);
            //chatText.setStyle("-fx-text-alignment: right;");

            if(sendTo.equals("all")){
                jsonMessage = new JSONMessage(new SendChat(store, -1));
                //scrollVBox.setAlignment(Pos.TOP_RIGHT);
                //chatText.centerShapeProperty().setValue(false);
                //chatText.
                //textVBox.getChildren().add(new TextArea("hallo"));
                //scrollPaneAnchor.getChildren().add(new TextArea("hallo"));
                //chatScrollPane.getContent().
                lobbyTextAreaChat.appendText("[You]: " + message + "\n");
                //scrollVBox.getChildren().add(hBox);
            } else{
                String destinationUser = sendTo;
                youMessage.setText("@" + destinationUser + " " + store + "\n");
                //chatText.setStyle("-fx-text-fill: green;");
                //scrollVBox.setAlignment(Pos.TOP_LEFT);
                //scrollVBox.getChildren().add(chatText);
                logger.info("playerList contains user" + client.getIDFrom(destinationUser));
                jsonMessage = new JSONMessage(new SendChat(store, client.getIDFrom(destinationUser)));
                lobbyTextAreaChat.appendText("[You]: @" + destinationUser + " " + message + "\n");
            }
            scrollVBox.getChildren().add(hBox);
            client.sendMessage(jsonMessage);
            } else {
                // TODO
            }
        lobbyTextFieldChat.clear();
        directChoiceBox.getSelectionModel().select(0);
    }

    private class RobotIcon {
        String userName;
        int userID;
        int position;
        int figure;
        ImageView robotImageView;
        Label labelOfUser;

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



