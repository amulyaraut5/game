package client;

import client.model.Client;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class ViewManager {
    private static final Logger logger = LogManager.getLogger();
    private static ViewManager instance;

    private Client client;

    private Stage menuStage = new Stage();
    private Stage gameStage = new Stage();
    private Scene menuScene;
    private Scene loginScene;
    private Scene lobbyScene;
    private Scene gameScene;

    private ViewManager() {
        Platform.runLater(() -> {
            try {
                constructScenes();
                constructMenuStage();
                constructGameStage();
                menuStage.show();
            } catch (IOException e) {
                logger.error("Constructing Scenes failed: " + e.getMessage());
            }
        });
    }

    public static ViewManager getInstance() {
        if (instance == null) instance = new ViewManager();
        return instance;
    }

    public void nextScene() {
        if (menuStage.isShowing()) {

            if (menuStage.getScene() == menuScene) menuStage.setScene(loginScene);
            else if (menuStage.getScene() == loginScene) menuStage.setScene(lobbyScene);
            else if (menuStage.getScene() == lobbyScene) showGameStage();
        } else {
            logger.warn("There is no next Scene!");
        }
    }

    public void previousScene() {
        if (gameStage.isShowing()) showMenuStage();
        else {
            if (menuStage.getScene() == loginScene || menuStage.getScene() == lobbyScene) {
                menuStage.setScene(menuScene);
            } else logger.warn("There is no previous Scene!");
        }
    }

    public void showMenu() {
        if (gameStage.isShowing()) {
            menuStage.setScene(menuScene);
            showMenuStage();
        } else {
            if (menuStage.getScene() == loginScene || menuStage.getScene() == lobbyScene) {
                menuStage.setScene(menuScene);
            } else logger.warn("The menuScene is already the currently shown Scene!");
        }
    }

    private void showGameStage() {
        if (menuStage.getScene() == lobbyScene) {
            menuStage.close();
            gameStage.show();
        } else {
            logger.warn("Game starts only from the lobby!");
        }
    }

    private void showMenuStage() {
        gameStage.close();
        menuStage.show();
    }

    private void constructMenuStage() {
        menuStage.setTitle("RoboRally - Menu");
        menuStage.setResizable(false);
        menuStage.setScene(menuScene);

        menuStage.setOnCloseRequest(event -> {
            if (menuStage.getScene() == menuScene) {
                menuStage.close();
            } else {
                menuStage.setScene(menuScene);
            }
        });
    }

    private void constructGameStage() {
        gameStage.setTitle("RoboRally - Game");
        gameStage.setResizable(false);
        gameStage.setScene(gameScene);

        gameStage.setOnCloseRequest(event -> {
            //TODO leave game
            showMenuStage();
        });
    }

    private void constructScenes() throws IOException {
        /*String[] views = {"menuView", "loginView", "lobbyView", "gameView"};
        for (int i = 0; i < views.length; i++) {
            FXMLLoader viewLoader = new FXMLLoader(getClass().getResource("/view/" + views[i] + ".fxml"));
        }*/
        FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("/view/menuView.fxml"));
        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/view/loginView.fxml"));
        FXMLLoader lobbyLoader = new FXMLLoader(getClass().getResource("/view/lobbyView.fxml"));
        FXMLLoader gameLoader = new FXMLLoader(getClass().getResource("/view/gameView.fxml"));

        menuScene = new Scene(menuLoader.load());
        loginScene = new Scene(loginLoader.load());
        lobbyScene = new Scene(lobbyLoader.load());
        gameScene = new Scene(gameLoader.load());
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Client getClient() {
        return client;
    }
}
