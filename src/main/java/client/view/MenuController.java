package client.view;

import client.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.Server;

import static utilities.Utilities.PORT;

public class MenuController {
    private Main main;
    private static final Logger logger = LogManager.getLogger();

    @FXML
    public void joinGameClicked(ActionEvent event) {
        logger.info("join Game Clicked");
        main.constructLoginStage();
    }

    @FXML
    public void hostGameClicked(ActionEvent event) {
        //Server server = new Server(PORT);
        //server.start();

        main.constructLoginStage();
    }

    public void setMain(Main main) {
        this.main = main;
    }
}