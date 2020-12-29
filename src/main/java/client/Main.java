package client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class Main extends Application {

    static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        logger.info("Main Method started");
        launch(args);
        logger.info("Main Method ended");
    }

    @Override
    public void start(Stage menuStage) throws IOException {
        ViewManager.getInstance();
    }


    public void sendChatMessage(String body, String type) {
        /*if(type.equals("loginController")) loginController.write(body);
        else if(type.equals("receivedChat")) gameViewController.setTextArea(body);
        else if(type.equals("playerAdded")) gameViewController.setUsersTextArea(body);*/

    }

}
