package client.view;

import game.gameObjects.tiles.Attribute;
import game.gameObjects.tiles.Empty;
import game.gameObjects.tiles.RotatingBelt;
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

        var field1 = new ArrayList<Attribute>();
        Orientation[] orientations1 = {Orientation.UP, Orientation.LEFT};
        field1.add(new Empty());
        field1.add(new RotatingBelt(orientations1, true, 1));

        var field2 = new ArrayList<Attribute>();
        Orientation[] orientations2 = {Orientation.RIGHT, Orientation.UP};
        field2.add(new Empty());
        field2.add(new RotatingBelt(orientations2, true, 2));

        var field3 = new ArrayList<Attribute>();
        Orientation[] orientations3 = {Orientation.DOWN, Orientation.RIGHT};
        field3.add(new Empty());
        field3.add(new RotatingBelt(orientations3, false, 1));

        var field4 = new ArrayList<Attribute>();
        Orientation[] orientations4 = {Orientation.LEFT, Orientation.DOWN};
        field4.add(new Empty());
        field4.add(new RotatingBelt(orientations4, false, 2));

// TODO: 03.01.2021  if 2 mapfields have same position, only choose one
        map.add(new BoardElement(1, field1));
        map.add(new BoardElement(2, field2));
        map.add(new BoardElement(3, field3));
        map.add(new BoardElement(4, field4));

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
            for (Attribute attribute : field) {
                var attributeImage = attribute.createImage();
                fields[pos].getChildren().add(attributeImage);
            }
            flowPane.getChildren().add(fields[pos]);
        }
    }

    private Pane setNextPane() {
        Pane innerPane = null;
        String path = null;
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