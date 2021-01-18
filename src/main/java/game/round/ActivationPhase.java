package game.round;

import game.Player;
import game.gameObjects.cards.Card;
import game.gameObjects.maps.Map;
import game.gameObjects.robot.Robot;
import game.gameObjects.tiles.Attribute;
import game.gameObjects.tiles.Belt;
import game.gameObjects.tiles.RotatingBelt;
import game.gameObjects.tiles.Wall;
import game.gameObjects.tiles.*;
import utilities.Coordinate;
import utilities.JSONProtocol.body.CurrentCards;
import utilities.MapConverter;
import utilities.Orientation;
import utilities.Utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import static java.lang.StrictMath.abs;

/**
 * The Activation Phase is the third phase in the Round.
 * In this class the Programming Cards and GameBoard Tiles are activated.
 *
 * @author janau, sarah
 */

public class ActivationPhase extends Phase {

    // TODO when we transfer StartBoard: private ArrayList<Player> priorityList;

    private Antenna antenna = new Antenna();


    /**
     * saves the Player ID and the card for the current register
     */
    private HashMap<Integer, Card> currentCards = new HashMap<>();

    private Map gameMap;

    //Saves current Register number(for push panels and energy fields)
    private int currentRegister;

    public ActivationPhase() {
        super();
    }


    /**
     * starts the ActivationPhase.
     * After each register the method for activating the board tiles ist called.
     * TODO In every register the priority is determined and the players cards get activated
     * * in priority order.
     */
    @Override
    public void startPhase() {
        for (int register = 1; register <6; register++) {
            turnCards();
            activateCards();
        }
        activateBoard();
        //throw new UnsupportedOperationException();
    }

    /**
     * At the beginning of each register the current cards are shown.
     */

    private void turnCards () {
        for (int register = 1; register < 6; register++) {
            for (Player player : playerList) { //TODO in order of priority List
                currentCards.put(player.getID(), player.getRegisterCard(register));
            }
            server.communicateAll(new CurrentCards(currentCards));
        }
    }

    /**
     * After the cards of each player for the current register have been shown,
     * this method activates the cards depending on the priority of the player.
     * Each player has to confirm with the PlayIt protocol.
     */

    private void activateCards() {
        for (Integer playerID : currentCards.keySet()) { //if cards are saved in current cards based on priority this works
            //TODO player needs to send PlayIt protocol
            Card currentCard = currentCards.get(playerID);
            Player currentPlayer = game.getPlayerFromID(playerID);
            currentCard.handleCard(game, currentPlayer);
        }
    }

    /**
     * Method that activates the board elements in their right order.
     */


    private void activateBoard() {
        // TODO - implement ActivationPhase.activateBoard
        gameMap = game.getMap();

        // And then we can execute other board elements in order

        activateBlueBelts();
        activateGreenBelts();
		/*
           all board elements functionality are handled in activation elements class except laser
		 */

        // TODO after all robots were moved/affected by the board: check if two robots are on the same tile and handle pushing action
        //throw new UnsupportedOperationException();

    }

    public void activateGreenBelts() {
        for (Coordinate tileCoordinate : gameMap.getGreenBelts()) {
            for (Player currentPlayer : playerList) {
                if (tileCoordinate.equals(currentPlayer.getRobot().getPosition())) {
                    for (Attribute a : gameMap.getTile(tileCoordinate).getAttributes()) {
                        if (a.getType() == Utilities.AttributeType.Belt) {
                            handleMove(currentPlayer, ((Belt) a).getOrientation());
                        }
                        if (a.getType() == Utilities.AttributeType.RotatingBelt) {
                            RotatingBelt temp = (RotatingBelt) a;
                            handleMove(currentPlayer, temp.getOrientations()[1]);
                        }
                    }
                }
            }
        }
    }

    public void activateBlueBelts() {
        for (int i = 0; i < 2; i++) {
            for (Coordinate tileCoordinate : gameMap.getBlueBelts()) {
                for (Player currentPlayer : playerList) {
                    if (tileCoordinate.equals(currentPlayer.getRobot().getPosition())) {
                        for (Attribute a : gameMap.getTile(tileCoordinate).getAttributes()) {
                            if (a.getType() == Utilities.AttributeType.Belt) {
                                handleMove(currentPlayer, ((Belt) a).getOrientation());

                            }
                            if (a.getType() == Utilities.AttributeType.RotatingBelt) {
                                RotatingBelt temp = (RotatingBelt) a;
                                handleMove(currentPlayer, temp.getOrientations()[1]);
                            }
                        }
                    }
                }
            }
        }
    }

    //Supposed o handle a robot moving one tile.
    //TODO Once the game can be started, it needs to check wheiher the robots really move in the right direction
    public void handleMove(Player player, Orientation o) {
        //calculate potential new position
        Coordinate newPosition = null;
        if (o == Orientation.UP) {
            newPosition = player.getRobot().getPosition().clone();
            newPosition.addToY(-1);
        }
        if (o == Orientation.RIGHT) {
            newPosition = player.getRobot().getPosition().clone();
            newPosition.addToX(1);
        }
        if (o == Orientation.DOWN) {
            newPosition = player.getRobot().getPosition().clone();
            newPosition.addToY(1);
        }
        if (o == Orientation.LEFT) {
            newPosition = player.getRobot().getPosition().clone();
            newPosition.addToX(-1);
        }
        //Handle board elements
        boolean canMove = true;
        boolean inPit = false;
        boolean onCheckpoint = false;
        for (Attribute a : gameMap.getTile(newPosition).getAttributes()) {
            switch (a.getType()) {
                //handle different tile effects here
                case Wall:
                    Wall temp = (Wall) a;
                    for (Orientation orientation : temp.getOrientations()) {
                        if (orientation == o.getOpposite()) {
                            canMove = false;
                        }
                    }
                case Pit:
                    inPit = true;

                case ControlPoint:
                    onCheckpoint = true;
            }
        }

        //Handle collisions
        for (Player currentPlayer : playerList) {
            if (newPosition.equals(currentPlayer.getRobot().getPosition())) {
                Coordinate old = currentPlayer.getRobot().getPosition();
                handleMove(currentPlayer, o);
                if ((old.equals(currentPlayer.getRobot().getPosition()))) {
                    canMove = false;
                }
            }
        }

        //move robot, activate board element if given
        if (inPit && canMove) {
            moveOne(player, o);
            player.getRobot().reboot();
        } else {
            if (onCheckpoint && canMove) {
                moveOne(player, o);
                player.checkPointReached();
            } else {
                if (canMove) {
                    moveOne(player, o);
                }
            }
        }
    }

    public void moveOne(Player player, Orientation orientation) {
        player.getRobot().move(1, orientation);
        server.communicateAll(MapConverter.convertCoordinate(player, player.getRobot().getPosition()));
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
                        players.get(i).getRobot().getPosition().getX(),
                        players.get(i).getRobot().getPosition().getY());
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
                        int yRobot = rd.getRobot().getPosition().getY();
                        yCoordinates.add(yRobot);
                    }
                    //sort the y coordinates of robots
                    Collections.sort(yCoordinates);

                    for(int k = 1; k <= sortedDistance.size(); k++) {
                        if(firstSameDistance.getDistance() == sortedDistance.get(k).getDistance()){
                        sameDistance.add(sortedDistance.get(k));
                        }
                        for(RobotDistance rd : sameDistance){
                            if(rd.getRobot().getPosition().getY() == antenna.getY()){
                                playerPriority.add(rd.getPlayerID());
                            }else if(rd.getRobot().getPosition().getY() > antenna.getY()){



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