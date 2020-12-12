package client.view;

import client.model.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;

import java.io.IOException;

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

    public void startGameView(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/GameView.fxml"));
        Parent gameView = null;
        try {
            gameView = (Parent) loader.load();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        GameViewController controller = loader.getController();


    }
}