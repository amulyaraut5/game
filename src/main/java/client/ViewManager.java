package client;

import client.model.ViewClient;
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
    private Scene mapScene;

    private Pane chatPane;

    private MenuController menuController;
    private LoginController loginController;
    private LobbyController lobbyController;
    private GameController gameController;
    private MapSelectionController mapSelectionController;

    private Scene currentScene;

    private ViewManager() {
    }

    public void start() {
        Platform.runLater(() -> {
            try {
                constructScenes();
                createChatPane();

                constructMenuStage();
                constructGameStage();

                showMenu();
                openMenuStage();
            } catch (IOException e) {
                logger.error("Constructing Scenes failed: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    public void showMenu() {
        ViewClient.getInstance().setCurrentController(menuController);
        menuStage.setScene(menuScene);
        if (currentScene == gameScene) openMenuStage();
        currentScene = menuScene;
    }

    public void showLogin() {
        ViewClient.getInstance().setCurrentController(loginController);
        menuStage.setScene(loginScene);
        loginController.requestFocus();
        if (currentScene == gameScene) openMenuStage();
        currentScene = loginScene;
    }

    public void showLobby() {
        ViewClient.getInstance().setCurrentController(lobbyController);
        lobbyController.attachChatPane(chatPane);
        menuStage.setScene(lobbyScene);
        if (currentScene == gameScene) openMenuStage();
        currentScene = lobbyScene;
    }

    public void showMap() {
        ViewClient.getInstance().setCurrentController(mapSelectionController);
        menuStage.setScene(mapScene);
        if (currentScene == gameScene) openMenuStage();
        currentScene = mapScene;
    }

    public void showGame() {
        ViewClient.getInstance().setCurrentController(gameController);
        openGameStage();
        currentScene = gameScene;
    }

    private void openGameStage() {
        gameController.attachChatPane(chatPane);
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
            if (currentScene != menuScene) ViewClient.getInstance().disconnect();
            System.exit(0);
        });
    }

    private void constructGameStage() {
        gameStage.setTitle("RoboRally - Game");
        gameStage.setResizable(false);
        gameStage.setScene(gameScene);

        gameScene.setOnKeyPressed(event -> gameController.keyPressed(event));

        gameStage.setOnCloseRequest(event -> {
            resetAll();
        });
    }

    private void constructScenes() throws IOException {
        FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("/view/menuView.fxml"));
        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/view/loginView.fxml"));
        FXMLLoader lobbyLoader = new FXMLLoader(getClass().getResource("/view/lobbyView.fxml"));
        FXMLLoader gameLoader = new FXMLLoader(getClass().getResource("/view/gameView.fxml"));
        FXMLLoader mapLoader = new FXMLLoader(getClass().getResource("/view/mapSelection.fxml"));

        menuScene = new Scene(menuLoader.load());
        loginScene = new Scene(loginLoader.load());
        lobbyScene = new Scene(lobbyLoader.load());
        gameScene = new Scene(gameLoader.load());
        mapScene = new Scene(mapLoader.load());

        menuController = menuLoader.getController();
        loginController = loginLoader.getController();
        lobbyController = lobbyLoader.getController();
        gameController = gameLoader.getController();
        mapSelectionController = mapLoader.getController();

        ArrayList<Controller> controllerList = new ArrayList<>();

        controllerList.add(loginController);
        controllerList.add(lobbyController);
        controllerList.add(gameController);
        controllerList.add(mapSelectionController);

        ViewClient.getInstance().setController(controllerList);
    }

    public void resetAll() {
        showMenu();
        try {
            constructScenes();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void resetGame() {
        showLobby();
    }

    public static ViewManager getInstance() {
        if (instance == null) instance = new ViewManager();
        return instance;
    }
}
