package client.view;

import client.Main;
import client.model.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import utilities.JSONProtocol.JSONMessage;
import utilities.JSONProtocol.lobby.SetStatus;
import utilities.Utilities;

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
        jsonMessage = new JSONMessage(Utilities.MessageType.SetStatus, new SetStatus(status));
        client.sendMessage(jsonMessage);

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
            //JSONMessage msg = new JSONMessage("userMessage", message);
            //client.sendUserInput(msg);
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
     * @param main
     */
    public void setMain(Main main) {
        this.main = main;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}