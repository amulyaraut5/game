package server;

import client.view.GameBoardController;
import game.Game;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.Coordinate;
import utilities.JSONProtocol.JSONMessage;
import utilities.JSONProtocol.body.*;
import utilities.Updatable;
import utilities.enums.Rotation;

import java.io.IOException;

public class ServerController implements Updatable {
    private static final Logger logger = LogManager.getLogger();
    private Server server = Server.getInstance();
    private Game game = Game.getInstance();

    private GameBoardController gameBoardController;

    @FXML
    private Pane boardPane;

    @FXML
    private void initialize() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/innerViews/gameBoard.fxml"));
            boardPane.getChildren().add(fxmlLoader.load());

            gameBoardController = fxmlLoader.getController();
        } catch (IOException e) {
            logger.error("GameBoard could not be created: " + e.getMessage());
        }
    }

    @Override
    public void update(JSONMessage message) {
        switch (message.getType()) {
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
            case PickDamage -> {
            }
            case PlayerShooting -> {
                gameBoardController.handleShooting(game.getPlayers());
                gameBoardController.handleRobotShooting(game.getPlayers());
            }
            case Reboot -> {
                Reboot reboot = (Reboot) message.getBody();
            }
            case SelectionFinished -> {
            }
            case TimerStarted -> {
            }
            case TimerEnded -> {
            }
            case CurrentCards -> {
            }
            case CurrentPlayer -> {
                //TODO display
            }
            case CheckpointReached -> {
                //todo show
            }
            case GameWon -> {
                //TODO close game
            }
        }
    }
}
