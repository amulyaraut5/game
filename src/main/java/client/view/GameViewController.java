package client.view;

import game.gameObjects.tiles.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.JSONProtocol.JSONMessage;
import utilities.JSONProtocol.body.SendChat;
import utilities.JSONProtocol.body.gameStarted.BoardElement;
import utilities.Utilities;
import utilities.Utilities.Orientation;

import java.io.IOException;
import java.util.ArrayList;

/**
 * The GameViewController class controls the GameView and the Chat of the game
 *
 * @author Sarah,
 */
public class GameViewController extends Controller {
    private static final Logger logger = LogManager.getLogger();
    private final Group[] fields = new Group[100];
    private int currentInnerView = 0;
    /**
     * the ready Button which can be clicked to show the availability for playing the game
     */
    @FXML
    private Button readyButton;
    /**
     * The TextField where the player can type in its message to one user/other users
     */
    @FXML
    private TextField chatTextField;
    /**
     * The TextArea which displays the chat history with other players
     */
    @FXML
    private TextArea chatWindow;
    @FXML
    private BorderPane outerPane;
    @FXML
    private FlowPane flowPane;

    /**
     *
     */
    public void close() {
        //client.disconnect(); TODO disconnect client on closure of window
    }

    @FXML
    private void initialize() {
        var map = new ArrayList<BoardElement>(100);
        var field = new ArrayList<ArrayList<Attribute>>();

        for (int i = 0; i < 14; i++) {
            field.add(i, new ArrayList<>());
            field.get(i).add(new Empty());
        }

        field.get(1).add(new Antenna());
        field.get(2).add(new Belt(Orientation.RIGHT, 1));
        field.get(3).add(new ControlPoint(5));
        field.get(4).add(new Empty());
        field.get(5).add(new EnergySpace(3));
        field.get(6).add(new Gear(Utilities.Rotation.LEFT));
        field.get(7).add(new Laser(Orientation.LEFT, 3));
        field.get(8).add(new Pit());
        field.get(9).add(new PushPanel(Orientation.DOWN, new int[]{3}));
        field.get(10).add(new Reboot());
        field.get(11).add(new RotatingBelt(new Orientation[]{Orientation.UP, Orientation.RIGHT}, true, 1));
        field.get(12).add(new RotatingBelt(new Orientation[]{Orientation.DOWN, Orientation.RIGHT}, false, 2));
        field.get(13).add(new Wall(Orientation.RIGHT));


// TODO: 03.01.2021  if 2 mapfields have same position, only choose one

        for (int i = 0; i < field.size(); i++) {
            map.add(new BoardElement(i, field.get(i)));
        }

        buildMap(map);
    }

    /**
     * This message adds a String to the chatTextArea
     *
     * @param messageBody
     */
    private void setTextArea(String messageBody) {
        chatWindow.appendText(messageBody + "\n");
    }

    /**
     * The method gets called by clicking on the submit button
     * it casts the message of the user to a JSONMessage (private/not private) and
     * clears the textField and displays the message in the textArea
     *
     * @param event
     */
    @FXML
    private void sendChatMessage(ActionEvent event) {
        String message = chatTextField.getText();
        if (!message.isBlank()) {
            setTextArea("[You]: " + message);
            JSONMessage msg = new JSONMessage(new SendChat(message, -1));
            client.sendMessage(msg);
        }
        chatTextField.clear();
    }

    @FXML
    private void changeInnerView(ActionEvent event) {
        Pane innerPane = setNextPane();
        outerPane.setCenter(innerPane);
    }

    private void buildMap(ArrayList<BoardElement> map) {
        for (int i = 0; i < 100; i++) {
            fields[i] = new Group();
        }

        for (BoardElement tile : map) {
            int pos = tile.getPosition();
            var field = tile.getField();
            // TODO: 04.01.2021 check priority for the imageViews (e.g.: draw Empty at first, then Laser, then Wall)
            for (Attribute attribute : field) {
                var attributeImage = attribute.createImage();
                fields[pos].getChildren().add(attributeImage);
            }
            flowPane.getChildren().add(fields[pos]);
        }
    }

    private Pane setNextPane() {
        Pane innerPane = null;
        String path = "";
        currentInnerView = ++currentInnerView % 3;

        if (currentInnerView == 0) path = "/view/innerViews/upgradeView.fxml";
        else if (currentInnerView == 1) path = "/view/innerViews/programmingView.fxml";
        else if (currentInnerView == 2) path = "/view/innerViews/activationView.fxml";

        try {
            innerPane = FXMLLoader.load(getClass().getResource(path));
        } catch (IOException e) {

        }
        return innerPane;
    }


}