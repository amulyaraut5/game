package game.round;

import game.Player;
import game.gameActions.AgainAction;
import game.gameActions.RebootAction;
import game.gameObjects.cards.Card;
import game.gameObjects.cards.damage.Spam;
import game.gameObjects.cards.damage.Trojan;
import game.gameObjects.cards.damage.Virus;
import game.gameObjects.cards.damage.Worm;
import game.gameObjects.decks.*;
import game.gameObjects.robot.Robot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.User;
import utilities.Coordinate;
import utilities.JSONProtocol.body.Error;
import utilities.JSONProtocol.body.*;
import utilities.RegisterCard;
import utilities.enums.AttributeType;
import utilities.enums.CardType;
import utilities.enums.Orientation;
import utilities.enums.Rotation;

import java.util.ArrayList;
import java.util.Comparator;

import static java.lang.StrictMath.abs;

/**
 * The Activation Phase is the third phase in the Round.
 * In this class the Programming Cards and GameBoard Tiles are activated.
 *
 * @author janau, sarah, annika
 */

public class ActivationPhase extends Phase {

    private static final Logger logger = LogManager.getLogger();

    private final ArrayList<RegisterCard> currentCards = new ArrayList<>();
    private final ArrayList<CardType> cardTypes = new ArrayList<>();

    private final BoardElements activationElements = new BoardElements(this);
    private final LaserAction laserAction = new LaserAction();
    private final ArrayList<Player> activePlayers = players;
    private final ArrayList<Player> rebootedPlayers = new ArrayList<>();

    private final SpamDeck spamDeck = game.getSpamDeck();
    private final TrojanDeck trojanDeck = game.getTrojanDeck();
    private final VirusDeck virusDeck = game.getVirusDeck();
    private final WormDeck wormDeck = game.getWormDeck();

    private final ArrayList<Player> priorityList = calculatePriority(map.getAntenna());
    /**
     * keeps track of the current register
     */
    private int currentRegister = 1;

    /**
     * starts the ActivationPhase.
     * After each register the method for activating the board tiles ist called.
     * In every register the priority is determined (-> turnCards) and the players cards get activated
     * in priority order.
     */
    public ActivationPhase() {
        super();
        turnCards(currentRegister);
    }

    public ArrayList<RegisterCard> getCurrentCards() {
        return currentCards;
    }

    /**
     * At the beginning of each register the cards of each player in the current register is shown.
     * This is already in priority order.
     */
    public void turnCards(int register) {
        for (Player player : calculatePriority(map.getAntenna())) {
            RegisterCard playerRegisterCard = new RegisterCard(player.getID(), player.getRegisterCard(register));
            currentCards.add(playerRegisterCard);
        }
        server.communicateAll(new CurrentCards(currentCards));
        //logger.info("turnCards" + calculatePriority((gameMap.getAntenna())));
        server.communicateAll(new CurrentPlayer((currentCards.get(0)).getPlayerID()));
    }

    /**
     * This method is called whenever a PlayIt() protocol is received.
     * it is checked if its the given players turn. If yes, the cards is handled.
     */
    public void activateCards(int playerID) {

        //Because currentCards is in priority order the first person to activate their cards is at index 0.
        //So by removing the index 0 after every players turn the current player is always at index 0.
        RegisterCard playerRegisterCard = currentCards.get(0);

        //if its this players turn his card is activated
        outerLoop:
        if (playerRegisterCard.getPlayerID() == playerID) {
            CardType currentCard = playerRegisterCard.getCard();
            handleCard(currentCard, game.getPlayerFromID(playerID));
            currentCards.remove(0);

            //if he was the last player to send the PlayIt() protocol for this register the board is activated
            if (activePlayers.isEmpty()) {
                activePlayers.addAll(rebootedPlayers);
                rebootedPlayers.clear();
                game.nextPhase();
                break outerLoop;
            }
            if (currentCards.isEmpty()) {
                endOfRound();
            } else {
                server.communicateAll(new CurrentPlayer((currentCards.get(0)).getPlayerID()));
            }
        } else { //if the player at index 0 is not the player that send the PlayIt() protocol he gets an error
            server.communicateDirect(new Error("It is not your turn!"), playerID);
        }
    }

    public void endOfRound() {
        activateBoard();

        if (currentRegister < 5) { //if it is not the 5th register yet the cards from the next register are turned
            currentRegister++;
            turnCards(currentRegister);
        } else { //if it is already the 5th register the next phase is called
            if (rebootedPlayers != null) {
                activePlayers.addAll(rebootedPlayers);
                rebootedPlayers.clear();
            }
            game.nextPhase();
        }
    }

    public void removeCurrentCards(int playerID) {
        logger.info("removeCC reached");
        RegisterCard temp = null;
        for (RegisterCard rc : currentCards) {
            logger.info(rc.getPlayerID());
            logger.info(rc.getCard());
            if (rc.getPlayerID() == playerID) {
                logger.info("if reached");
                temp = rc;
                logger.info(rc.getPlayerID() + " und" + rc.getCard());
            }
        }
        currentCards.remove(temp);
        Player player = game.getPlayerFromID(playerID);
        activePlayers.remove(player);
        rebootedPlayers.remove(player);
        for (RegisterCard rc : currentCards) {
            logger.info(rc.getPlayerID());
            logger.info(rc.getCard());
        }
        if (currentCards.isEmpty() && currentRegister < 5) {
            endOfRound();
        } else {
            server.communicateAll(new CurrentPlayer((currentCards.get(0)).getPlayerID()));
        }
    }

    /**
     * Method that activates the board elements in their right order.
     */
    public void activateBoard() {
        activationElements.activateBlueBelts();
        activationElements.activateGreenBelts();
        activationElements.activatePushPanel();
        activationElements.activateGear();
        server.communicateAll(new PlayerShooting());
        laserAction.activateBoardLaser(activePlayers);
        laserAction.activateRobotLaser(activePlayers);
        activationElements.activateEnergySpace();
        activationElements.activateControlPoint();
        // TODO after all robots were moved/affected by the board: check if two robots are on the same tile and handle pushing action
    }

    public void handleMove(Player player, Orientation o) {
        //calculate potential new position

        Coordinate newPosition = activationElements.calculateNew(player, o);

        //Handle board elements
        boolean canMove = true;

        //look for a blocking wall on current Tile
        if (map.isWallBlocking(player.getRobot().getCoordinate(), o)) {
            canMove = false;
        }

        //look for a blocking wall on new tile
        if (!newPosition.isOutsideMap()) {
            if (map.isWallBlocking(newPosition, o.getOpposite())) {
                canMove = false;
            }
        }

        //Handle collisions
        for (Player collisionPlayer : players) {
            if (newPosition.equals(collisionPlayer.getRobot().getCoordinate())) {
                Coordinate old = collisionPlayer.getRobot().getCoordinate().clone();
                handleMove(collisionPlayer, o);
                if ((old.equals(collisionPlayer.getRobot().getCoordinate()))) {
                    canMove = false;
                }
            }
        }
        //move robot, activate board element if given
        if (canMove) moveOne(player, o);
    }

    public void moveOne(Player player, Orientation orientation) {
        player.getRobot().move(orientation);
        handleTile(player);
    }

    public void handleCard(CardType cardType, Player player) {
        Robot robot = player.getRobot();
        Orientation orientation = robot.getOrientation();

        switch (cardType) {
            case MoveI -> handleMove(player, orientation);
            case MoveII -> {
                handleMove(player, orientation);
                if (!isRebooting(player)) handleMove(player, orientation);
            }
            case MoveIII -> {
                handleMove(player, orientation);
                if (!isRebooting(player)) handleMove(player, orientation);
                if (!isRebooting(player)) handleMove(player, orientation);
            }
            case TurnLeft -> robot.rotate(Rotation.LEFT);

            case TurnRight -> robot.rotate(Rotation.RIGHT);

            case UTurn -> robot.rotateTo(orientation.getOpposite());

            case BackUp -> handleMove(player, orientation.getOpposite());

            case PowerUp -> {
                player.setEnergyCubes(player.getEnergyCubes() + 1);
                server.communicateAll(new Energy(player.getID(), 1));
            }
            case Again -> handleRecursion(player, orientation);

            case Spam -> {
                //Add spam card back into the spam deck
                spamDeck.addCard(new Spam());
                //remove top card from programming deck
                Card topCard = player.getDrawProgrammingDeck().pop();

                //Play the top-card
                handleCard(topCard.getName(), player);
            }
            case Worm -> {
                //Reboot the robot.
                new RebootAction().doAction(player);
                //Add worm card back into the worm deck
                wormDeck.getDeck().add(new Worm());
            }
            case Virus -> {
                int robotX = robot.getCoordinate().getX();
                int robotY = robot.getCoordinate().getY();

                for (Player otherPlayer : players) {
                    int otherRobotX = otherPlayer.getRobot().getCoordinate().getX();
                    int otherRobotY = otherPlayer.getRobot().getCoordinate().getY();

                    if (otherPlayer != player && (otherRobotX <= robotX + 6 || otherRobotY <= robotY + 6)) {
                        drawDamage(virusDeck, otherPlayer, 1);
                    }
                }
                virusDeck.addCard(new Virus());
                //remove top card from programming deck
                Card topCard = player.getDrawProgrammingDeck().pop();
                //Play the top-card
                handleCard(topCard.getName(), player);
            }
            case Trojan -> {
                trojanDeck.addCard(new Trojan());
                //Draw two spam cards
                drawDamage(spamDeck, player, 2);
                Card topCard = player.getDrawProgrammingDeck().pop();
                //Play the top-card
                handleCard(topCard.getName(), player);
            }
            default -> logger.error("The CardType " + cardType + " is invalid or not yet implemented!");
        }
    }

    public void handleRecursion(Player player, Orientation orientation) {
        if (currentRegister == 1)
            player.message(new Error("No Previous Movement Recorded"));
        else if (currentRegister == 2 && player.getLastRegisterCard() == CardType.Again)
            player.message(new Error("I am an Idiot."));
        else {
            if (player.getLastRegisterCard() == CardType.Again) {
                int currentRegister = getCurrentRegister();
                Card card = player.getRegisterCard(currentRegister - 2);
                handleCard(card.getName(), player);
            } else {
                new AgainAction().doAction(player);
            }
        }
    }

    public void handleTile(Player player) {
        Coordinate robotPos = player.getRobot().getCoordinate();
        if (robotPos.isOutsideMap() || map.getAttributeOn(AttributeType.Pit, robotPos) != null) {
            rebootedPlayers.add(player);
            activePlayers.remove(player);
            new RebootAction().doAction(player);
        }
    }

    /**
     * calculates the distance between antenna and robot on the map
     * and returns the distance by the number of tiles between them
     *
     * @param antenna game antenna
     * @param robot   the robot for whom priority is calculated
     * @return the tiles between antenna and robot
     */
    public double calculateDistance(Coordinate antenna, Coordinate robot) {
        Coordinate antennaRobotDifference = antenna.subtract(robot);
        return abs(antennaRobotDifference.getX()) + abs(antennaRobotDifference.getY());
    }

    /**
     * calculates the priority and returns a list of players in the order of priority
     *
     * @param antenna game antenna
     * @return A List of players in priority order
     */
    public ArrayList<Player> calculatePriority(Coordinate antenna) {
        ArrayList<RobotDistance> sortedDistance = sortDistance(antenna);
        //logger.info("calculatePrio HIER - " + sortedDistance.toString());

        ArrayList<Player> playerPriority = new ArrayList<>();

        int sortedDistanceSize = sortedDistance.size();

        for (int i = 0; i < sortedDistanceSize; i++) {
            if (sortedDistance.size() == 0) {
                return playerPriority;
            } else if (sortedDistance.size() == 1) {
                //logger.info("calculatePrio 1.if - HIER - " + sortedDistance.toString());
                playerPriority.add(sortedDistance.get(0).getPlayer());
                //logger.info("hier1");
                sortedDistance.remove(0);

                //objects have the same distance values -> selection by clockwise antenna beam
            } else if (sortedDistance.get(0).getDistance() == sortedDistance.get(1).getDistance()) {
                //logger.info("hier");
                //add the robots with same distance into a list
                ArrayList<RobotDistance> sameDistance = new ArrayList<>();

                RobotDistance firstSameDistance = sortedDistance.get(0);

                int tempSortedDistanceSize = sortedDistance.size();
                //logger.info("00: " +tempSortedDistanceSize);

                //compare first element with same distance with all following elements and add matching ones to list sameDistance
                for (int k = 0; k < tempSortedDistanceSize; k++) {
                    //logger.info("0for: " +sortedDistance.size());
                    if (firstSameDistance.getDistance() == sortedDistance.get(0).getDistance()) {
                        sameDistance.add(sortedDistance.get(0));
                        //logger.info("1for: " + sameDistance);
                        sortedDistance.remove(sortedDistance.get(0));
                        //logger.info("2for: " + sortedDistance);
                    }
                }
                //sort sameDistance by yCoordinate -> smallest y coordinate first
                sameDistance.sort(Comparator.comparingInt(RobotDistance::getYCoordinate));
                //logger.info("3- sameDistance.sort: " + sameDistance);

                ArrayList<Player> greaterThanAntenna = new ArrayList<>();
                ArrayList<Player> smallerThanAntenna = new ArrayList<>();

                for (RobotDistance rd : sameDistance) {
                    int antennaY = antenna.getY();
                    int robotY = rd.getRobot().getCoordinate().getY();

                    if (robotY < antennaY) {
                        smallerThanAntenna.add(rd.getPlayer());
                        //logger.info("5- smaller: " +smallerThanAntenna);
                    } else if (robotY > antennaY) {
                        greaterThanAntenna.add(rd.getPlayer());
                        //logger.info("6- greater: " +greaterThanAntenna);
                    } else {
                        playerPriority.add(rd.getPlayer());
                        //logger.info("7- equal: " +playerPriority);
                    }
                }
                playerPriority.addAll(greaterThanAntenna);
                playerPriority.addAll(smallerThanAntenna);
                //logger.info("8- end of else if :" +playerPriority);

                //first and second object have different distance values -> first player in list is currentPlayer
            } else {
                playerPriority.add(sortedDistance.get(0).getPlayer());
                //logger.info("calculatePrio - ELSE:" + playerPriority);
                sortedDistance.remove(0);
                //logger.info("calculatePrio 3.else - HIER - " + sortedDistance.toString());
            }
        }

        //logger.info("calculatePrio -RETURN: " + playerPriority);
        return playerPriority;
    }

    public ArrayList<RobotDistance> sortDistance(Coordinate antenna) {
        //List containing information for determining the next player in line (next robot with priority)
        ArrayList<RobotDistance> sortedDistance = new ArrayList<>();

        //Fill List sortedDistance with matching objects
        int i = 0;
        ArrayList<Player> players = activePlayers;
        while (i < players.size()) {
            //logger.info("sortDistance HIER");
            //logger.info(activePlayers);

            //Point is generated with robot x and y position
            Coordinate robotPosition = new Coordinate(
                    players.get(i).getRobot().getCoordinate().getX(),
                    players.get(i).getRobot().getCoordinate().getY());
            //get playerID
            Player player = players.get(i);
            //get robot
            Robot robot = players.get(i).getRobot();
            //get distance to antenna
            double distance = calculateDistance(antenna, robotPosition);
            //get y coordinate
            int yRobot = robot.getCoordinate().getY();
            //safe object in sortedDistance
            sortedDistance.add(new RobotDistance(player, robot, distance, yRobot));

            i++;
        }
        // sort RobotDistance by distance
        sortedDistance.sort(Comparator.comparingDouble(RobotDistance::getDistance));

        return sortedDistance;
    }

    public ArrayList<Player> getActivePlayers() {
        return activePlayers;
    }

    public int getCurrentRegister() {
        return currentRegister;
    }

    public ArrayList<Player> getRebootedPlayers() {
        return rebootedPlayers;
    }

    public void drawDamage(DamageCardDeck damageDeck, Player player, int amount) {
        logger.info("drawDamage reached");
        cardTypes.clear();
        if (!(spamDeck.size() < amount)) {
            ArrayList<Card> damageCards = damageDeck.drawCards(amount);
            player.getDiscardedProgrammingDeck().getDeck().addAll(damageCards);
            for (Card card : damageCards) {
                cardTypes.add(card.getName());
            }
            server.communicateAll(new DrawDamage(player.getID(), cardTypes));
        } else {
            if (damageDeck.size() == 0) {
                server.communicateDirect(new PickDamage(amount), player.getID());
            } else {
                int alreadyDrawn = damageDeck.size();
                ArrayList<Card> damageCards = damageDeck.drawCards(alreadyDrawn);
                for (Card card : damageCards) {
                    cardTypes.add(card.getName());
                }
                player.getDiscardedProgrammingDeck().drawCards(alreadyDrawn);
                server.communicateDirect(new PickDamage(amount - (damageDeck.size())), player.getID());
            }
        }
    }

    public void handleSelectedDamage(SelectDamage selectDamage, User user) {
        logger.info("handleSelectedDamage");
        Player player = game.userToPlayer(user);
        ArrayList<CardType> selectedCards = selectDamage.getCards();
        for (CardType cardType : selectedCards) {
            switch (cardType) {
                case Spam -> {
                    player.getDiscardedProgrammingDeck().addCard(new Spam());
                    spamDeck.pop();
                }
                case Virus -> {
                    player.getDiscardedProgrammingDeck().addCard(new Virus());
                    virusDeck.pop();
                }
                case Worm -> {
                    player.getDiscardedProgrammingDeck().addCard(new Worm());
                    wormDeck.pop();
                }
                case Trojan -> {
                    player.getDiscardedProgrammingDeck().addCard(new Trojan());
                    trojanDeck.pop();
                }
                default -> server.communicateAll(new Error("This is not a valid damage card"));
            }
            cardTypes.add(cardType);
        }
        server.communicateAll(new DrawDamage(user.getID(), selectedCards));
        logger.info("playerDiscard: " + player.getDiscardedProgrammingDeck().getDeck());
    }

    public boolean isRebooting(Player player) {
        boolean isRebooting = false;
        for (Player p : rebootedPlayers) {
            if (player == p) {
                isRebooting = true;
                break;
            }
        }
        return isRebooting;
    }

    public ArrayList<Player> getPriorityList() {
        return priorityList;
    }

    /**
     * Class to handle the players robots by y-coordinate and distance from antenna
     */
    public static class RobotDistance {
        private final Player player;
        private final Robot robot;
        private final double distance;
        private final int yCoordinate;

        public RobotDistance(Player player, Robot robot, double distance, int yCoordinate) {
            this.player = player;
            this.robot = robot;
            this.distance = distance;
            this.yCoordinate = yCoordinate;
        }

        public Player getPlayer() {
            return player;
        }

        public Robot getRobot() {
            return robot;
        }

        public double getDistance() {
            return distance;
        }

        public int getYCoordinate() {
            return yCoordinate;
        }

        //TODO remove after testing
        @Override
        public String toString() {
            return "RobotDistance{" +
                    "player=" + player +
                    ", robot=" + robot +
                    ", distance=" + distance +
                    ", yCoordinate=" + yCoordinate +
                    '}';
        }
    }
}