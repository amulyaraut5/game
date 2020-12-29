package client.view;

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


}