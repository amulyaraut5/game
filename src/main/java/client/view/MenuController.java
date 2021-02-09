package client.view;

import ai.AIClient;
import client.model.Client;
import com.jfoenix.controls.JFXButton;
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
 * The MenuController is the Controller for the main menu view.
 *
 * @author simon
 */
public class MenuController extends Controller implements Updatable {
    private static final Logger logger = LogManager.getLogger();
    private volatile boolean timeout = false;

    @FXML
    private JFXButton hostButton;
    @FXML
    private JFXButton joinButton;
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

            if (isPortValid()) {
                buttonsEnabled = false;
                new Thread(() -> {
                    Server server = Server.getInstance();

                    if (!server.isAlive()) server.start();
                    System.out.println(server.getState());
                    connect(client);
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

            if (isPortValid()) {
                buttonsEnabled = false;
                infoLabel.setText("Trying to connect...");
                new Thread(() -> connect(client)).start();
            }
        }
    }

    @FXML
    public void aiJoinClicked() {
        if (buttonsEnabled) {
            logger.info("AI Join Clicked.");

            if (isPortValid()) {
                buttonsEnabled = false;
                infoLabel.setText("Trying to connect...");
                new Thread(() -> connect(new AIClient())).start();
            }
        }
    }

    private void connect(Client client) {
        Platform.runLater(() -> infoLabel.setText(""));
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
            if (!finalConnected) infoLabel.setText("The server is not reachable!");
            else infoLabel.setText("");
            hostButton.setDisable(false);
            joinButton.setDisable(false);
        });
    }

    @Override
    public void update(JSONMessage message) {
        if (message.getType() == MessageType.Error) {
            Error error = (Error) message.getBody();
            infoLabel.setText(error.getError());
        }
    }

    public boolean isPortValid() {
        if (!(textPortNumber.getText().isBlank())) {
            try {
                int portNr = Integer.parseInt(textPortNumber.getText());
                client.setPort(portNr);
                return true;
            } catch (NumberFormatException e) {
                infoLabel.setText("Port number invalid!");
                return false;
            }
        } else client.setPort(Constants.PORT);
        return true;
    }
}