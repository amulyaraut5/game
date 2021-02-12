package client.view;

import game.Player;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
import utilities.JSONProtocol.body.Error;
import utilities.JSONProtocol.body.*;
import utilities.Updatable;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class displays the joined and ready users and already has the possibility to chat with other users
 *
 * @author sarah, louis, simon
 */
public class LobbyController extends Controller implements Updatable {
    private static final Logger logger = LogManager.getLogger();
    /**
     * HashMap with a player and its related VBox.
     */
    private final HashMap<Player, VBox> playerIcons = new HashMap<>();

    /**
     * The chatPane contains the pane.
     */
    @FXML
    private BorderPane chatPane;

    /**
     * The checkBox for the player to set its status.
     */
    @FXML
    private CheckBox readyCheckbox;

    /**
     * The playerIconPane which contains all different vBoxes of players.
     */
    @FXML
    private FlowPane playerIconPane;

    /**
     * The label which displays errors.
     */
    @FXML
    private Label infoLabel;

    @FXML
    private ImageView mapImageView;

    @FXML
    private Label mapLabel;

    @FXML
    private Pane mapSelectionPane;

    @FXML
    private Pane overlayPane;

    /**
     * This method sets the chat in the chatPane with its width and height.
     *
     * @param chat the chatPane
     */
    public void attachChatPane(Pane chat) {
        chat.setPrefWidth(chatPane.getPrefWidth());
        chat.setPrefHeight(chatPane.getPrefHeight());
        chatPane.setCenter(chat);
    }

    /**
     * This method displays an user who joined to the lobby
     * with its chosen robot, name and also the name is added to the ChoiceBox
     * so that other users in lobby can send (direct) messages.
     *
     * @param player the player who joins
     */
    public void addJoinedPlayer(Player player) {
        String path = "/lobby/" + robotNames[player.getFigure()] + ".png";
        String name = viewClient.getUniqueName(player.getID());
        player.setUniqueName(name);
        ImageView imageView = ImageHandler.createImageView(path, 90, 90);

        Label label = new Label(name);
        label.setPrefWidth(90);
        label.setPrefHeight(30);
        label.setAlignment(Pos.TOP_CENTER);
        label.setStyle("-fx-text-fill: midnightblue");
        VBox group = new VBox(imageView, label);
        playerIconPane.getChildren().add(group);
        playerIcons.put(player, group);
    }

    /**
     * The robot image of the user who clicked the ready button gets changed. Now the icon has a pink
     * background to signal the ready status.
     *
     * @param playerStatus if the player is ready or not
     */
    private void displayStatus(PlayerStatus playerStatus) {
        Player player = viewClient.getPlayerFromID(playerStatus.getID());
        String path = "/lobby/" + robotNames[player.getFigure()];
        if (playerStatus.isReady()) path += "-ready.png";
        else path += ".png";
        InputStream stream = ImageHandler.class.getResourceAsStream(path);
        Image image = new Image(stream, 90, 90, true, true);
        ImageView imageView = (ImageView) playerIcons.get(player).getChildren().get(0);
        imageView.setImage(image);
    }

    /**
     * This method removes a player from the lobby by exiting.
     *
     * @param player that exits
     */
    public void removePlayer(Player player) {
        VBox tile = playerIcons.get(player);
        playerIconPane.getChildren().remove(tile);
    }

    /**
     * This method overwrites the method from Client and handles the JSONMessage from the server
     * that are for the LobbyController and handles them.
     *
     * @param message that the player received from the server for the lobby
     */
    @Override
    public void update(JSONMessage message) {
        switch (message.getType()) {
            case Error -> {
                Error error = (Error) message.getBody();
                Updatable.showInfo(infoLabel, error.getError());
            }
            case PlayerStatus -> {
                PlayerStatus playerStatus = (PlayerStatus) message.getBody();
                displayStatus(playerStatus);
            }
            case SelectMap -> {
                SelectMap selectMap = (SelectMap) message.getBody();
                if(!mapSelected)
                    showMapView(selectMap.getAvailableMaps());
            }
            case MapSelected -> {
                MapSelected msg = (MapSelected) message.getBody();
                mapLabel.setText(msg.getMap().get(0));
                try {
                    InputStream path = getClass().getResourceAsStream("/maps/" + msg.getMap().get(0) + ".PNG");
                    Image mapImage = new Image(path, 200, 200, true, true);
                    mapImageView.setImage(mapImage);
                    mapImageView.setVisible(true);
                } catch (NullPointerException e) {
                    mapImageView.setVisible(false);
                }
            }
        }
    }

    private void showMapView(ArrayList<String> availableMaps) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/innerViews/mapView.fxml"));
            mapSelectionPane.getChildren().add(fxmlLoader.load());
            MapController mapController = fxmlLoader.getController();
            mapController.setMaps(availableMaps, this);
        } catch (IOException e) {
            logger.error("GameBoard could not be created: " + e.getMessage());
        }
        overlayPane.setVisible(true);
    }

    public void mapSelected(String map) {
            overlayPane.setVisible(false);
            viewClient.sendMessage(new MapSelected(map));
            mapSelected = true;


    }

    public boolean mapSelected = false;
    /**
     * By clicking the ready checkbox a message will be send to the client (and then to the server)
     * to signal the ready status of the user.
     */
    @FXML
    private void checkBoxAction() {
        viewClient.sendMessage(new SetStatus((readyCheckbox.isSelected())));
    }

    public void resetFocus() {
        infoLabel.requestFocus();
    }

    public CheckBox getReadyCheckbox() {
        return readyCheckbox;
    }
}
