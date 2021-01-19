package client.view;

import game.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.JSONProtocol.JSONBody;
import utilities.JSONProtocol.body.ReceivedChat;
import utilities.JSONProtocol.body.SendChat;

public class ChatController extends Controller {
    private static final Logger logger = LogManager.getLogger();

    @FXML
    private TextArea chatWindow;
    /**
     * the choiceBox where the user can choose if the
     * message should be a direct message or who should be
     * the receiver of the message
     */
    @FXML
    private ChoiceBox<String> directChoiceBox;
    @FXML
    private TextField lobbyTextFieldChat;

    @FXML
    public void initialize() {
        directChoiceBox.getItems().add("all");
        directChoiceBox.getSelectionModel().select(0);

        client.setChatController(this);
    }

    /**
     * The messages received from other users are printed
     * in the chatTextArea
     *
     * @param messageBody
     */
    public void setTextArea(String messageBody) {
        chatWindow.appendText(messageBody + "\n");
    }

    /**
     * this method displays an user who joined to the lobby
     * with its choosed robot, name and also the name is added to the choicebox
     * so that other users in lobby can send direct messages.
     * Also
     */
    public void addUser(Player player) {
        String newName = player.getName() + " " + player.getID();
        directChoiceBox.getItems().add(newName);
    }

    /**
     * send a chat Message, either private or to everyone
     *
     * @param event
     */
    @FXML
    private void submitChatMessage(ActionEvent event) {
        String sendTo = directChoiceBox.getSelectionModel().getSelectedItem();
        logger.trace("chose choice: " + sendTo);
        String message = lobbyTextFieldChat.getText();
        JSONBody jsonBody;
        if (!message.isBlank()) {
            if (sendTo.equals("all")) {
                jsonBody = new SendChat(message, -1);
                chatWindow.appendText("[You] " + message + "\n");
            } else {
                String[] userInformation = sendTo.split(" ");
                String destinationUser = "";
                for (int i = 0; i < userInformation.length - 1; i++) destinationUser += userInformation[i] + " ";
                String idUser = userInformation[userInformation.length - 1];
                destinationUser = destinationUser.substring(0, destinationUser.length() - 1);
                jsonBody = new SendChat(message, Integer.parseInt(idUser));
                chatWindow.appendText("[You] @" + destinationUser + ": " + message + "\n");
            }
            client.sendMessage(jsonBody);
        }
        lobbyTextFieldChat.clear();
        directChoiceBox.getSelectionModel().select(0);
    }

    public void receivedChat(ReceivedChat receivedChat) {
        String chat;
        if (receivedChat.isPrivat())
            chat = "[" + receivedChat.getFrom() + "] @You: " + receivedChat.getMessage();
        else chat = "[" + receivedChat.getFrom() + "] " + receivedChat.getMessage();
        setTextArea(chat);
    }
}