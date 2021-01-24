package client.view;

import game.Player;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.ImageHandler;
import utilities.JSONProtocol.JSONMessage;
import utilities.JSONProtocol.body.PlayerStatus;
import utilities.JSONProtocol.body.SetStatus;
import utilities.Updateable;

import java.io.InputStream;
import java.util.HashMap;

/**
 * This class displays the joined and ready users and already has the possibility to chat with other users
 *
 * @author sarah, louis
 */
public class LobbyController extends Controller implements Updateable {
    private static final Logger logger = LogManager.getLogger();
    private final HashMap<Player, VBox> playerTiles = new HashMap<>();

    @FXML
    private BorderPane chatPane;
    @FXML
    private CheckBox readyCheckbox;
    @FXML
    private FlowPane playerTilePane;
    @FXML
    private Label infoLabel;

    /**
     * this method gets called automatically by constructing view
     * it adds the different ImageViews and Labels to lists and also
     * sets the default of the choiceBox to all. Additionally the current imageView
     * and label are assigned
     */
    @FXML
    public void initialize() {
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
        String name = client.getUniqueName(player.getID());
        ImageView imageView = ImageHandler.createImageView(path, 90, 90);

        Label label = new Label(name);
        label.setPrefWidth(90);
        label.setPrefHeight(30);
        label.setAlignment(Pos.TOP_CENTER);

        VBox group = new VBox(imageView, label);
        playerTilePane.getChildren().add(group);
        playerTiles.put(player, group);
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
        InputStream stream = ImageHandler.class.getResourceAsStream(path);
        Image image = new Image(stream, 90, 90, true, true);
        ImageView imageView = (ImageView) playerTiles.get(player).getChildren().get(0);
        imageView.setImage(image);
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
        VBox tile = playerTiles.get(player);
        playerTilePane.getChildren().remove(tile);
    }

    @Override
    public void update(JSONMessage message) {

    }

    public void displayError(String error) {
        infoLabel.setText(error);
    }
}