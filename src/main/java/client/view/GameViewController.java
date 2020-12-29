package client.view;

import client.ViewManager;
import client.model.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.JSONProtocol.JSONMessage;
import utilities.JSONProtocol.body.SendChat;
import utilities.JSONProtocol.body.SetStatus;

/**
 * The GameViewController class controls the GameView and the Chat of the game
 *
 * @author Sarah,
 */
public class GameViewController extends Controller {
    private static final Logger logger = LogManager.getLogger();
    /**
     * the ready Button which can be clicked to show the availability for playing the game
     */
    @FXML
    public Button readyButton;
    /**
     * client who plays the game and has access to the main class
     */
    private Client client;
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
    /**
     * The TextArea which displays the users who already joined the lobby
     */
    @FXML
    private TextArea joinedUsersTextArea;

    /**
     * by clicking on the button the player can select if he/she is ready to start the game
     * the method also sends a SetStatus protocol method with the current status of the button
     */
    public void readyButton() {
        String ready = "I'm ready!";
        String notReady = "I'm not ready!";
        boolean status = false;
        JSONMessage jsonMessage;
        if (readyButton.getText().equals(ready)) {
            readyButton.setText(notReady);
            readyButton.setOpacity(0.5);
            status = true;
        } else {
            readyButton.setText(ready);
            readyButton.setOpacity(100);
            status = false;
        }
        jsonMessage = new JSONMessage(new SetStatus(status));
        client.sendMessage(jsonMessage);

    }

    /**
     * The method gets called by clicking on the submit button
     * it casts the message of the user to a JSONMessage (private/not private) and
     * clears the textField and displays the message in the textArea
     *
     * @param actionEvent
     */
    //TODO direct
    public void sendChatMessage(ActionEvent actionEvent) {
        String message = chatTextField.getText();
        if (!message.isBlank()) {
            setTextArea("[You]: " + message);
            JSONMessage msg = new JSONMessage(new SendChat(message, -1));
            client.sendMessage(msg);
        }
        chatTextField.clear();
    }


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
    public void setTextArea(String messageBody) {
        chatWindow.appendText(messageBody + "\n");
    }

    /**
     * this method displays an user who joined to the lobby
     *
     * @param body
     */
    public void setUsersTextArea(String body) {
        joinedUsersTextArea.appendText(body + "\n");
    }

    /**
     * This method sets the client
     *
     * @param client
     */
    public void setClient(Client client) {
        //TODO remove
        this.client = client;
    }

}