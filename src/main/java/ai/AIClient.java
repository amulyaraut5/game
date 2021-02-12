package ai;

import client.model.Client;
import game.Game;
import game.Player;
import game.gameObjects.maps.Map;
import game.gameObjects.robot.Robot;
import utilities.Constants;
import utilities.Coordinate;
import utilities.JSONProtocol.JSONMessage;
import utilities.JSONProtocol.body.*;
import utilities.MapConverter;
import utilities.enums.CardType;
import utilities.enums.GameState;
import utilities.enums.MessageType;
import utilities.enums.Rotation;

import java.util.*;

/**
 * This Class coordinates the messages received on the client and their responses from the AI.
 *
 * @author simon,Louis
 */
public class AIClient extends Client {
    /**
     * Game map.
     */
    private Map map;

    /**
     * the current phase of the game.
     */
    private GameState currentPhase = GameState.CONSTRUCTION;
    /**
     * Instance of game.
     */
    private Game game = Game.getInstance();

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
     * handles the received messages on the clientside for the AI.
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
                CardType test[] = new CardType[] {CardType.MoveII, CardType.Spam, CardType.MoveII, CardType.MoveII, CardType.MoveII };
                logger.info(cardsToString(test));
                CardType[] test1= handleDamageCards(test);
                logger.info(cardsToString(test1));
                Welcome wc = (Welcome) message.getBody();
                thisPlayersID = wc.getPlayerID();
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        choosePlayerValues();
                        cancel();
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
                int virusCount = game.getVirusDeck().size();
                int spamCount = game.getSpamDeck().size();
                int wormCount = game.getWormDeck().size();
                int trojanCount = game.getTrojanDeck().size();
                while (countToPick > 0) {
                    if (virusCount > 0) {
                        chooseCard = CardType.Virus;
                        virusCount--;
                    }
                    else if (spamCount > 0) {
                        chooseCard = CardType.Spam;
                        spamCount--;
                    }
                    else if (wormCount > 0){
                        chooseCard = CardType.Worm;
                        wormCount--;
                    }
                    else if (trojanCount > 0) {
                        chooseCard = CardType.Trojan;
                        trojanCount--;
                    }
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
                if (ai != null) {
                    ai.getRobot().setCoordinate(Coordinate.parse(msg.getTo()));
                }
            }
            case PlayerTurning -> {
                PlayerTurning msg = (PlayerTurning) message.getBody();
                Robot r = getPlayerFromID(msg.getPlayerID()).getRobot();
                if (msg.getDirection() == Rotation.LEFT) {
                    r.setOrientation(r.getOrientation().getPrevious());
                } else {
                    r.setOrientation(r.getOrientation().getNext());
                }
                logger.info("AI think it's facing " + r.getOrientation());
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
            }
            case GameWon -> {
                Random r = new Random();
                synchronized (this) {
                    try {
                        wait(r.nextInt(1000));
                    } catch (InterruptedException ignored) {
                    }
                }
                disconnect();
            }
            default -> logger.error("The MessageType " + type + " is invalid or not yet implemented!");
        }
    }

    /**
     * Chooses five cards from the received yourCards protocol to fill the registers with.
     *
     * @param yourCards received yourCards protocol
     */
    private void chooseCards(YourCards yourCards) {
        Set<CardType[]> combinations = createCardCombinations(yourCards.getCards());
        HashMap<CardType[], Coordinate> possiblePositions = new HashMap<>();

        int nextControlPoint = getPlayerFromID(thisPlayersID).getCheckPointCounter();
        Coordinate controlPoint = map.readControlPointCoordinate().get(nextControlPoint);

        MoveSimulator moveSimulator = new MoveSimulator(this, map, controlPoint);
        Robot robot = getPlayerFromID(thisPlayersID).getRobot();

        for (CardType[] cards : combinations) {
            if (cards[0] != CardType.Again) {
                Coordinate resPos = moveSimulator.simulateCombination(cards, robot.getCoordinate(), robot.getOrientation());
                //System.out.println("resPos: " + Arrays.toString(cards) + " " + resPos); //TODO
                if (resPos != null) possiblePositions.put(cards, resPos);
            }
        }
        System.out.println("YourCards: " + yourCards.getCards());

        CardType[] bestCombination = getBestCombination(possiblePositions);

        if (bestCombination == null) {
            bestCombination = createRandomCombination(yourCards);
        }

        CardType[] improvedCombination = handleDamageCards(bestCombination);

        for (int i = 0; i < 5; i++) {
            CardType cardType = improvedCombination[i];
            sendMessage(new SelectCard(cardType, i + 1));
        }
    }

    private CardType[] createRandomCombination(YourCards yourCards) {
        CardType[] randCombination = new CardType[5];
        ArrayList<CardType> availableCards = new ArrayList<>(yourCards.getCards());
        for (int i = 0; i < 5; i++) {
            int rdm = new Random().nextInt(availableCards.size());
            randCombination[i] = availableCards.get(rdm);
            availableCards.remove(rdm);
        }
        return randCombination;
    }

    /**
     * Approach to choose the best card combination(consisting of five cards)  from all possible ones.
     *
     * @param resultingPositions Hashmap that includes all Card possible combinations, mapped to their resulting positions.
     */
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

    /**
     * Creates the PlayerValues protocol message. To do so, it chooses a random still available player figure.
     *
     */
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


    public CardType[] handleDamageCards(CardType[] combination){
        CardType[] newList = new CardType[5];
        int i = 0;
        for (CardType card : combination) {
            if(!isDamageCard(card)){
                newList[i] = card;
                i++;
            }
        }
        for (CardType card : combination) {
            if(isDamageCard(card)){
                newList[i] = card;
                i++;
            }
        }

        return newList;
    }



    public boolean isDamageCard(CardType card){
        switch (card){
            case Spam,Virus,Trojan,Worm:
                return true;
        }
        return false;

    }

    public String cardsToString(CardType[] array){
        String output = "";
        for (CardType card : array) {
            switch (card){
                case MoveII -> output += "MoveII ";
                case Spam -> output += "Spam";
            }
        }
        return output;
    }


}
