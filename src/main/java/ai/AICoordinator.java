package ai;

import client.model.Client;
import game.Player;
import game.gameObjects.maps.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.Coordinate;
import utilities.JSONProtocol.JSONMessage;
import utilities.JSONProtocol.body.Error;
import utilities.JSONProtocol.body.*;
import utilities.MapConverter;
import utilities.Utilities;
import utilities.enums.CardType;
import utilities.enums.GameState;
import utilities.enums.MessageType;
import utilities.enums.Orientation;

import java.util.*;

/**
 * This Class coordinates the messages received on the client and their responses from the AI.
 *
 * @author simon
 */
public class AICoordinator {
    private static final Logger logger = LogManager.getLogger();
    private final ArrayList<Player> players = new ArrayList<>();
    private Client client = Client.getInstance();
    private int thisPlayersID;
    private Map map;
    private GameState activePhase;

    /**
     * creates a set containing all combinations and their permutations
     * of 5 cards to choose from the 9 drawn card of the deck.
     *
     * @return A Set of all combinations of selected cards.
     */
    private static Set<CardType[]> createCardCombinations(ArrayList<CardType> cards) {
        Set<CardType[]> combinations = new HashSet<>();

        for (int one = 0; one < cards.size(); one++) {
            for (int two = 0; two < cards.size(); two++) {
                if (two != one) {
                    for (int three = 0; three < cards.size(); three++) {
                        if (three != one && three != two) {
                            for (int four = 0; four < cards.size(); four++) {
                                if (four != one && four != two && four != three) {
                                    for (int five = 0; five < cards.size(); five++) {
                                        if (five != one && five != two && five != three && five != four) {
                                            combinations.add(new CardType[]{cards.get(one), cards.get(two),
                                                    cards.get(three), cards.get(four), cards.get(five)});
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return combinations;
    }

    /**
     * mandles the received messages on the clientside for the AI.
     *
     * @param message message from the ReaderThread
     */
    public void handleMessage(JSONMessage message) {
        MessageType type = message.getType();

        switch (type) {
            case TimerStarted, TimerEnded, Energy, ReceivedChat, GameWon, PlayerStatus -> {
                //TODO nothing
            }
            case HelloClient -> client.sendMessage(new HelloServer(Utilities.PROTOCOL, "Astreine Akazien", true));
            case Welcome -> {
                Welcome wc = (Welcome) message.getBody();
                thisPlayersID = wc.getPlayerID();
                Timer t = new java.util.Timer();
                t.schedule(
                        new java.util.TimerTask() {
                            @Override
                            public void run() {
                                choosePlayerValues();
                                t.cancel();
                            }
                        }, 1000
                );
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
                //TODO remove player
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
            case CurrentCards -> {
                //TODO
            }
            case Reboot -> {
                Reboot reboot = (Reboot) message.getBody();
                // TODO nothing
            }
            case CheckpointReached -> {
                CheckpointReached msg = (CheckpointReached) message.getBody();
                getPlayerFromID(msg.getPlayerID()).setCheckPointCounter(msg.getNumber());
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
    }

    private void gameStarted(GameStarted gameStarted) {
        map = MapConverter.reconvert(gameStarted);
        int[] startingPoints = {40, 79, 15, 54, 67, 106};

        for (int i : startingPoints) {
            client.sendMessage(new SetStartingPoint(i));
        }
    }

    private void chooseCards(YourCards yourCards) {
        ArrayList<CardType> availableCards = new ArrayList<>(yourCards.getCards());

        for (int i = 0; i < 5; i++) {
            int rdm = new Random().nextInt(availableCards.size());
            client.sendMessage(new SelectCard(availableCards.get(rdm), i + 1));
            availableCards.remove(rdm);
        }

        Set<CardType[]> combinations = createCardCombinations(yourCards.getCards());
        HashMap<CardType[], Coordinate> resultingPositions = new HashMap<>();
        HashMap<Coordinate, Set<CardType[]>> combinationsForPositions = new HashMap<>();
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

    public void choosePlayerValues() {

        List<Integer> a = new ArrayList<>();
        List<Integer> b = new ArrayList<>();

        for (Player player : players) {
            a.add(player.getFigure());
        }

        for (int i = 0; i < 6; i++) {
            b.add(i);
        }

        b.removeAll(a);

        if (b.size() > 0) {
            String name = b.get(0).toString() + "_AI";
            client.sendMessage(new PlayerValues(name, b.get(0)));
        }

    }
}
