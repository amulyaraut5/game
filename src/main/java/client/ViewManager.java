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
 * @author simon, sarah
 */
public class ViewManager {
    private static final Logger logger = LogManager.getLogger();
    /**
     * Singleton instance of ViewManager
     */
    private static ViewManager instance;

    private Client client = Client.getInstance();

    private Stage menuStage = new Stage();
    private Stage gameStage = new Stage();
    private Scene menuScene;
    private Scene loginScene;
    private Scene lobbyScene;
    private Scene gameScene;
    private Pane chatPane;
    private Pane playerMap;
    //private Scene mapSelectionScene;
    private LobbyController lobbyController;
    private GameViewController gameViewController;
    private LoginController loginController;
    private PlayerMapController playerMapController;
    //private MapSelectionController mapSelectionController;

    private ViewManager() {
        Platform.runLater(() -> {
            try {
                constructScenes();
                createChatPane();
                createPlayerMap();

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

            /*else if (menuStage.getScene() == loginScene) {
                menuStage.setScene(mapSelectionScene);}*/

            else if (menuStage.getScene() == loginScene){
                menuStage.setScene(lobbyScene);
                lobbyController.attachChatPane(chatPane);
            }
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

    public void displayErrorMessage(String error) {
        //TODO
    }

    private void showGameStage() {
        gameViewController.attachChatPane(chatPane);
        gameViewController.attachPlayerMap(playerMap);

       //gameViewController.changeInnerView();
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
            //TODO check if current scene is lobbyScene -> leave lobby & disconnect
            if (menuStage.getScene() == menuScene) {
                menuStage.close();
            } else {
                showGameStage();    //TODO just for testing, remove later, add menuStage.show();
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

    private void createChatPane() {
        try {
            chatPane = FXMLLoader.load(getClass().getResource("/view/innerViews/chatView.fxml"));
        } catch (IOException e) {
            logger.error("ChatPane could not be created: " + e.getMessage());
        }
    }
    private void createPlayerMap() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/innerViews/playerMap.fxml"));
            playerMap = fxmlLoader.load();
            playerMapController = fxmlLoader.getController();
            gameViewController.setPlayerMapController(playerMapController);
        } catch (IOException e) {
            logger.error("PlayerMap could not be created: " + e.getMessage());
        }
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
        //FXMLLoader mapSceneLoader = new FXMLLoader(getClass().getResource("/view/mapSelection.fxml"));

        menuScene = new Scene(menuLoader.load());
        loginScene = new Scene(loginLoader.load());
        lobbyScene = new Scene(lobbyLoader.load());
        gameScene = new Scene(gameLoader.load());
        //mapSelectionScene = new Scene(mapSceneLoader.load());

        loginController = loginLoader.getController();
        lobbyController = lobbyLoader.getController();
        gameViewController = gameLoader.getController();
        //mapSelectionController = mapSceneLoader.getController();

        ArrayList<Controller> controllerList = new ArrayList<>();

        controllerList.add(loginController);
        controllerList.add(lobbyController);
        controllerList.add(gameViewController);
        //controllerList.add(mapSelectionController);
        client.setController(controllerList);
    }
}
