package game.round;

import game.Player;
import game.gameObjects.cards.Card;
import game.gameObjects.maps.Map;
import game.gameObjects.tiles.*;
import javafx.geometry.Point2D;
import utilities.Coordinate;
import utilities.JSONProtocol.body.CurrentCards;
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
        for (int register = 1; register < 6; register++) {
            for (Player player : playerList) {
                currentCards.put(player.getId(), player.getRegisterCard(register));
            }
            server.communicateAll(new CurrentCards(currentCards));
        }
        //throw new UnsupportedOperationException();
    }

    /**
     * The player needs to confirm that he want to play the card (PlayIt).
     * If he confirms this method needs to be called.
     */

    private void activateCards() {
        for (Integer key : currentCards.keySet()) {
            Card currentCard = currentCards.get(key);
            Player currentPlayer = game.getPlayerFromID(key);
            currentCard.handleCard(game, currentPlayer);
        }
    }

    /**
     * Method that activates the board elements in their right order.
     */


    private void activateBoard(Player player) {
        // TODO - implement ActivationPhase.activateBoard
        gameMap = game.getMap();


        // And then we can execute other board elements in order


		/*
		activateBlueBelts;
		activateGreenBelts;
		pushPanel.performAction();
		gear.performAction();
		boardLaser.performAction();
		robotLaser.performAction(); TODO is there a robot laser?
		energySpace.performAction();
		checkPoint.performAction();
		 */
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
        for (Attribute a : gameMap.getTile(newPosition).getAttributes()) {
            switch (a.getType()) {
                //handle different tile effects here
            }
        }

    }


    /**
     * This class contains a constructor getter and setter to handle robots by distance from antenna
     */
    public class RobotDistance {
        private int playerID;
        private double distance;

        //playerID and calculated distance
        public RobotDistance(int playerID, double distance) {
            this.playerID = playerID;
            this.distance = distance;
        }
        public int getPlayerID() { return playerID; }
        public double getDistance() { return distance; }
    }


    //List containing information for determining the next player in line (next robot with priority)
    ArrayList<RobotDistance> nextPriority = new ArrayList<>();

    /**
     * calculates the distance between antenna and robot on the map
     * and returns the distance by the number of tiles between them
     * @param antenna
     * @param robot
     * @return the tiles between antenna and robot
     */
    public double calculateDistance (Point2D antenna, Point2D robot) {
        Point2D antennaRobotDifference = antenna.subtract(robot);
        double tileDistance = abs(antennaRobotDifference.getX()) + abs(antennaRobotDifference.getY());
        return tileDistance;
    }

    /**
     * calculates the priority of the robots and returns the matching playerID of the player whose turn it is
     * @param antenna
     * @return
     */
    public int calculatePriority(Point2D antenna) {
        //Fill List nextPriority with matching objects
        if (nextPriority.size() == 0) {
            int i = 0;
            ArrayList<Player> players = game.getPlayers();
            while (i < players.size()) {
                //Point is generated with robot x and y position
                Point2D robot = new Point2D(
                        players.get(i).getRobot().getPosition().getX(),
                        players.get(i).getRobot().getPosition().getY());

                //get playerID
                int playerID = players.get(i).getPlayerID();
                //get distance to antenna
                double distance = calculateDistance(antenna, robot);
                //safe object in nextPriority
                nextPriority.add(new RobotDistance(playerID, distance));

                i++;
            }
        }

        // sort RobotDistance by distance
        Collections.sort(nextPriority, Comparator.comparingDouble(RobotDistance::getDistance));


        //first object in list nextPriority
        int firstPlayerID = nextPriority.get(0).getPlayerID();
        double firstRobotDistance = nextPriority.get(0).getDistance();

        //first and second object have different distance values -> first player in list is currentPlayer
        if (firstRobotDistance != nextPriority.get(1).getDistance()) {
            nextPriority.remove(0);
            return firstPlayerID;
        //objects have the same distance values -> selection by clockwise antenna beam
        } else {
            for(int j = 0; j < nextPriority.size(); j++){
                ArrayList<RobotDistance> sameDistance = new ArrayList<>();
                if(firstRobotDistance == nextPriority.get(j).getDistance()){
                    sameDistance.add(nextPriority.get(j));
                }
            }
            //TODO selection by clockwise antenna beam
            nextPriority.remove(0);
            return firstPlayerID;
        }
    }
}