package client.view;

import ai.AIClient;
import client.model.Client;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.Server;
import utilities.Constants;
import utilities.JSONProtocol.JSONMessage;
import utilities.JSONProtocol.body.Error;
import utilities.Updatable;
import utilities.enums.MessageType;

import java.util.Timer;
import java.util.TimerTask;

/**
 * The MenuController is the Controller for the main menu view.
 *
 * @author simon
 */
public class MenuController extends Controller implements Updatable {
    private static final Logger logger = LogManager.getLogger();
    private volatile boolean timeout = false;
    private volatile boolean buttonsEnabled = true;

    @FXML
    private Label infoLabel;
    /**
     * This JFXTextField takes the port or the default port.
     */
    @FXML
    private JFXTextField textPortNumber;

    /**
     * Method creates a new Server and Client and opens the Login view
     */
    @FXML
    public void hostGameClicked() {
        if (buttonsEnabled) {
            logger.info("Host Game Clicked.");
            Server server = Server.getInstance();

            if (changePort(server)) {
                buttonsEnabled = false;
                new Thread(() -> {
                    logger.trace(server.getState());
                    if (!server.isAlive()) server.start();
                    connect(viewClient);
                }).start();
            }
        }
    }

    /**
     * Method creates a new Client and opens the Login view
     */
    @FXML
    public void joinGameClicked() {
        if (buttonsEnabled) {
            logger.info("Join Game Clicked.");

            if (changePort()) {
                buttonsEnabled = false;
                Updatable.showInfo(infoLabel, "Trying to connect...");
                new Thread(() -> connect(viewClient)).start();
            }
        }
    }

    /**
     * This method gets called by clicking on the join button and tries to connect an AIClient
     */
    @FXML
    public void aiJoinClicked() {
        if (buttonsEnabled) {
            logger.info("AI Join Clicked.");

            if (changePort()) {
                buttonsEnabled = false;
                Updatable.showInfo(infoLabel, "Trying to connect...");
                new Thread(() -> connect(new AIClient())).start();
            }
        }
    }

    private void connect(Client client) {
        boolean connected = false;
        timeout = false;

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                timeout = true;
                cancel();
            }
        }, 2000);

        while (!connected && !timeout) {
            connected = client.establishConnection();
            synchronized (this) {
                try {
                    wait(200);
                } catch (InterruptedException ignored) {
                }
            }
        }
        boolean finalConnected = connected;
        Platform.runLater(() -> {
            if (!finalConnected) Updatable.showInfo(infoLabel, "Server not reachable!");
            else if (client instanceof AIClient) Updatable.showInfo(infoLabel, "AI joined successfully!");
            buttonsEnabled = true;
        });
    }

    /**
     * Outputs the error on the infoLabel.
     * @param message the JSONMessage
     */
    @Override
    public void update(JSONMessage message) {
        if (message.getType() == MessageType.Error) {
            Error error = (Error) message.getBody();
            Updatable.showInfo(infoLabel, error.getError());
        }
    }

    /**
     * Checks for the port.
     * @return if port is changed
     */
    public boolean changePort() {
        return changePort(null);
    }

    /**
     * Checks for the port.
     * @param server the server
     * @return if port is changed
     */
    public boolean changePort(Server server) {
        if (!textPortNumber.getText().isBlank()) {
            try {
                int portNr = Integer.parseInt(textPortNumber.getText());
                if (portNr > 1023 && portNr < 65536) {
                    viewClient.setPort(portNr);
                    if (server != null) server.setPort(portNr);
                    return true;
                } else Updatable.showInfo(infoLabel, "Use port number between 1024 - 65535!");
            } catch (NumberFormatException e) {
                Updatable.showInfo(infoLabel, "Port number invalid!");
            }
        } else {
            viewClient.setPort(Constants.PORT);
            if (server != null) server.setPort(Constants.PORT);
            return true;
        }
        return false;
    }
}