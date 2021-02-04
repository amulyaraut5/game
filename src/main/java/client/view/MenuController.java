package client.view;

import ai.AIClient;
import client.model.Client;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.Server;
import utilities.JSONProtocol.JSONMessage;
import utilities.JSONProtocol.body.Error;
import utilities.Updatable;

/**
 * The MenuController is the Controller for the main menu view.
 *
 * @author simon
 */
public class MenuController extends Controller implements Updatable {
    private static final Logger logger = LogManager.getLogger();

    @FXML
    private Label infoLabel;

    /**
     * Method creates a new Server and Client and opens the Login view
     */
    @FXML
    public void hostGameClicked() {
        logger.info("Host Game Clicked.");

        new Thread(() -> {
            Server server = Server.getInstance();
            if (!server.isAlive()) server.start();
            else logger.warn("The server is already running.");
            connect(client);
        }).start();
    }

    /**
     * Method creates a new Client and opens the Login view
     */
    @FXML
    public void joinGameClicked() {
        logger.info("Join Game Clicked.");
        new Thread(() -> connect(client)).start();
    }

    @FXML
    public void aiJoinClicked() {
        connect(new AIClient());
    }

    private void connect(Client client) {
        boolean connected = client.establishConnection();
        Platform.runLater(() -> {
            if (!connected) infoLabel.setText("The server is not reachable!");
            else infoLabel.setText("");
        });
    }

    @Override
    public void update(JSONMessage message) {
        switch (message.getType()) {
            case Error -> {
                Error error = (Error) message.getBody();
                infoLabel.setText(error.getError());
            }
        }
    }
}