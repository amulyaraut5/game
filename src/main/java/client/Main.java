package client;

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
            constructScenes();
            constructGameStage();
            constructMenuStage();
        });
        constructGameStage();
        constructMenuStage();
        menuStage.show();
    }

    public void constructScenes() {
        Parent login = null;
        Parent game = null;
        Parent menu = null;
        try {
            menu = new FXMLLoader(getClass().getResource("/menuView.fxml")).load();
            login = new FXMLLoader(getClass().getResource("/loginView.fxml")).load();
            game = new FXMLLoader(getClass().getResource("/gameView.fxml")).load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        menuScene = new Scene(menu);
        loginScene = new Scene(login);
        gameScene = new Scene(game);
    }

    public void constructMenuStage() {
        menuStage.setTitle("RoboRally - Menu");
        menuStage.setResizable(false);
        menuStage.setScene(loginScene);

        menuStage.setOnCloseRequest(event -> {
            menuStage.close();
        });

    }

    public void constructGameStage() {
        if (gameStage == null) {
            gameStage = new Stage();
        }
        gameStage.setTitle("RoboRally - Game");
        gameStage.setResizable(false);
        gameStage.setScene(gameScene);

        gameStage.setOnCloseRequest(event -> {
            gameStage.close();
        });
    }
}
