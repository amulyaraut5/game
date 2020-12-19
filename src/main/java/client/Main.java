package client;

import client.view.GameViewController;
import client.view.LoginController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private static Stage menuStage;
    private static Stage gameStage;
    private static Scene menuScene;
    private static Scene loginScene;
    private static Scene gameScene;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage menuStage) throws IOException {
        this.menuStage = menuStage;
        Platform.runLater(() -> {
            constructGameStage();
            constructMenuStage();
        });
        constructGameStage();
        constructMenuStage();
        menuStage.show();
    }

    public void constructMenuStage() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/loginView.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        LoginController loginController = loader.getController();

        menuStage.setTitle("Roborally");
        menuStage.setResizable(false);
        menuStage.setScene(new Scene(root));
        LoginController.setStage(menuStage);

        menuStage.setOnCloseRequest(event -> {
            loginController.close();
            menuStage.close();
        });

    }

    public void constructGameStage() {
        if (gameStage == null) {
            gameStage = new Stage();
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gameView.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        GameViewController gameController = loader.getController();

        gameStage.setTitle("Robo Rally");
        gameStage.setResizable(false);
        gameStage.setScene(new Scene(root));
        gameController.setStage(gameStage);

        gameStage.setOnCloseRequest(event -> {
            gameController.close();
            gameStage.close();
        });
    }
}
