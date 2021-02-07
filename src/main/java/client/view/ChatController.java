package client.view;

import com.jfoenix.controls.JFXComboBox;
import game.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.JSONProtocol.JSONBody;
import utilities.JSONProtocol.body.ReceivedChat;
import utilities.JSONProtocol.body.SendChat;

import java.util.ArrayList;

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
    private JFXComboBox<String> directChoiceBox;
    @FXML
    private TextField lobbyTextFieldChat;
    @FXML
    private ScrollPane scrollPane;

    @FXML
    public void initialize() {
        directChoiceBox.getItems().add("all");
        directChoiceBox.getSelectionModel().select(0);
        scrollPane.setFitToWidth(true);
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
        String newName = client.getUniqueName(player.getID());
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
        JSONBody jsonBody = null;
        if (!message.isBlank()) {
            if (sendTo.equals("all")) {
                jsonBody = new SendChat(message, -1);
                chatWindow.appendText("[You] " + message + "\n");
            } else {
                int count = 0;
                String[] name = sendTo.split(" ", 2);
                for (Player player : client.getPlayers()) {
                    if (player.getName().equals(name[0])) count++;
                }
                boolean toMe = false;
                if (count == 1) {
                    if(sendTo.equals(client.getPlayerFromID(client.getThisPlayersID()).getName())) toMe = true;
                    for (Player player : client.getPlayers())
                        if (sendTo.equals(player.getName())) jsonBody = new SendChat(message, player.getID());
                } else {
                    ArrayList<Integer> names = new ArrayList<>();
                    for (Player player : client.getPlayers())
                        if (name[0].equals(player.getName())) names.add(player.getID());
                    if (sendTo.length() == 1) {
                        jsonBody = new SendChat(message, names.get(0));
                    } else {
                        String idNr = sendTo.substring(sendTo.length() - 1);
                        if(Integer.parseInt(idNr) == client.getThisPlayersID()) toMe = true;
                        jsonBody = new SendChat(message, Integer.parseInt(idNr));
                    }
                }
                if(!toMe){
                    chatWindow.appendText("[You] @" + sendTo + ": " + message + "\n");
                }
            }
            String[] messageSplit = message.split(" ");

            if (message.equals("#emptySpam")) {
                setCountSpamCards(0);
            } else if (message.equals("#countDamage")) {
                logger.info("count of damage cards on client side: Spam: " + getCountSpamCards() + ", Trojan: " + getCountTrojanCards() + ", Virus: " + getCountVirusCards() + ", Worm: " + getCountWormCards());
            } else if (messageSplit.length > 1 && messageSplit[0].equals("#damage")) {
                int damageCount = Integer.parseInt(messageSplit[1]);
                setCountSpamCards(getCountSpamCards() - damageCount);
            }
            client.sendMessage(jsonBody);
        }
        lobbyTextFieldChat.clear();
        directChoiceBox.getSelectionModel().select(0);
    }

    public void receivedChat(ReceivedChat receivedChat) {
        String chat;
        String sender = client.getUniqueName(receivedChat.getFrom());
        if (receivedChat.isPrivat())
            chat = "[" + sender + "] @You: " + receivedChat.getMessage();
        else chat = "[" + sender + "] " + receivedChat.getMessage();
        setTextArea(chat);
    }
}