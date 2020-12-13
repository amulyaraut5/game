package client.view;

import client.model.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

public class GameViewController {

    private static Client client;
    @FXML
    public Button submitButton;
    @FXML
    public TextArea userArea;
    @FXML
    public TextField chatTextArea;
    @FXML
    public TextArea chatWindow;

    private static Stage stage;

/*
    public void startGameView(Stage gameStage) throws IOException {
        gStage = gameStage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/GameView.fxml"));
        Scene scene = new Scene(loader.load());
        //client.setController(controller);
        gameStage.setTitle("RoboRally");
        gameStage.setScene(scene);
        gameStage.setResizable(false);
        gameStage.show();

        gameStage.setOnCloseRequest(event -> {
            //controller.close();
            gameStage.close();
        });

    }

 */

    public void initialize() {
        //chatTextArea.clear();
        try {
            client = new Client(this);
        } catch (Exception e) {
            System.out.println("There was an exception in view." + e);
        }
    }

    private static void setClient(Client client) {
        GameViewController.client = client;
    }

    @FXML
    private void chatMessageHandling(ActionEvent event) throws Exception {
        //submitButton.setDisable(true);
        String message = chatTextArea.getText();
        if(!message.isBlank()) {
            client.processViewMessage(message);
        }
        chatTextArea.clear();
        //submitButton.setDisable(false);
    }

    /**
     * Method displays new message in chat.
     */
    public void displayInChat(String message) {
        javafx.application.Platform.runLater(() -> userArea.appendText("\n" + message));
    }


    public void setStage(Stage s){
        this.stage = s;
    }

    public void close() {
        //client.disconnect(); TODO disconnect client on closure of window
    }
}
