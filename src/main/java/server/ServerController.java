package server;

import ai.AIClient;
import client.model.Client;
import client.view.GameBoardController;
import com.jfoenix.controls.JFXButton;
import game.Game;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.Coordinate;
import utilities.JSONProtocol.JSONMessage;
import utilities.JSONProtocol.body.Error;
import utilities.JSONProtocol.body.*;
import utilities.Updatable;
import utilities.enums.Rotation;
import utilities.enums.ServerState;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class ServerController implements Updatable {
    private static final Logger logger = LogManager.getLogger();
    private final Server server = Server.getInstance();
    private final Game game = Game.getInstance();
    private volatile boolean timeout = false;

    private GameBoardController gameBoardController;

    public AnchorPane AIWonPane;
    @FXML
    private Pane boardPane;
    @FXML
    private Label infoLabel;
    @FXML
    private JFXButton startButton;
    @FXML
    private JFXButton AIButton;
    @FXML
    private HBox iconPane;
    @FXML
    private Pane instructionPane;

    @FXML
    private void initialize() {
        iconPane.setSpacing(40);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/innerViews/gameBoard.fxml"));
            boardPane.getChildren().add(fxmlLoader.load());

            gameBoardController = fxmlLoader.getController();
        } catch (IOException e) {
            logger.error("GameBoard could not be created: " + e.getMessage());
        }
        Updatable.showInfo(infoLabel, "Server started!     ");
    }

    @FXML
    public void startClicked() {
        if (server.getServerState() != ServerState.RUNNING_GAME) {
            if (server.getUsers().size() > 1) {
                Updatable.showInfo(infoLabel, "Game started!");
                startButton.setDisable(true);
                AIButton.setDisable(true);
                startButton.setVisible(false);
                AIButton.setVisible(false);
                instructionPane.setVisible(false);
                server.startAIGame();
            } else Updatable.showInfo(infoLabel, "Not enough users!");
        } else Updatable.showInfo(infoLabel, "Game already running!");
    }

    @FXML
    public void addAIClicked() {
        if (server.getUsers().size() < 6) {
            Updatable.showInfo(infoLabel, "AI joined!");
            iconPane.getChildren().add(new Label("AI " + server.getUsers().size()));
            new Thread(() -> connect(new AIClient())).start();
            if (server.getUsers().size() == 6) {
                Updatable.showInfo(infoLabel, "Last AI Joined!");
                AIButton.setDisable(true);
                AIButton.setVisible(false);
            }
        } else {
            Updatable.showInfo(infoLabel, "Server already full!");
            AIButton.setDisable(true);
            AIButton.setVisible(false);
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
    }


    @Override
    public void update(JSONMessage message) {
        switch (message.getType()) {
            case Error -> Updatable.showInfo(infoLabel, ((Error) message.getBody()).getError());
            case GameStarted -> {
                GameStarted gameStarted = (GameStarted) message.getBody();
                gameBoardController.buildMap(gameStarted);
                //othersController.createPlayerMats(client.getPlayers());
            }
            case StartingPointTaken -> {
                StartingPointTaken msg = (StartingPointTaken) message.getBody();
                gameBoardController.placeRobotInMap(game.getPlayerFromID(msg.getPlayerID()), Coordinate.parse(msg.getPosition()));
            }
            case ActivePhase -> {
                ActivePhase activePhase = (ActivePhase) message.getBody();
                //todo display phase
            }
            case YourCards -> {
                YourCards yourCards = (YourCards) message.getBody();
                //TODO programmingController.startProgrammingPhase(yourCards.getCards());
            }
            case CardsYouGotNow -> {
                CardsYouGotNow cardsYouGotNow = (CardsYouGotNow) message.getBody();
                //TODO playerMatController.setNewCardsYouGotNow(cardsYouGotNow);
            }
            case Movement -> {
                Movement msg = (Movement) message.getBody();
                gameBoardController.handleMovement(game.getPlayerFromID(msg.getPlayerID()), Coordinate.parse(msg.getTo()));
            }
            case PlayerTurning -> {
                PlayerTurning msg = (PlayerTurning) message.getBody();
                int angle;
                if (msg.getDirection() == Rotation.LEFT) angle = -90;
                else angle = 90;
                gameBoardController.handlePlayerTurning(game.getPlayerFromID(msg.getPlayerID()), angle);
            }
            case CardSelected -> {
                CardSelected cardSelected = (CardSelected) message.getBody();
                //othersController.getOtherPlayerController(cardSelected.getPlayerID()).cardSelected(cardSelected.getRegister());
            }
            case PickDamage, CurrentCards, TimerEnded, TimerStarted, SelectionFinished -> {
            }
            case PlayerShooting -> {
                gameBoardController.robotLaserAnimation(game.getPlayers());
                gameBoardController.boardLaserAnimation(game.getPlayers());
            }
            case Reboot -> {
                Reboot reboot = (Reboot) message.getBody();
            }
            case CurrentPlayer -> {
                //TODO display
            }
            case CheckpointReached -> {
                //todo show
            }
            case GameWon -> {
                AIWonPane.setVisible(true);
                //TODO close
            }
        }
    }
}
