package client.view;

import client.model.Client;
import com.google.gson.JsonObject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

public class GameViewController {

    public Client client;
    @FXML
    public Button submitButton;
    @FXML
    public TextArea userArea;
    @FXML
    public TextField chatTextArea;
    @FXML
    public TextArea chatWindow;

    private static Stage stage;


    public void chatMessageHandling(ActionEvent actionEvent) {
        String message = chatTextArea.getText();
        if(!message.isBlank()){
            chatWindow.appendText("[You]: " + message + "\n");
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("type", "usermessage");
            jsonObject.addProperty("messagebody", message);
            client.sentUserInput(jsonObject);
        }
        chatTextArea.clear();
    }

    @FXML
    public void onEnter(ActionEvent ae) throws Exception {
        String message = chatTextArea.getText();
        if(!message.isBlank()){
            chatWindow.appendText("[You]: " + message + "\n");
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("type", "usermessage");
            jsonObject.addProperty("messagebody", message);
            client.sentUserInput(jsonObject);
        }
        chatTextArea.clear();
    }


    public void setStage(Stage s){
        this.stage = s;
    }

    public void close() {
        //client.disconnect(); TODO disconnect client on closure of window
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setTextArea(String messageBody) {
        chatWindow.appendText(messageBody+ "\n");
    }
}