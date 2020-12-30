package client.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.JSONProtocol.JSONMessage;
import utilities.JSONProtocol.body.SendChat;

import java.io.IOException;

/**
 * The GameViewController class controls the GameView and the Chat of the game
 *
 * @author Sarah,
 */
public class GameViewController extends Controller {
    private static final Logger logger = LogManager.getLogger();
    int i = 0;
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
    private Canvas fxCanvas;

    /**
     *
     */
    public void close() {
        //client.disconnect(); TODO disconnect client on closure of window
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
        i = ++i % 3;

        if (i == 0) path = "/view/innerViews/upgradeView.fxml";
        else if (i == 1) path = "/view/innerViews/programmingView.fxml";
        else if (i == 2) path = "/view/innerViews/activationView.fxml";

        try {
            innerPane = FXMLLoader.load(getClass().getResource(path));
        } catch (IOException e) {

        }
        return innerPane;
    }


}