package client.view;

import game.gameObjects.tiles.Attribute;
import game.gameObjects.tiles.Empty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
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

    public void buildMap(ArrayList<BoardElement> map) {
        for (int i = 0; i < 100; i++) {
            fields[i] = new Group();
        }

        for (BoardElement tile : map) {
            int pos = tile.getPosition();
            var field = tile.getField();
            // TODO: 04.01.2021 check priority for the imageViews (e.g.: draw Empty at first, then Laser, then Wall)

            fields[pos - 1].getChildren().add(new Empty().createImage());

            for (Attribute attribute : field) {
                Node attributeImage = attribute.createImage();
                if (attributeImage != null) {
                    fields[pos - 1].getChildren().add(attributeImage);
                }
            }
            flowPane.getChildren().add(fields[pos - 1]);
        }
    }

    @FXML
    private void initialize() {
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