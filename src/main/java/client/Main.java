package client;

import client.model.Client;
import client.view.GameViewController;
import client.view.LoginController;
import client.view.MenuController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class Main extends Application {

    private Stage menuStage;
    private Stage gameStage;
    private Scene menuScene;
    private Scene loginScene;
    private Scene gameScene;
    private Parent login = null;
    private Parent game = null;
    private Parent menu = null;
    private GameViewController gameViewController;

    private Stage currentStage = menuStage;

    private LoginController loginController;

    static final Logger logger = LogManager.getLogger();

    private Client client;

    public static void main(String[] args) {
        logger.info("Main Method started");
        launch(args);
        logger.info("Main Method ended");
    }

    @Override
    public void start(Stage menuStage) throws IOException {
        this.menuStage = menuStage;//TODO test, if this works, because no object of Main is yet created

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
        loginController.createClient();
        menuStage.setScene(loginScene);

        menuStage.setOnCloseRequest(event -> {
            menuStage.close();
        });

    }

    public void constructGameStage() {
        menuStage.close();
        if (gameStage == null) {
            gameStage = new Stage();
        }
        gameViewController.setClient(client);
        gameStage.setTitle("RoboRally - Game");
        gameStage.setResizable(false);
        gameStage.setScene(gameScene);
        gameStage.show();
        gameStage.setOnCloseRequest(event -> {
            gameStage.close();
        });
    }

    private void createControllerAndParents() throws IOException {
        //menu
        FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("/view/menuView.fxml"));
        menu = menuLoader.load();
        MenuController menuController = menuLoader.getController();
        menuController.setMain(this);

        //login
        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/view/loginView.fxml"));
        login = loginLoader.load();
        loginController = loginLoader.getController();
        loginController.setMain(this);

        //game
        FXMLLoader gameLoader = new FXMLLoader(getClass().getResource("/view/gameView.fxml"));
        game = gameLoader.load();
        gameViewController = gameLoader.getController();
        gameViewController.setMain(this);

    }

    public void showGameStage() {
        menuStage.close();

        gameStage.show();
    }

    public void showMenuStage() {
        gameStage.close();
        menuStage.setScene(loginScene);
        menuStage.show();
    }

    public void setClient(Client client) {
        this.client = client;
    }


    public void sendChatMessage(String body, String type) {
        if(type.equals("receivedChat")){
            gameViewController.setTextArea(body);
        } else if(type.equals("playerAdded")){
            gameViewController.setUsersTextArea(body);
        }

    }
}
