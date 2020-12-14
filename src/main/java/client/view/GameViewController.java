package client.view;

import client.model.Client;
import client.model.JSONMessage;
import com.google.gson.JsonObject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class GameViewController {

    private static Stage stage;
    public Client client;
    @FXML
    public Button submitButton;
    @FXML
    public TextArea userArea;
    @FXML
    public TextField chatTextArea;
    @FXML
    public TextArea chatWindow;

    public void sendChatMessage(ActionEvent actionEvent) {
        String message = chatTextArea.getText();
        if (!message.isBlank()) {
            chatWindow.appendText("[You]: " + message + "\n");
            JSONMessage msg = new JSONMessage("userMessage", message);
            client.sentUserInput(msg);
        }
        chatTextArea.clear();
    }


    public void setStage(Stage s) {
        this.stage = s;
    }

    public void close() {
        //client.disconnect(); TODO disconnect client on closure of window
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setTextArea(String messageBody) {
        chatWindow.appendText(messageBody + "\n");
    }
}