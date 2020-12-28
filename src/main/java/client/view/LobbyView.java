package client.view;
import client.Main;
import client.model.Client;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LobbyView extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage lobbyStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/lobbyView.fxml"));
        Parent root = loader.load();
        LobbyController lobbyController = loader.getController();

        lobbyStage.setTitle("Roborally");
        lobbyStage.setScene(new Scene(root));
        lobbyStage.show();
        lobbyController.setStage(lobbyStage);

        lobbyStage.setOnCloseRequest(event -> {
            lobbyController.close();
            lobbyStage.close();
        });
    }


}
