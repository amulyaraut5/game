package ai;

import client.model.Client;
import game.Player;
import game.gameObjects.maps.Map;
import javafx.application.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.Coordinate;
import utilities.JSONProtocol.JSONMessage;
import utilities.JSONProtocol.body.Error;
import utilities.JSONProtocol.body.*;
import utilities.MapConverter;
import utilities.Utilities;
import utilities.enums.GameState;
import utilities.enums.MessageType;
import utilities.enums.Orientation;

import java.util.ArrayList;

public class AICoordinator {
    private static final Logger logger = LogManager.getLogger();
    private final ArrayList<Player> players = new ArrayList<>();
    private Client client = Client.getInstance();
    private int thisPlayersID;
    private Map map;
    private GameState activePhase;

    public void handleMessage(JSONMessage message) {
        MessageType type = message.getType();

        Platform.runLater(() -> {
            switch (type) {
                case HelloClient -> client.sendMessage(new HelloServer(Utilities.PROTOCOL, "Astreine Akazien", true));
                case Welcome -> {
                    Welcome wc = (Welcome) message.getBody();
                    thisPlayersID = wc.getPlayerID();
                    client.sendMessage(new PlayerValues("AI Test", 2)); //TODO choose unselected figure
                }
                case PlayerAdded -> {
                    PlayerAdded playerAdded = (PlayerAdded) message.getBody();
                    Player player = new Player(playerAdded);
                    players.add(player);
                    if (player.getID() == thisPlayersID) {
                        client.sendMessage(new SetStatus(true));
                    }
                }
                case Error -> {
                    Error error = (Error) message.getBody();
                    //TODO catch error? e.g. if startingpoint is alreage taken
                }
                case ConnectionUpdate -> {
                    //TODO remove
                }
                case PlayerStatus -> {
                    PlayerStatus playerStatus = (PlayerStatus) message.getBody();
                    //TODO nothing
                }
                case ReceivedChat -> {
                    ReceivedChat receivedChat = (ReceivedChat) message.getBody();
                    //TODO nothing
                }
                case GameStarted -> {
                    GameStarted gameStarted = (GameStarted) message.getBody();
                    gameStarted(gameStarted);
                }
                case StartingPointTaken -> {
                    StartingPointTaken msg = (StartingPointTaken) message.getBody();
                    Player player = getPlayerFromID(msg.getPlayerID());
                    Coordinate coordinate = Coordinate.parse(msg.getPosition());
                    player.getRobot().setCoordinate(coordinate);
                }
                case ActivePhase -> {
                    ActivePhase msg = (ActivePhase) message.getBody();
                    activePhase = msg.getPhase();
                }
                case YourCards -> {
                    YourCards yourCards = (YourCards) message.getBody();
                    chooseCards(yourCards);
                }
                case CardsYouGotNow -> {
                    CardsYouGotNow msg = (CardsYouGotNow) message.getBody();
                    //TODO getPlayerFromID(thisPlayersID).setDrawnProgrammingCards(msg.getCards());
                }
                case SelectionFinished -> {
                    SelectionFinished selectionFinished = (SelectionFinished) message.getBody();
                    //TODO nothing
                }
                case TimerStarted -> {
                    //TODO nothing
                }
                case TimerEnded -> {
                    //TODO nothing
                }
                case CurrentCards -> {
                    //TODO
                }
                case Reboot -> {
                    Reboot reboot = (Reboot) message.getBody();
                    // TODO nothing
                }
                case Energy -> {
                    Energy energy = (Energy) message.getBody();
                    //TODO nothing
                }
                case CheckPointReached -> {
                    CheckpointReached msg = (CheckpointReached) message.getBody();
                    getPlayerFromID(msg.getPlayerID()).setCheckPointCounter(msg.getNumber());
                }
                case GameWon -> {
                    GameWon gameWon = (GameWon) message.getBody();
                    //TODO nothing
                }
                case Movement -> {
                    Movement msg = (Movement) message.getBody();
                    getPlayerFromID(msg.getPlayerID()).getRobot().setCoordinate(Coordinate.parse(msg.getTo()));
                }
                case PlayerTurning -> {
                    PlayerTurning msg = (PlayerTurning) message.getBody();
                    Orientation orientation = Orientation.UP;
                    switch (msg.getDirection()) {
                        case RIGHT -> orientation = Orientation.RIGHT;
                        case LEFT -> orientation = Orientation.LEFT;
                    }
                    getPlayerFromID(msg.getPlayerID()).getRobot().setOrientation(orientation);
                }
                case CardSelected -> {
                    CardSelected cardSelected = (CardSelected) message.getBody();
                } //TODO
                case NotYourCards -> {
                    NotYourCards notYourCards = (NotYourCards) message.getBody();
                } //TODO
                case ShuffleCoding -> {
                    ShuffleCoding shuffleCoding = (ShuffleCoding) message.getBody();
                } //TODO
                case DiscardHand -> {
                    DiscardHand discardHand = (DiscardHand) message.getBody();
                } //TODO
                default -> logger.error("The MessageType " + type + " is invalid or not yet implemented!");
                case CurrentPlayer -> {
                    CurrentPlayer msg = (CurrentPlayer) message.getBody();
                    if (msg.getPlayerID() == thisPlayersID) {
                        client.sendMessage(new PlayIt());
                    }
                }
            }
        });
    }

    private void gameStarted(GameStarted gameStarted) {
        map = MapConverter.reconvert(gameStarted);
        for (int i = 0; i < 130; i++) {
            client.sendMessage(new SetStartingPoint(i)); //TODO try only for all 6 startingpoints
        }
    }

    private void chooseCards(YourCards yourCards) {
        for (int i = 0; i < 5; i++) {
            client.sendMessage(new SelectCard(yourCards.getCards().get(i), i + 1));
        }
    }

    /**
     * Gets a player based on their ID from the list of players saved in {@link Client}.
     *
     * @param id ID of the wanted player.
     * @return Unique player with the ID, {@code null} if no player with the ID exists.
     */
    public Player getPlayerFromID(int id) {
        for (Player player : players) {
            if (player.getID() == id) return player;
        }
        return null;
    }
}
