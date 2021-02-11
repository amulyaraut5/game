package client.view;

import ai.AIClient;
import client.model.Client;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
 * The MenuController is the Controller for the main menu view. TODO
 *
 * @author simon
 */
public class MenuController extends Controller implements Updatable {
    private static final Logger logger = LogManager.getLogger();
    private volatile boolean timeout = false;
    private volatile boolean buttonsEnabled = true;

    @FXML
    private Label infoLabel;
    @FXML
    private TextField textPortNumber;

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

    @Override
    public void update(JSONMessage message) {
        if (message.getType() == MessageType.Error) {
            Error error = (Error) message.getBody();
            Updatable.showInfo(infoLabel, error.getError());
        }
    }

    public boolean changePort() {
        return changePort(null);
    }

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