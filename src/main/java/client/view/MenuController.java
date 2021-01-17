package client.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.Server;

/**
 * The MenuController is the Controller for the main menu view.
 *
 * @author simon
 */
public class MenuController extends Controller {
    private static final Logger logger = LogManager.getLogger();

    @FXML
    private Label infoLabel;

    /**
     * Method creates a new Server and Client and opens the Login view
     */
    @FXML
    public void hostGameClicked() {
        logger.info("Host Game Clicked.");

        Server server = Server.getInstance();
        if (!server.isAlive()) {
            Server.getInstance().start();
        }else{
            logger.warn("Server is already running.");
        }
        connect();
    }

    /**
     * Method creates a new Client and opens the Login view
     */
    @FXML
    public void joinGameClicked() {
        logger.info("Join Game Clicked.");

        connect();
    }

    private void connect() {
        infoLabel.setText("");
        boolean connected = client.establishConnection();
        if (!connected) infoLabel.setText("The Server is not reachable!");
    }
}