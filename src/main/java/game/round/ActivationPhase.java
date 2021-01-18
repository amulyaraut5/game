package game.round;

import game.Player;
import game.gameObjects.maps.Map;
import game.gameObjects.tiles.*;
import javafx.geometry.Point2D;
import utilities.Coordinate;
import utilities.JSONProtocol.body.CurrentCards;
import utilities.JSONProtocol.body.Error;
import utilities.MapConverter;
import utilities.RegisterCard;
import utilities.enums.AttributeType;
import utilities.enums.CardType;
import utilities.enums.Orientation;

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

    // TODO when we transfer StartBoard: private ArrayList<Player> priorityList;

    private Antenna antenna = new Antenna();


    /**
     * saves the Player ID and the card for the current register
     */
    private ArrayList<RegisterCard> currentCards = new ArrayList<>();

    private Map gameMap;

    //Saves current Register number(for push panels and energy fields)
    private int currentRegister;

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
        game.nextPhase();
    }


    /**
     * At the beginning of each register the current cards are shown.
     */

    private void turnCards(int register) {
        for (Player player : playerList) { //TODO in order of priority List
            RegisterCard playerRegisterCard = new RegisterCard(player.getID(), player.getRegisterCard(register));
            currentCards.add(playerRegisterCard);
        }
        server.communicateAll(new CurrentCards(currentCards));
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
                        if (a.getType() == AttributeType.Belt) {
                            handleMove(currentPlayer, ((Belt) a).getOrientation());
                        }
                        if (a.getType() == AttributeType.RotatingBelt) {
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
                            if (a.getType() == AttributeType.Belt) {
                                handleMove(currentPlayer, ((Belt) a).getOrientation());

                            }
                            if (a.getType() == AttributeType.RotatingBelt) {
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
        private double distance;

        //playerID and calculated distance
        public RobotDistance(int playerID, double distance) {
            this.playerID = playerID;
            this.distance = distance;
        }

        public int getPlayerID() {
            return playerID;
        }

        public double getDistance() {
            return distance;
        }
    }


    /**
     * calculates the distance between antenna and robot on the map
     * and returns the distance by the number of tiles between them
     *
     * @param antenna
     * @param robot
     * @return the tiles between antenna and robot
     */
    public double calculateDistance(Point2D antenna, Point2D robot) {
        Point2D antennaRobotDifference = antenna.subtract(robot);
        double tileDistance = abs(antennaRobotDifference.getX()) + abs(antennaRobotDifference.getY());
        return tileDistance;
    }


    /**
     * calculates the priority and returns the playerID of the player whose turn it is
     *
     * @param antenna
     * @return
     */
    public int calculatePriority(Point2D antenna) {
        //List containing information for determining the next player in line (next robot with priority)
        ArrayList<RobotDistance> nextPriority = new ArrayList<>();

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
                int playerID = players.get(i).getID();
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
            for (int j = 0; j < nextPriority.size(); j++) {
                ArrayList<RobotDistance> sameDistance = new ArrayList<>();
                if (firstRobotDistance == nextPriority.get(j).getDistance()) {
                    sameDistance.add(nextPriority.get(j));
                }
            }
            //TODO selection by clockwise antenna beam
            nextPriority.remove(0);
            return firstPlayerID;
        }
    }
}