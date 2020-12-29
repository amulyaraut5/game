package client.view;

import client.ViewManager;
import client.model.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.JSONProtocol.JSONMessage;
import utilities.JSONProtocol.body.GameStarted;
import utilities.JSONProtocol.body.SendChat;
import utilities.JSONProtocol.body.SetStatus;
import utilities.JSONProtocol.body.gameStarted.Field;
import utilities.JSONProtocol.body.gameStarted.Maps;

import java.util.ArrayList;

/**
 * This class displays the joined and ready users and already has the possibility to chat with other users
 *
 * @author sarah, louis
 */
public class LobbyController extends Controller {
    private static final Logger logger = LogManager.getLogger();


    @FXML
    public TextArea lobbyTextAreaChat;
    @FXML
    public TextField lobbyTextFieldChat;

    @FXML
    public CheckBox readyCheckbox;

    public TextArea joinedUsersTextArea;

    public TextArea readyUsersTextArea;



    @FXML
    private void checkBoxAction(ActionEvent event) {
        JSONMessage msg = new JSONMessage(new SetStatus(readyCheckbox.isSelected()));
        client.sendMessage(msg);
    }

    /**
     * send chat Message
     * @param event
     */
    @FXML
    private void submitChatMessage(ActionEvent event) {
            String message = lobbyTextFieldChat.getText();
            if (!message.isBlank()) {
                lobbyTextAreaChat.appendText("[You]: " + message + "\n");
                JSONMessage msg = new JSONMessage(new SendChat(message, -1));
                client.sendMessage(msg);
            }
            lobbyTextFieldChat.clear();
        }

    public  void setTextArea(String messageBody){
        lobbyTextAreaChat.appendText(messageBody + "\n");
    }
    /**
     * this method displays an user who joined to the lobby
     *
     * @param joinedUser
     */
    public void setJoinedUsersTextArea(String joinedUser) {
        joinedUsersTextArea.appendText(joinedUser + "\n");
    }
    public void setReadyUsersTextArea(String readyUser){
            readyUsersTextArea.appendText(readyUser + "\n");
    }

}



