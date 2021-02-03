package client.view;

import game.Player;
import javafx.event.ActionEvent;
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
import utilities.JSONProtocol.JSONBody;
import utilities.JSONProtocol.JSONMessage;
import utilities.JSONProtocol.body.Error;
import utilities.JSONProtocol.body.MapSelected;
import utilities.JSONProtocol.body.PlayerStatus;
import utilities.JSONProtocol.body.SetStatus;
import utilities.Updatable;

import java.io.InputStream;
import java.util.HashMap;

/**
 * This class displays the joined and ready users and already has the possibility to chat with other users
 *
 * @author sarah, louis
 */
public class LobbyController extends Controller implements Updatable {
    private static final Logger logger = LogManager.getLogger();
    private final HashMap<Player, VBox> playerIcons = new HashMap<>();

    @FXML
    private BorderPane chatPane;
    @FXML
    private CheckBox readyCheckbox;
    @FXML
    private FlowPane playerIconPane;
    @FXML
    private Label infoLabel;

    @FXML
    private Label infoLabel2;


    @FXML private CheckBox dizzyHighway;
    @FXML private CheckBox extraCrispy;

    @FXML private ImageView dizzy;
    @FXML private ImageView crispy;

    private final boolean state = false;



    /**
     * this method gets called automatically by constructing view
     * it adds the different ImageViews and Labels to lists and also
     * sets the default of the choiceBox to all. Additionally the current imageView
     * and label are assigned
     */
    @FXML
    public void initialize() {
        setVisible(false);
        setImageViewVisible(true);
        infoLabel2.setText("First Player to click Ready will get a chance to choose a map.");
    }

    public void attachChatPane(Pane chat) {
        chat.setPrefWidth(chatPane.getPrefWidth());
        chat.setPrefHeight(chatPane.getPrefHeight());
        chatPane.setCenter(chat);
    }

    public void reset(){
        readyCheckbox.setSelected(state);
        dizzyHighway.setSelected(state);
        extraCrispy.setSelected(state);
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
        playerIconPane.getChildren().add(group);
        playerIcons.put(player, group);
    }


    /**
     * The robot image of the user who clicked the ready button gets changed. Now the icon has a pink
     * background to signal the ready status.
     *
     * @param playerStatus
     */
    private void displayStatus(PlayerStatus playerStatus) {
        Player player = client.getPlayerFromID(playerStatus.getID());
        String path = "/lobby/" + robotNames[player.getFigure()];
        if (playerStatus.isReady()) path += "-ready.png";
        else path += ".png";
        InputStream stream = ImageHandler.class.getResourceAsStream(path);
        Image image = new Image(stream, 90, 90, true, true);
        ImageView imageView = (ImageView) playerIcons.get(player).getChildren().get(0);
        imageView.setImage(image);
    }

    /**
     * by clicking the ready checkbox a message will be send to the client (and then to the server)
     * to signal the ready status of the user.
     */
    @FXML
    private void checkBoxAction() {
        client.sendMessage(new SetStatus(readyCheckbox.isSelected()));
        if(!readyCheckbox.isSelected()){
            infoLabel2.setText("Please wait till somebody selects the map.");
            setDisable(true);
            setSelected(false);
        }
    }

    public void removePlayer(Player player) {
        VBox tile = playerIcons.get(player);
        playerIconPane.getChildren().remove(tile);
    }


    @FXML
    private void choiceBoxActionForMap(ActionEvent event){

        if(dizzyHighway.isSelected()){
            JSONBody jsonBody = new MapSelected("DizzyHighway");
            client.sendMessage(jsonBody);
            setDisable(true);
        }
        else if(extraCrispy.isSelected()){
            JSONBody jsonBody = new MapSelected("ExtraCrispy");
            client.sendMessage(jsonBody);
            setDisable(true);
        }
    }

    @Override
    public void update(JSONMessage message) {
        switch (message.getType()) {
            case Error -> {
                Error error = (Error) message.getBody();
                infoLabel.setText(error.getError());
            }
            case PlayerStatus -> {
                PlayerStatus playerStatus = (PlayerStatus) message.getBody();
                displayStatus(playerStatus);
            }
            case SelectMap -> {
                setVisible(true);
                setDisable(false);
            }
        }
    }

    private void setVisible(boolean b){
        dizzyHighway.setVisible(b);
        extraCrispy.setVisible(b);
    }

    private void setDisable(boolean b){
        dizzyHighway.setDisable(b);
        extraCrispy.setDisable(b);

    }

    private void setSelected(boolean b){
        dizzyHighway.setSelected(b);
        extraCrispy.setSelected(b);
    }

    private void setImageViewVisible(boolean b){
        dizzy.setVisible(b);
        crispy.setVisible(b);
    }
}
