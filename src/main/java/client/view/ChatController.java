package client.view;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import game.Player;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.Constants;
import utilities.JSONProtocol.JSONBody;
import utilities.JSONProtocol.body.ReceivedChat;
import utilities.JSONProtocol.body.SendChat;
import utilities.Updatable;

/**
 * This method represents the chat for chatting with other players and direct chatting.
 *
 * @author sarah
 */
public class ChatController extends Controller {
    private static final Logger logger = LogManager.getLogger();

    /**
     * It shows if the current message is a direct message to the player itself,
     * then the player won't see a [You] message, because he will receive it from the server and then display it.
     */
    private boolean privateToMe;

    /**
     * The textArea where the whole chat history gets displayed.
     */
    @FXML
    private JFXTextArea chatWindow;

    /**
     * The choiceBox where the user can choose if the message should be a direct message and who should be
     * the receiver of the message (one or all).
     */
    @FXML
    private JFXComboBox<String> directChoiceBox;

    /**
     * The TextField which reads the message the player typed.
     */
    @FXML
    private JFXTextField messageField;

    /**
     * In this method the directBox gets initialized and the chat controller gets assigned to viewClient. Also the
     * lobbyTextArea recognizes if the enter key gets pressed and call the method submitChatMessage().
     */
    public void initialize() {
        directChoiceBox.getItems().add("all");
        directChoiceBox.getSelectionModel().select(0);
        viewClient.setChatController(this);
        chatWindow.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean old, Boolean isFocused) -> {
            if (isFocused) resetFocus();
        });

    }

    /**
     * The messages received from other users are printed
     * in the chatTextArea and make a new line.
     *
     * @param messageBody the message which will get printed
     */
    public void setTextArea(String messageBody) {
        chatWindow.appendText(messageBody + "\n");
    }

    /**
     * This method displays an user who joined to the lobby
     * with its chosen robot, name and also the name is added to the choiceBox
     * so that other users in lobby can send direct messages.
     *
     * @param player that gets added
     */
    public void addUser(Player player) {
        String newName = viewClient.getUniqueName(player.getID());
        directChoiceBox.getItems().add(newName);
    }

    /**
     * This message gets called when the user presses enter, it reads the receiver (all or one person) from the comboBox
     * and the message which gets filtered for instructions and everything gets send as the JSONMessage SendChat.
     */
    @FXML
    private void submitChatMessage() {
        resetFocus();
        String sendTo = directChoiceBox.getSelectionModel().getSelectedItem();
        logger.trace("chose choice: " + sendTo);
        String message = messageField.getText();
        if (message.equals("#hotkeys")) {
            chatWindow.appendText(Constants.HOTKEYSLIST);
        } else {
            if (!message.isBlank()) {
                JSONBody jsonBody = null;
                if (sendTo.equals("all")) {
                    jsonBody = new SendChat(message, -1);
                    chatWindow.appendText("[You] " + message + "\n");
                } else{
                    for (Player player: viewClient.getPlayers()){
                        if (player.getUniqueName().equals(sendTo)){
                            jsonBody = new SendChat(message, player.getID());
                            if (player.getID() == viewClient.getThisPlayersID()) privateToMe = true;
                        }
                    }
                    if (!privateToMe) {
                        chatWindow.appendText("[You] @" + sendTo + ": " + message + "\n");
                    }
                }
                checkMessage(message);
                viewClient.sendMessage(jsonBody);
            }
        }
        privateToMe = false;
        messageField.clear();
        directChoiceBox.getSelectionModel().select(0);
    }


    /**
     * This message checks whether the typed message is either #emptySpam or #damageDecks or #damage x,
     * depending to this it calls messages that change things on the client side.
     *
     * @param message the message which has to be checked
     */
    private void checkMessage(String message) {
        String[] messageSplit = message.split(" ");
        if (message.equals("#emptySpam")) {
            viewClient.setCountSpamCards(0);
        } else if (message.equals("#damageDecks")) {
            logger.info("count of damage cards on client side: Spam: " + viewClient.getCountSpamCards() + ", Trojan: " + viewClient.getCountTrojanCards() + ", Virus: " + viewClient.getCountVirusCards() + ", Worm: " + viewClient.getCountWormCards());
        } else if (messageSplit.length > 1 && messageSplit[0].equals("#damage")) {
            int damageCount = Integer.parseInt(messageSplit[1]);
            viewClient.setCountSpamCards(viewClient.getCountSpamCards() - damageCount);
        }
    }

    /**
     * This message displays a received message depending if it is direct or not
     * in the chatWindow.
     *
     * @param receivedChat the receivedChat with sender and message
     */
    public void receivedChat(ReceivedChat receivedChat) {
        String chat;
        String sender = viewClient.getUniqueName(receivedChat.getFrom());
        String message = receivedChat.getMessage();
        if (receivedChat.isPrivat())
            chat = "[" + sender + "] @You: " + message;
        else chat = "[" + sender + "] " + message;
        setTextArea(chat);
    }

    /**
     * This method resets the focus onto the infoLabel if the current controller is LobbyController
     * or to the boardPane if current controller is GameController.#
     * It gets called if the focus is set on the textField or textArea and the user clicks Enter.
     */
    private void resetFocus() {
        Updatable controller = viewClient.getCurrentController();
        if (controller instanceof GameController) {
            GameController gameController = (GameController) controller;
            gameController.resetFocus();
        } else if (controller instanceof LobbyController) {
            LobbyController lobbyController = (LobbyController) controller;
            lobbyController.resetFocus();
        }
    }
}