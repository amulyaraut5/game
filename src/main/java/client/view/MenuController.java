package client.view;

import client.ViewManager;
import client.model.Client;
import javafx.fxml.FXML;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.Server;

import static utilities.Utilities.PORT;

/**
 * The MenuController is the Controller for the main menu view.
 *
 * @author simon
 */
public class MenuController extends Controller {
    private static final Logger logger = LogManager.getLogger();

    /**
     * Method creates a new Server and Client and opens the Login view
     */
    @FXML
    public void hostGameClicked() {
        logger.info("Host Game Clicked");

        //TODO test if a server already exists on localhost? or print out that the user only joined
        Thread t = new Thread(() -> {
            new Server().start();
        });
        t.setName("Server Thread");
        t.start();

        client.establishConnection();
        viewManager.nextScene();
    }

    /**
     * Method creates a new Client and opens the Login view
     */
    @FXML
    public void joinGameClicked() {
        logger.info("Join Game Clicked");

        client.establishConnection();
        viewManager.nextScene();
    }
}