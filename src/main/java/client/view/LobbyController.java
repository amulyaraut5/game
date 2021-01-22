package client.view;

import game.Player;
import javafx.fxml.FXML;
import javafx.scene.Group;
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
    /**
     * In robotImageViews the different ImageViews that can be assigned
     * are stored together
     */
    private final ArrayList<ImageView> robotImageViewPuffer = new ArrayList<>();
    /**
     * In robotLabels the different Labels that can be assigned
     * are stored together
     */
    private final ArrayList<Label> robotLabels = new ArrayList<>();
    private HashMap<Player, Group> robotImageViews = new HashMap<>();
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
     * this method gets called automatically by constructing view
     * it adds the different ImageViews and Labels to lists and also
     * sets the default of the choiceBox to all. Additionally the current imageView
     * and label are assigned
     */
    @FXML
    public void initialize() {
        robotImageViewPuffer.add(robot1ImageView);
        robotImageViewPuffer.add(robot2ImageView);
        robotImageViewPuffer.add(robot3ImageView);
        robotImageViewPuffer.add(robot4ImageView);
        robotImageViewPuffer.add(robot5ImageView);
        robotImageViewPuffer.add(robot6ImageView);

        robotLabels.add(robot1Label);
        robotLabels.add(robot2Label);
        robotLabels.add(robot3Label);
        robotLabels.add(robot4Label);
        robotLabels.add(robot5Label);
        robotLabels.add(robot6Label);

        currentImageView = robot1ImageView;
        currentLabel = robot1Label;
    }

    public void attachChatPane(Pane chat) {
        chat.setPrefWidth(chatPane.getPrefWidth());
        chat.setPrefHeight(chatPane.getPrefHeight());
        chatPane.setCenter(chat);
    }

    /**
     * this method displays an user who joined to the lobby
     * with its chosen robot, name and also the name is added to the ChoiceBox
     * so that other users in lobby can send direct messages.
     * Also
     */
    public void addJoinedPlayer(Player player) {
        String path = "/lobby/" + robotNames[player.getFigure()] + ".png";
        String newName = client.getUniqueName(player.getID());
        currentImageView.setImage(new Image(getClass().getResource(path).toString()));

        currentLabel.setText(newName);
        ImageView imageViewPuffer = currentImageView;
        Label labelPuffer = currentLabel;

        int position = robotImageViewPuffer.indexOf(currentImageView) + 1;

        Group group = new Group(imageViewPuffer, labelPuffer);
        robotImageViews.put(player, group);

        nextRobot();
    }


    /**
     * The robot image of the user who clicked the ready button gets changed. Now the icon has a pink
     * background to signal the ready status.
     *
     * @param playerStatus
     */
    public void displayStatus(PlayerStatus playerStatus) {
        Player player = client.getPlayerFromID(playerStatus.getID());
        String path = "/lobby/" + robotNames[player.getFigure()];
        if (playerStatus.isReady()) path += "-ready.png";
        else path += ".png";
        Image image = new Image(getClass().getResource(path).toString());
        ImageView imageView = (ImageView) robotImageViews.get(player).getChildren().get(0);
        imageView.setImage(image);
    }

    /**
     * The next free place in the lobby is presented by the next current Label and
     * ImageView.
     */
    private void nextRobot() {
        currentImageView = robotImageViewPuffer.get(robotImageViewPuffer.indexOf(currentImageView) + 1);
        currentLabel = robotLabels.get(robotLabels.indexOf(currentLabel) + 1);

    }

    /**
     * by clicking the ready checkbox a message will be send to the client (and then to the server)
     * to signal the ready status of the user.
     */
    @FXML
    private void checkBoxAction() {
        client.sendMessage(new SetStatus(readyCheckbox.isSelected()));
    }

    public void removePlayer(Player player) {
        ImageView imageView = (ImageView) robotImageViews.get(player).getChildren().get(0);
        Label label = (Label) robotImageViews.get(player).getChildren().get(0);

        imageView.setImage(null);
        label.setText("");
    }
}