package client.view;

import client.model.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;

public class GameViewController {

    public Client client;
    @FXML
    public Button submitButton;
    @FXML
    public TextArea userArea;
    @FXML
    public TextArea chatTextArea;
    @FXML
    public TextArea chatWindow;



    public void chatMessageHandling(ActionEvent actionEvent) {
        String message = chatTextArea.getText();
        if(!message.isBlank()){
            chatWindow.appendText("[You]: " + message + "\n");
            client.sendUserInput(message);
        }
        chatTextArea.clear();
    }
}