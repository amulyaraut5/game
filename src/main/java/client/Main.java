package client;

import javafx.application.Application;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main extends Application {
    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        logger.info("MAIN METHOD STARTED");
        launch(args);
        logger.info("MAIN METHOD ENDED");
    }

    @Override
    public void start(Stage menuStage) {
        ViewManager.getInstance();
    }
}
