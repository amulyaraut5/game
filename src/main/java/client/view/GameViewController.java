package client.view;

import client.Main;
import client.model.Client;
import Utilities.JSONProtocol.JSONMessage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
     * @param client
     */
    public void setClient(Client client) {
        this.client = client;
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
}