package client;

import client.view.GameViewController;
import client.view.LoginController;
import client.view.MenuController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private static Stage menuStage;
    private static Stage loginStage;
    private static Stage gameStage;
    private static Scene menuScene;
    private static Scene loginScene;
    private static Scene gameScene;
    private Parent login = null;
    private Parent game = null;
    private Parent menu = null;

    private static Stage currentStage = menuStage;

    private LoginController loginController;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage menuStage) throws IOException {
        Main.menuStage = menuStage;

        Platform.runLater(() -> {
            try {
                constructScenes();
            } catch (IOException e) {
                e.printStackTrace();
            }
            constructMenuStage();

        });

        constructMenuStage();
        menuStage.show();
    }

    public void constructScenes() throws IOException {

        createControllerAndParents();

        menuScene = new Scene(menu);
        loginScene = new Scene(login);
        gameScene = new Scene(game);
    }

    public void constructMenuStage() {
        menuStage.setTitle("RoboRally - Menu");
        menuStage.setResizable(false);
        menuStage.setScene(menuScene);

        menuStage.setOnCloseRequest(event -> {
            menuStage.close();
        });

    }

    public void constructLoginStage() {
        menuStage.setTitle("RoboRally - Login");
        menuStage.setResizable(false);
        loginController.begin();
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

    private void createControllerAndParents() throws IOException {
        //menu
        FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("/menuView.fxml"));
        menu = menuLoader.load();
        MenuController menuController = menuLoader.getController();
        menuController.setMain(this);

        //login
        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/loginView.fxml"));
        login = loginLoader.load();
        loginController = loginLoader.getController();
        loginController.setMain(this);

        //game
        FXMLLoader gameLoader = new FXMLLoader(getClass().getResource("/gameView.fxml"));
        game = gameLoader.load();
        GameViewController gameController = gameLoader.getController();
        gameController.setMain(this);
    }

    public static void showGameStage() {
        menuStage.close();
        gameStage.show();
    }

    public static void showMenuStage(){
        gameStage.close();
        menuStage.setScene(loginScene);
        menuStage.show();
    }
}
