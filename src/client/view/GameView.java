package client.view;

import client.view.login.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GameView extends Application {

    public static void main(String[] args){ launch(args);}

    @Override
    public void start(Stage gameStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("GameView.fxml"));
        Parent root = loader.load();
        GameViewController gameController = loader.getController();

        gameStage.setTitle("Robo Rally");
        gameStage.setScene(new Scene(root));
        gameStage.show();
        gameController.setStage(gameStage);

        gameStage.setOnCloseRequest(event -> {
            gameController.close();
            gameStage.close();
        });


    }
}
