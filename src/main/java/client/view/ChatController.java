package client.view;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import game.Player;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.Constants;
import utilities.JSONProtocol.JSONBody;
import utilities.JSONProtocol.body.ReceivedChat;
import utilities.JSONProtocol.body.SendChat;

import javax.swing.text.Utilities;
import java.util.ArrayList;

public class ChatController extends Controller {
    private static final Logger logger = LogManager.getLogger();

    @FXML
    private JFXTextArea chatWindow;
    /**
     * the choiceBox where the user can choose if the
     * message should be a direct message or who should be
     * the receiver of the message
     */
    @FXML
    private JFXComboBox<String> directChoiceBox;
    @FXML
    private JFXTextArea lobbyTextFieldChat;

    @FXML
    public void initialize() {
        directChoiceBox.getItems().add("all");
        directChoiceBox.getSelectionModel().select(0);
        client.setChatController(this);
        lobbyTextFieldChat.setOnKeyPressed(new EventHandler<KeyEvent>()
        {
            @Override
            public void handle(KeyEvent ke)
            {
                if (ke.getCode().equals(KeyCode.ENTER))
                {
                    submitChatMessage();
                }
            }
        });
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
     */

    private void submitChatMessage() {
        if(client.getCurrentController().getClass().equals(GameController.class)){
            GameController gameController = (GameController) client.getCurrentController();
            gameController.getBoardPane().requestFocus();
        }
        String sendTo = directChoiceBox.getSelectionModel().getSelectedItem();
        logger.trace("chose choice: " + sendTo);
        String message = lobbyTextFieldChat.getText();
        message = message.substring(0, message.length()-1);
        System.out.println(message);
        JSONBody jsonBody = null;
        if(message.equals("#hotkeys")){
            chatWindow.appendText(Constants.HOTKEYSLIST);
        } else {
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
                        if (sendTo.equals(client.getPlayerFromID(client.getThisPlayersID()).getName())) toMe = true;
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
                            if (Integer.parseInt(idNr) == client.getThisPlayersID()) toMe = true;
                            jsonBody = new SendChat(message, Integer.parseInt(idNr));
                        }
                    }
                    if (!toMe) {
                        chatWindow.appendText("[You] @" + sendTo + ": " + message + "\n");
                    }
                }
                checkMessage(message);
                client.sendMessage(jsonBody);
            }
        }

        lobbyTextFieldChat.clear();
        directChoiceBox.getSelectionModel().select(0);
    }

    private void checkMessage(String message){
        String[] messageSplit = message.split(" ");
        if (message.equals("#emptySpam")) {
            client.setCountSpamCards(0);
        } else if (message.equals("#damageDecks")) {
            logger.info("count of damage cards on client side: Spam: " + client.getCountSpamCards() + ", Trojan: " + client.getCountTrojanCards() + ", Virus: " + client.getCountVirusCards() + ", Worm: " + client.getCountWormCards());
        } else if (messageSplit.length > 1 && messageSplit[0].equals("#damage")) {
            int damageCount = Integer.parseInt(messageSplit[1]);
            client.setCountSpamCards(client.getCountSpamCards() - damageCount);
        }
    }

    public void receivedChat(ReceivedChat receivedChat) {
        String chat;
        String sender = client.getUniqueName(receivedChat.getFrom());
        String message = receivedChat.getMessage();
        if (receivedChat.isPrivat())
            chat = "[" + sender + "] @You: " + message;
        else chat = "[" + sender + "] " + message;
        setTextArea(chat);
    }
}