package client.view;

import game.gameObjects.tiles.Attribute;
import game.gameObjects.tiles.Belt;
import game.gameObjects.tiles.Gear;
import game.gameObjects.tiles.Pit;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.Coordinate;
import utilities.JSONProtocol.JSONMessage;
import utilities.JSONProtocol.body.SendChat;
import utilities.Utilities;

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
/*        GraphicsContext gc = fxCanvas.getGraphicsContext2D();

        gc.setFill(Color.GREEN);
        gc.fillRect(0, 0, fxCanvas.getHeight(), fxCanvas.getWidth());

        String path = "/tiles/green-up.png";
        String url = getClass().getResource(path).toString();
        Image image = new Image(url);
        gc.drawImage(image, 0, 0, 60, 60);

        ArrayList<Attribute> list = new ArrayList<Attribute>(100);
        for (int j = 0; j < 13; j++) {
            list.add(new Pit());
        }
        list.add(new Gear(Utilities.Orientation.RIGHT));
        list.add(new Gear(Utilities.Orientation.LEFT));
        list.add(new Belt(Utilities.Orientation.RIGHT, 1));
        list.add(new Belt(Utilities.Orientation.LEFT, 2));

        for (int j = 0; j < list.size(); j++) {
            if (list.get(j) != null) {
                list.get(j).draw(gc, new Coordinate(j % 10, (int) j / 10));
            }
        }*/
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