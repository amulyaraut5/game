package client;

import client.model.Client;
import client.view.*;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;

/**
 * ViewManager is a Singleton. Class handles all different views and their transitions. ViewManager is Singleton.
 * The instance is initially created in getInstance() with lazy initialization.
 * <p>
 * The Scenes of the view can be changed with nextScenes() and previousScene(). showMenu() return to the main menu.
 *
 * @author Simon, sarah
 */
public class ViewManager {
    private static final Logger logger = LogManager.getLogger();
    /**
     * Singleton instance of ViewManager
     */
    private static ViewManager instance;

    private final Stage menuStage = new Stage();
    private final Stage gameStage = new Stage();

    private Scene menuScene;
    private Scene loginScene;
    private Scene lobbyScene;
    private Scene gameScene;

    private Pane chatPane;

    private MenuController menuController;
    private LoginController loginController;
    private LobbyController lobbyController;
    private GameViewController gameViewController;

    private Scene currentScene;

    private ViewManager() {
        Platform.runLater(() -> {
            try {
                constructScenes();
                createChatPane();

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

    public void displayErrorMessage(String error) {
        if (currentScene == menuScene) menuController.displayError(error);
        if (currentScene == loginScene) loginController.displayError(error);
        if (currentScene == lobbyScene) lobbyController.displayError(error);
        if (currentScene == gameScene) gameViewController.displayError(error);
    }

    public void showMenu() {
        menuStage.setScene(menuScene);
        if (currentScene == gameScene) openMenuStage();
        currentScene = menuScene;
    }

    public void showLogin() {
        menuStage.setScene(loginScene);
        if (currentScene == gameScene) openMenuStage();
        currentScene = loginScene;
    }

    public void showLobby() {
        lobbyController.attachChatPane(chatPane);
        menuStage.setScene(lobbyScene);
        if (currentScene == gameScene) openMenuStage();
        currentScene = lobbyScene;
    }

    public void showGame() {
        openGameStage();
        currentScene = gameScene;
    }

    private void openGameStage() {
        gameViewController.attachChatPane(chatPane);
        menuStage.close();
        gameStage.show();
    }

    private void openMenuStage() {
        gameStage.close();
        menuStage.show();
    }

    private void createChatPane() {
        try {
            chatPane = FXMLLoader.load(getClass().getResource("/view/innerViews/chatView.fxml"));
        } catch (IOException e) {
            logger.error("ChatPane could not be created: " + e.getMessage());
        }
    }

    private void constructMenuStage() {
        menuStage.setTitle("RoboRally - Menu");
        menuStage.setResizable(false);
        menuStage.setScene(menuScene);

        menuStage.setOnCloseRequest(event -> {
            Client.getInstance().disconnect();
        });
    }

    private void constructGameStage() {
        gameStage.setTitle("RoboRally - Game");
        gameStage.setResizable(false);
        gameStage.setScene(gameScene);

        gameStage.setOnCloseRequest(event -> {
            Client.getInstance().disconnect();
            showMenu();
        });
    }

    private void constructScenes() throws IOException {
        FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("/view/menuView.fxml"));
        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/view/loginView.fxml"));
        FXMLLoader lobbyLoader = new FXMLLoader(getClass().getResource("/view/lobbyView.fxml"));
        FXMLLoader gameLoader = new FXMLLoader(getClass().getResource("/view/gameView.fxml"));

        menuScene = new Scene(menuLoader.load());
        loginScene = new Scene(loginLoader.load());
        lobbyScene = new Scene(lobbyLoader.load());
        gameScene = new Scene(gameLoader.load());

        menuController = menuLoader.getController();
        loginController = loginLoader.getController();
        lobbyController = lobbyLoader.getController();
        gameViewController = gameLoader.getController();

        ArrayList<Controller> controllerList = new ArrayList<>();

        controllerList.add(loginController);
        controllerList.add(lobbyController);
        controllerList.add(gameViewController);

        Client.getInstance().setController(controllerList);
    }
}
