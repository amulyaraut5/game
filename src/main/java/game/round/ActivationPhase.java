package game.round;

import game.Player;
import game.gameActions.*;
import game.gameObjects.cards.Card;
import game.gameObjects.cards.damage.Spam;
import game.gameObjects.cards.damage.Trojan;
import game.gameObjects.cards.damage.Worm;
import game.gameObjects.maps.Map;
import game.gameObjects.robot.Robot;
import game.gameObjects.tiles.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.Coordinate;
import utilities.JSONProtocol.body.*;
import utilities.JSONProtocol.body.Error;
import utilities.RegisterCard;
import utilities.enums.AttributeType;
import utilities.enums.CardType;
import utilities.enums.Orientation;
import utilities.enums.Rotation;

import javax.crypto.Cipher;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static java.lang.StrictMath.abs;

/**
 * The Activation Phase is the third phase in the Round.
 * In this class the Programming Cards and GameBoard Tiles are activated.
 *
 * @author janau, sarah
 */

public class ActivationPhase extends Phase {

    private static final Logger logger = LogManager.getLogger();

    // TODO when we transfer StartBoard: private ArrayList<Player> priorityList;

    /**
     * saves the Player ID and the card for the current register
     */
    private ArrayList<RegisterCard> currentCards = new ArrayList<>();

    private Map gameMap;

    //Saves current Register number(for push panels and energy fields)
    private int currentRegister;

    private ActivationElements activationElements;

    /**
     * starts the ActivationPhase.
     * After each register the method for activating the board tiles ist called.
     * TODO In every register the priority is determined and the players cards get activated
     * * in priority order.
     */

    public ActivationPhase() {
        super();
        for (int register = 1; register < 6; register++) {
            turnCards(register);
            //Because whenever a players card was activated he is removed from the current Player list the board
            //is only activated after each player took their turn
            if (currentCards.size() == 0) {
                activateBoard();
                //throw new UnsupportedOperationException();
            }
        }
        //game.nextPhase(); TODO set next Phase somewehere else
    }


    /**
     * At the beginning of each register the current cards are shown.
     */

    private void turnCards(int register) {
        for (Player player : playerList) { //TODO in order of priority List
            RegisterCard playerRegisterCard = new RegisterCard(player.getID(), player.getRegisterCard(register));
            currentCards.add(playerRegisterCard);
        }
        server.communicateAll(new CurrentCards(currentCards)); //TODO CurrentCards is sent multiple times
    }

    /**
     * This method is called whenever a PlayIt() protocol is received.
     * it is checked if its the given players turn. If yes, the cards is handled.
     */

    public void activateCards(int playerID) {
        //Because current Cards ist in priority order the first person to activate their cards is at index 0.
        //So by removing the index 0 after every players turn the current player is always at index 0.
        RegisterCard playerRegisterCard = currentCards.get(0);
        if (playerRegisterCard.getPlayerID() == playerID) {
            CardType currentCard = playerRegisterCard.getCard();
            //currentCard.handleCard(game, game.getPlayerFromID(playerID));
            currentCards.remove(0);
        } else {
            server.communicateDirect(new Error("It's not your turn!"), playerID);
        }
    }

    /**
     * Method that activates the board elements in their right order.
     */
    private void activateBoard() {
        gameMap = game.getMap();

        activationElements.activateBlueBelts();
        activationElements.activateGreenBelts();
        //all board elements functionality are handled in activation elements class except laser

        // TODO after all robots were moved/affected by the board: check if two robots are on the same tile and handle pushing action
    }

    //Supposed o handle a robot moving one tile.
    //TODO Once the game can be started, it needs to check wheiher the robots really move in the right direction
    public void handleMove(Player player, Orientation o) {
        //calculate potential new position
        Coordinate newPosition = null;
        if (o == Orientation.UP) {
            newPosition = player.getRobot().getCoordinate().clone();
            newPosition.addToY(-1);
        }
        if (o == Orientation.RIGHT) {
            newPosition = player.getRobot().getCoordinate().clone();
            newPosition.addToX(1);
        }
        if (o == Orientation.DOWN) {
            newPosition = player.getRobot().getCoordinate().clone();
            newPosition.addToY(1);
        }
        if (o == Orientation.LEFT) {
            newPosition = player.getRobot().getCoordinate().clone();
            newPosition.addToX(-1);
        }
        //Handle board elements
        boolean canMove = true;

        //look for a blocking wall on current Tile
        for (Attribute a : gameMap.getTile(player.getRobot().getCoordinate()).getAttributes()) {
            switch (a.getType()) {
                //handle different tile effects here
                case Wall -> {
                    Wall temp = (Wall) a;
                    for (Orientation orientation : temp.getOrientations()) {
                        if (orientation == o) canMove = false;
                    }
                }
            }
        }

        //look for a blocking wall on new tile
        for (Attribute a : gameMap.getTile(newPosition).getAttributes()) {
            switch (a.getType()) {
                //handle different tile effects here
                case Wall -> {
                    Wall temp = (Wall) a;
                    for (Orientation orientation : temp.getOrientations()) {
                        if (orientation == o.getOpposite()) canMove = false;
                    }
                }
            }
        }

        //Handle collisions
        for (Player currentPlayer : playerList) {
            if (newPosition.equals(currentPlayer.getRobot().getCoordinate())) {
                Coordinate old = currentPlayer.getRobot().getCoordinate();
                handleMove(currentPlayer, o);
                if ((old.equals(currentPlayer.getRobot().getCoordinate()))) {
                    canMove = false;
                }
            }
        }

        //move robot, activate board element if given
        if (canMove) {
            moveOne(player, o);
            handleTile(player);
        }


    }

    public void moveOne(Player player, Orientation orientation) {
        player.getRobot().move(1, orientation);
        //server.communicateAll(new Movement(player.getID(),player.getRobot().getCoordinate().toPosition()));
    }

    public void communicateBeltMovement(Player player){
        server.communicateAll(new Movement(player.getID(),player.getRobot().getCoordinate().toPosition()));
    }


    public void handleCard(CardType cardType, Player player) {
        Orientation orientation = player.getRobot().getOrientation();

        switch (cardType) {
            case MoveI -> {
                moveOne(player, orientation);
                logger.info(player.getName() + "moved one Tile.");
            }
            case MoveII -> {
                moveOne(player, orientation);
                moveOne(player, orientation);
                logger.info(player.getName() + "moved two Tiles.");
            }
            case MoveIII -> {
                moveOne(player, orientation);
                moveOne(player, orientation);
                moveOne(player, orientation);
                logger.info(player.getName() + "moved three Tiles.");
            }
            case  TurnLeft -> {
                RotateRobot rotateRobot = new RotateRobot((Orientation.LEFT));
                rotateRobot.doAction(Orientation.LEFT, player);
                server.communicateAll(new Movement(player.getID(),player.getRobot().getCoordinate().toPosition()));
                logger.info(player.getName() + "turned left.");
            }
            case TurnRight -> {
                RotateRobot rotateRobot = new RotateRobot((Orientation.RIGHT));
                rotateRobot.doAction(Orientation.RIGHT, player);
                server.communicateAll(new Movement(player.getID(),player.getRobot().getCoordinate().toPosition()));
                logger.info(player.getName() + "turned right.");
            }
            case UTurn -> {
                RotateRobot rotateRobot = new RotateRobot((Orientation.RIGHT));
                rotateRobot.doAction(Orientation.RIGHT, player);
                rotateRobot.doAction(Orientation.RIGHT, player);
                server.communicateAll(new Movement(player.getID(),player.getRobot().getCoordinate().toPosition()));
                logger.info(player.getName() + "performed U-Turn");
            }
            case BackUp -> {
                new MoveRobotBack().doAction(orientation, player);
                server.communicateAll(new Movement(player.getID(),player.getRobot().getCoordinate().toPosition()));
                logger.info(player.getName() + "moved back.");
            }
            case PowerUp -> {
                player.setEnergyCubes(player.getEnergyCubes() + 1);
                logger.info(player.getName() + "got one EnergyCube.");
            }
            case Again -> {
                new AgainAction().doAction(orientation, player);
                server.communicateAll(new Movement(player.getID(),player.getRobot().getCoordinate().toPosition()));
            }
            case Spam -> {
                Spam spam = new Spam();
                //Add spam card back into the spam deck
                game.getSpamDeck().getDeck().add(spam);
                //remove top card from programming deck
                Card topCard = player.getDrawProgrammingDeck().pop();

                //exchange spam card and new programming card in the current register
                int spamIndex = player.getRegisterCards().indexOf(spam);
                player.getRegisterCards().remove(spam);
                player.getRegisterCards().set(spamIndex, topCard);

                logger.info(player.getName() + "played a spam card.");
                //Play the new register card
                handleCard(topCard.getName(), player);
            }
            case Worm -> {
                Worm worm = new Worm();
                //Reboot the robot.
                new RebootAction().doAction(orientation, player);
                //Add worm card back into the worm deck
                game.getWormDeck().getDeck().add(worm);
                logger.info(player.getName() + "played a worm card.");
            }
            case Virus -> {
                int robotX = player.getRobot().getCoordinate().getX();
                int robotY = player.getRobot().getCoordinate().getY();
                ArrayList<Player> allPlayers = game.getPlayers();

                for (Player otherPlayer : allPlayers) {
                    int otherRobotX = otherPlayer.getRobot().getCoordinate().getX();
                    int otherRobotY = otherPlayer.getRobot().getCoordinate().getY();

                    if (otherPlayer != player && (otherRobotX <= robotX + 6 || otherRobotY <= robotY + 6)) {
                        Card virusCard = game.getVirusDeck().pop();
                        otherPlayer.getDiscardedProgrammingDeck().getDeck().add(virusCard);
                    }
                }
                logger.info(player.getName() + "played a virus card.");
            }
            case Trojan -> {
                Trojan trojan = new Trojan();
                game.getTrojanHorseDeck().getDeck().add(trojan);
                Card topCard = player.getDrawProgrammingDeck().pop();

                //exchange spam card and new programming card in the current register
                int trojanIndex = player.getRegisterCards().indexOf(trojan);
                player.getRegisterCards().remove(trojan);
                player.getRegisterCards().set(trojanIndex, topCard);

                //Draw two spam cards
                for (int i = 0; i < 2; i++) {
                    Card spamCard = game.getSpamDeck().pop();
                    player.getDiscardedProgrammingDeck().getDeck().add(spamCard);
                }
                logger.info(player.getName() + "played a trojan card.");
                //Play the new register card
                handleCard(topCard.getName(), player);
            }
            default -> logger.error("The CardType " + cardType + " is invalid or not yet implemented!");
        }
    }

    public void handleTile(Player player){
        for (Attribute a : gameMap.getTile(player.getRobot().getCoordinate()).getAttributes()) {
            switch (a.getType()){
                case Gear :
                    if(((Gear) a).getOrientation() == Rotation.RIGHT){
                        new RotateRobot(Orientation.RIGHT).doAction(Orientation.RIGHT, player);
                    }
                    else{
                        new RotateRobot(Orientation.LEFT).doAction(Orientation.LEFT, player);
                    }
                case Pit:
                    new RebootAction().doAction(Orientation.LEFT, player);

                case ControlPoint:
                    player.checkPointReached();
                    server.communicateAll(new CheckpointReached(player.getID(), player.getCheckPointCounter()));

                default:
                    server.communicateAll(new Movement(player.getID(),player.getRobot().getCoordinate().toPosition()));
            }
        }
    }


    /**
     * Class to handle the players robots by distance from antenna
     */
    public class RobotDistance {
        private int playerID;
        private Robot robot;
        private double distance;

        //playerID and calculated distance
        public RobotDistance(int playerID, Robot robot, double distance) {
            this.playerID = playerID;
            this.robot = robot;
            this.distance = distance;
        }
        public int getPlayerID() { return playerID; }
        public Robot getRobot() {
            return robot;
        }
        public double getDistance() { return distance; }
    }

    /**
     * calculates the distance between antenna and robot on the map
     * and returns the distance by the number of tiles between them
     *
     * @param antenna
     * @param robot
     * @return the tiles between antenna and robot
     */
    public double calculateDistance (Coordinate antenna, Coordinate robot) {
        Coordinate antennaRobotDifference = antenna.subtract(robot);
        double tileDistance = abs(antennaRobotDifference.getX()) + abs(antennaRobotDifference.getY());
        return tileDistance;
    }

    /**
     * calculates the priority and returns the playerID of the player whose turn it is
     * @param antenna
     * @return
     */
    public ArrayList<Integer> calculatePriority(Coordinate antenna) {
        //List containing information for determining the next player in line (next robot with priority)
        ArrayList<RobotDistance> sortedDistance = new ArrayList<>();

        //Fill List sortedDistance with matching objects
        if (sortedDistance.size() == 0) {
            int i = 0;
            ArrayList<Player> players = game.getPlayers();
            while (i < players.size()) {
                //Point is generated with robot x and y position
                Coordinate robotPosition = new Coordinate(
                        players.get(i).getRobot().getCoordinate().getX(),
                        players.get(i).getRobot().getCoordinate().getY());
                //get playerID
                int playerID = players.get(i).getID();
                //get robot
                Robot robot = players.get(i).getRobot();
                //get distance to antenna
                double distance = calculateDistance(antenna, robotPosition);
                //safe object in sortedDistance
                sortedDistance.add(new RobotDistance(playerID, robot, distance));

                i++;
            }
        }

        // sort RobotDistance by distance
        Collections.sort(sortedDistance, Comparator.comparingDouble(RobotDistance::getDistance));


        //first object in list sortedDistance
        int firstPlayerID = sortedDistance.get(0).getPlayerID();
        double firstRobotDistance = sortedDistance.get(0).getDistance();

        ArrayList<Integer> playerPriority = new ArrayList<>();

        //first and second object have different distance values -> first player in list is currentPlayer
        for (RobotDistance robotDistance : sortedDistance) {
            for(int j = 1; j <= sortedDistance.size(); j++) {
                if (robotDistance.getDistance() != sortedDistance.get(j).getDistance()) {
                    playerPriority.add(firstPlayerID);

                //objects have the same distance values -> selection by clockwise antenna beam
                } else {
                    //add the robots with same distance in one list
                    ArrayList<RobotDistance> sameDistance = new ArrayList<>();
                    RobotDistance firstSameDistance = robotDistance;
                    sameDistance.add(firstSameDistance);

                    //add y coordinates of sameDistance robots in one list
                    ArrayList<Integer> yCoordinates = new ArrayList<>();

                    for (RobotDistance rd : sameDistance){
                        int yRobot = rd.getRobot().getCoordinate().getY();
                        yCoordinates.add(yRobot);
                    }
                    //sort the y coordinates of robots
                    Collections.sort(yCoordinates);

                    for(int k = 1; k <= sortedDistance.size(); k++) {
                        if(firstSameDistance.getDistance() == sortedDistance.get(k).getDistance()){
                        sameDistance.add(sortedDistance.get(k));
                        }
                        for(RobotDistance rd : sameDistance){
                            if(rd.getRobot().getCoordinate().getY() == antenna.getY()){
                                playerPriority.add(rd.getPlayerID());
                            }else if(rd.getRobot().getCoordinate().getY() > antenna.getY()){



                            }
                        }


                    }

                        //TODO selection by clockwise antenna beam
                        playerPriority.add(firstPlayerID);
                        sortedDistance.remove(0);
                        return playerPriority;
                    }
                }
            }
        return playerPriority;
    }
}