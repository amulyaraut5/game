package ai;

import client.model.Client;
import game.Player;
import game.gameObjects.maps.Map;
import game.gameObjects.robot.Robot;
import utilities.Constants;
import utilities.Coordinate;
import utilities.JSONProtocol.JSONMessage;
import utilities.JSONProtocol.body.*;
import utilities.MapConverter;
import utilities.enums.*;

import java.util.*;

/**
 * This Class coordinates the messages received on the client and their responses from the AI.
 *
 * @author simon
 */
public class AIClient extends Client {
    private Map map;
    private GameState currentPhase = GameState.CONSTRUCTION;

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
            case TimerStarted, SelectionFinished, Error, TimerEnded, Energy, CardsYouGotNow,
                    ReceivedChat, PlayerStatus, HelloServer, SetStatus, SendChat,
                    SetStartingPoint, PlayIt, PlayerShooting, SelectMap, MapSelected, PlayerValues,
                    CardSelected, NotYourCards, DiscardHand, ShuffleCoding, CurrentCards -> {
                //nothing should happen, dummy case
            }
            case HelloClient -> sendMessage(new HelloServer(Constants.PROTOCOL, "Astreine Akazien", true));
            case Welcome -> {
                Welcome wc = (Welcome) message.getBody();
                thisPlayersID = wc.getPlayerID();

                Timer t = new Timer();
                t.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        choosePlayerValues();
                        t.cancel();
                    }
                }, 50);
            }
            case PlayerAdded -> {
                PlayerAdded playerAdded = (PlayerAdded) message.getBody();
                Player player = new Player(playerAdded);
                players.add(player);
                if (player.getID() == thisPlayersID) {
                    sendMessage(new SetStatus(true));
                }
            }
            case CurrentPlayer -> {
                CurrentPlayer msg = (CurrentPlayer) message.getBody();

                if (msg.getPlayerID() == thisPlayersID) {
                    logger.info("The orientation the AI thinks it is in is (before turn): " + getPlayerFromID(thisPlayersID).getRobot().getOrientation());
                    if (currentPhase == GameState.CONSTRUCTION) {
                        int[] startingPoints = {39, 78, 14, 53, 66, 105};
                        for (int point : startingPoints) {
                            sendMessage(new SetStartingPoint(point)); //choose not just first startingPoint
                        }
                    } else if (currentPhase == GameState.ACTIVATION) {
                        if (players.contains(getPlayerFromID(msg.getPlayerID()))) {
                            sendMessage(new PlayIt());
                        }
                    }
                }
            }
            case StartingPointTaken -> {
                StartingPointTaken msg = (StartingPointTaken) message.getBody();
                Player player = getPlayerFromID(msg.getPlayerID());
                Coordinate coordinate = Coordinate.parse(msg.getPosition());
                player.getRobot().setCoordinate(coordinate);
            }
            case GameStarted -> {
                GameStarted gameStarted = (GameStarted) message.getBody();
                map = MapConverter.reconvert(gameStarted);
            }
            case ActivePhase -> {
                ActivePhase msg = (ActivePhase) message.getBody();
                currentPhase = msg.getPhase();
                if (currentPhase == GameState.PROGRAMMING) {
                    for (Player player : rebootingAIs) {
                        logger.info(player.getID() + "is in.");
                    }

                    players.addAll(rebootingAIs);
                    rebootingAIs.clear();
                }
            }
            case YourCards -> {
                YourCards yourCards = (YourCards) message.getBody();
                chooseCards(yourCards);
            }
            case PickDamage -> {
                PickDamage pickDamage = (PickDamage) message.getBody();
                int countToPick = pickDamage.getCount();
                ArrayList<CardType> damageCards = new ArrayList<>();
                CardType chooseCard = null;
                while (countToPick > 0) {
                    if (getCountVirusCards() > 0) chooseCard = CardType.Virus;
                    else if (getCountSpamCards() > 0) chooseCard = CardType.Spam;
                    else if (getCountWormCards() > 0) chooseCard = CardType.Worm;
                    else if (getCountTrojanCards() > 0) chooseCard = CardType.Trojan;
                    damageCards.add(chooseCard);
                    countToPick--;
                }
                sendMessage(new SelectDamage(damageCards));
            }
            case CheckpointReached -> {
                CheckpointReached msg = (CheckpointReached) message.getBody();
                getPlayerFromID(msg.getPlayerID()).setCheckPointCounter(msg.getNumber());
            }
            case Movement -> {
                Movement msg = (Movement) message.getBody();
                Player ai = getPlayerFromID(msg.getPlayerID());
            }
            case PlayerTurning -> {
                PlayerTurning msg = (PlayerTurning) message.getBody();
                if(msg.getPlayerID() == thisPlayersID) {
                    if (msg.getDirection() == Rotation.RIGHT) {
                        getPlayerFromID(thisPlayersID).getRobot().setOrientation(getPlayerFromID(thisPlayersID).getRobot().getOrientation().getNext());
                    }
                    else {getPlayerFromID(thisPlayersID).getRobot().setOrientation(getPlayerFromID(thisPlayersID).getRobot().getOrientation().getPrevious());}
                    logger.info("AI think it's facing " + getPlayerFromID(thisPlayersID).getRobot().getOrientation());
                }
                /*
                Orientation orientation = Orientation.UP;
                switch (msg.getDirection()) {
                    case RIGHT -> orientation = Orientation.RIGHT;
                    case LEFT -> orientation = Orientation.LEFT;
                }

                getPlayerFromID(msg.getPlayerID()).getRobot().setOrientation(orientation);
                 */
            }
            case ConnectionUpdate -> {
                ConnectionUpdate msg = (ConnectionUpdate) message.getBody();
                if (msg.getAction().equals("Remove") && !msg.isConnected()) {
                    Player player = getPlayerFromID(msg.getID());
                    players.remove(player);

                    if (players.size() <= 1) {
                        disconnect();
                    }
                }
            }
            case Reboot -> {
                Reboot reboot = (Reboot) message.getBody();
                logger.info(reboot.getPlayerID() + "was out.");
                rebootingAIs.add(getPlayerFromID(reboot.getPlayerID()));
                players.remove(getPlayerFromID(reboot.getPlayerID()));
            }
            case DrawDamage -> {
                DrawDamage drawDamage = (DrawDamage) message.getBody();
                handleDamageCount(drawDamage.getCards());
                if (drawDamage.getPlayerID() == thisPlayersID) {
                    handleDamageCount(drawDamage.getCards());
                }
            }
            case GameWon -> {
                disconnect();
            }
            default -> logger.error("The MessageType " + type + " is invalid or not yet implemented!");
        }
    }

    private void chooseCards(YourCards yourCards) {
//        ArrayList<CardType> availableCards = new ArrayList<>(yourCards.getCards());
//        for (int i = 0; i < 5; i++) {
//            int rdm = new Random().nextInt(availableCards.size());
//            sendMessage(new SelectCard(availableCards.get(rdm), i + 1));
//            availableCards.remove(rdm);
//        }

        Set<CardType[]> combinations = createCardCombinations(yourCards.getCards());
        HashMap<CardType[], Coordinate> possiblePositions = new HashMap<>();
        MoveSimulator moveSimulator = new MoveSimulator(this, map);
        Robot robot = getPlayerFromID(thisPlayersID).getRobot();

        for (CardType[] cards : combinations) {
            if (cards[0] != CardType.Again) {
                Coordinate resPos = moveSimulator.simulateCombination(cards, robot.getCoordinate(), robot.getOrientation());
                //System.out.println("resPos: " + Arrays.toString(cards) + " " + resPos);
                if (resPos != null) possiblePositions.put(cards, resPos);
            }
        }
        System.out.println("YourCards: " + yourCards.getCards());

        CardType[] bestCombination = getBestCombination(possiblePositions);

        for (int i = 0; i < 5; i++) {
            sendMessage(new SelectCard(bestCombination[i], i + 1));
        }
    }

    private CardType[] getBestCombination(HashMap<CardType[], Coordinate> resultingPositions) {
        int shortestDistance = 100;
        CardType[] bestCombination = null;

        Set<CardType[]> keySet = resultingPositions.keySet();

        Player player = getPlayerFromID(thisPlayersID);
        int nextControlPoint = player.getCheckPointCounter();
        Coordinate controlPoint = map.readControlPointCoordinate().get(nextControlPoint);

        for (CardType[] cards : keySet) {
            int distance = Coordinate.distance(resultingPositions.get(cards), controlPoint);
            //logger.trace("distance: " + distance + " " + Arrays.toString(cards));
            if (distance < shortestDistance) {
                shortestDistance = distance;
                bestCombination = cards;
            }
        }
        //logger.trace("best distance: " + shortestDistance + " " + Arrays.toString(bestCombination));
        return bestCombination;
    }

    public void choosePlayerValues() {
        List<Integer> takenFigures = new ArrayList<>();
        List<Integer> freeFigures = new ArrayList<>();

        for (Player player : players) {
            takenFigures.add(player.getFigure());
        }

        for (int i = 0; i < 6; i++) {
            freeFigures.add(i);
        }

        freeFigures.removeAll(takenFigures);

        if (freeFigures.size() > 0) {
            Random r = new Random();
            int chosenFigure = freeFigures.get(r.nextInt(freeFigures.size()));
            String name = "AI " + thisPlayersID;
            sendMessage(new PlayerValues(name, chosenFigure));
        } else disconnect();
    }
}
