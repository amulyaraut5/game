package client.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class GameViewController {

    @FXML



    public void chatMessageHandling(ActionEvent actionEvent) {
        String message = chatTextArea.getText();
        if(!message.isBlank()){
            chatWindow.appendText("[You]: " + message + "\n");
            client.sentUserInput(message);
        }
        chatTextArea.clear();
    }
}