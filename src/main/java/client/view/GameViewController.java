package client.view;

import client.Main;
import client.model.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import utilities.JSONProtocol.JSONMessage;
import utilities.JSONProtocol.body.SendChat;
import utilities.JSONProtocol.body.SetStatus;
import utilities.Utilities;

import java.util.ArrayList;
import java.util.Collection;

public class GameViewController {
    /**
     *
     */
    private Stage stage;
    /**
     *
     */
    public Client client;

    /**
     *
     */
    @FXML
    public Button submitButton;

    /**
     *
     */
    @FXML
    public TextArea userArea;

    /**
     *
     */
    @FXML
    public TextField chatTextArea;

    /**
     *
     */
    @FXML
    public TextArea chatWindow;

    public Label player1Label;
    public Label player2Label;
    public Label player3Label;
    public Label player4Label;
    public Label player5Label;
    public Label player6Label;
    private ArrayList<Label> labelForPlayer = new ArrayList<>();
    private Label currentLabel = player1Label;

    public TextArea joinedUsersTextArea;
    /**
     *
     */
    private Main main;

    public Button readyButton;

    public void readyButton(){
        String ready = "I'm ready!";
        String notReady = "I'm not ready!";
        boolean status = false;
        JSONMessage jsonMessage;
        if(readyButton.getText().equals(ready)){
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

    public void initialize(){
        labelForPlayer.add(player1Label);
        labelForPlayer.add(player2Label);
        labelForPlayer.add(player3Label);
        labelForPlayer.add(player4Label);
        labelForPlayer.add(player5Label);
        labelForPlayer.add(player6Label);
    }
    /**
     *
     * @param actionEvent
     */
    //TODO
    public void sendChatMessage(ActionEvent actionEvent) {
        String message = chatTextArea.getText();
        if (!message.isBlank()) {
            chatWindow.appendText("[You]: " + message + "\n");
            JSONMessage msg = new JSONMessage(new SendChat(message, -1));
            client.sendMessage(msg);
        }
        chatTextArea.clear();
    }

    /**
     *
     * @param s
     */
    public void setStage(Stage s) {
        this.stage = s;
    }

    /**
     *
     */
    public void close() {
        //client.disconnect(); TODO disconnect client on closure of window
    }


    /**
     *
     * @param messageBody
     */
    public void setTextArea(String messageBody) {
        chatWindow.appendText(messageBody + "\n");
    }

    /**
     *
     * @param body
     */
    public void setUsersTextArea(String body) {
        joinedUsersTextArea.appendText(body+ "\n");
        currentLabel.setText(body);
        int index = labelForPlayer.indexOf(currentLabel);
        currentLabel = labelForPlayer.get(index);
    }

    /**
     *
     * @param main
     */
    public void setMain(Main main) {
        this.main = main;
    }

    public void setClient(Client client) {
        this.client = client;
    }

}