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


public class LobbyController extends Controller {
    private static final Logger logger = LogManager.getLogger();


    @FXML
    public TextArea lobbyTextAreaChat;
    @FXML
    public TextField lobbyTextFieldChat;

    @FXML
    public CheckBox readyCheckbox;

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
                lobbyTextAreaChat.appendText("[You]: " + message);
                JSONMessage msg = new JSONMessage(new SendChat(message, -1));
                client.sendMessage(msg);
            }
            lobbyTextFieldChat.clear();
        }

    public  void setTextArea(String messageBody){
        lobbyTextAreaChat.appendText(messageBody);
    }

}



