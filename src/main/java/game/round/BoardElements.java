package game.round;

import game.Game;
import game.Player;
import game.gameObjects.maps.Map;
import game.gameObjects.robot.Robot;
import game.gameObjects.tiles.*;
import server.Server;
import utilities.Coordinate;
import utilities.JSONProtocol.JSONBody;
import utilities.JSONProtocol.body.Energy;
import utilities.enums.AttributeType;
import utilities.enums.Orientation;
import utilities.enums.Rotation;

import java.util.ArrayList;

/**
 * This class contains the all the board elements and their functionality.
 * The execution of board elements are handled in activation phase.
 *
 * @author Amulya
 * @author Louis
 */
public class BoardElements {
    private final Game game = Game.getInstance();
    private final ArrayList<Player> playerList = game.getPlayers();
    private final Map map = game.getMap();
    private final ActivationPhase activationPhase;
    private final Server server = Server.getInstance();

    public BoardElements(ActivationPhase activationPhase) {
        this.activationPhase = activationPhase;
    }

    /**
     * Gears rotate robots resting on them 90 degrees in the direction of the arrows.
     * Red gears rotate left, and green gears rotate right.
     * PlayerTurning Protocol is sent to all players.
     */
    public void activateGear() {
        for (Coordinate coordinate : map.readGearCoordinate()) {
            for (Player player : playerList) {
                if (player.getRobot().getCoordinate().equals(coordinate)) {
                    for (Attribute a : map.getTile(coordinate.getX(), coordinate.getY()).getAttributes()) {
                        Rotation rotation = ((Gear) a).getOrientation();
                        player.getRobot().rotate(rotation);
                    }
                }
            }
        }
    }

    /**
     * Push panels push any robots resting on them into the next space in the direction the push
     * panel faces. They activate only in the register that corresponds to the number on them. For
     * example, if the robot end register two on a push panel labeled ???2, 4??? you will be pushed. If the robot end
     * register three on the same push panel, you won???t be pushed.
     * Movement Protocol is sent to the players.
     */

    public void activatePushPanel() {
        for (Coordinate coordinate : map.readPushPanelCoordinate()) {
            Tile tile = map.getTile(coordinate);
            for (Player player : playerList) {
                if (player.getRobot().getCoordinate().equals(coordinate)) {
                    for (Attribute a : tile.getAttributes()) {
                        for (int i : ((PushPanel) a).getRegisters()) {
                            if (i == activationPhase.getCurrentRegister()) {
                                activationPhase.handleMove(player, ((PushPanel) a).getOrientation());
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Whenever the player finds in this tile, player gets the energy token.
     * EnergyProtocol is sent to the player.
     */

    public void activateEnergySpace() {
        for (Coordinate coordinate : map.readEnergySpaceCoordinates()) {
            for (Player player : playerList) {
                if (player.getRobot().getCoordinate().equals(coordinate)) {
                    int energy = player.getEnergyCubes();
                    energy += energy;
                    player.setEnergyCubes(energy);
                    JSONBody jsonBody = new Energy(player.getID(), 1);
                    server.communicateAll(jsonBody);
                }
            }
        }
    }

    /**
     * When a player is on a blue belt at the end of a register, he gets moved 2 tiles in direction of the belt
     */

    public void activateBlueBelts() {
        ArrayList<Player> playersOnBelt = new ArrayList<>();
        ArrayList<Boolean> actionFinished = new ArrayList<>();
        ArrayList<Orientation> orientations = new ArrayList<>();
        ArrayList<Coordinate> oldPositions = new ArrayList<>();

        for (Coordinate tileCoordinate : map.getBlueBelts()) {
            for (Player currentPlayer : playerList) {
                if (tileCoordinate.equals(currentPlayer.getRobot().getCoordinate())) {
                    playersOnBelt.add(currentPlayer);
                    actionFinished.add(false);
                    oldPositions.add(currentPlayer.getRobot().getCoordinate().clone());

                    for (Attribute a : map.getTile(tileCoordinate).getAttributes()) {
                        if (a instanceof Belt) orientations.add(((Belt) a).getOrientation());
                        if (a instanceof RotatingBelt) orientations.add(((RotatingBelt) a).getOrientations()[0]);
                    }
                }
            }
        }

        for (Player player : playersOnBelt) oldPositions.add(player.getRobot().getCoordinate().clone());

        handleBeltMovement(playersOnBelt, actionFinished, orientations, oldPositions);
        for (int j = 0; j < actionFinished.size(); j++) {
            actionFinished.set(j, false);
        }

        for (Player player : playersOnBelt) {
            boolean stillOnBelt = false;
            for (Attribute a : map.getTile(player.getRobot().getCoordinate()).getAttributes()) {
                if (a.getType() == AttributeType.Belt) stillOnBelt = true;
                else {
                    if (a.getType() == AttributeType.RotatingBelt) stillOnBelt = true;
                }
                if (!stillOnBelt) actionFinished.set(playersOnBelt.indexOf(player), true);
            }
            for (Attribute a : map.getTile(player.getRobot().getCoordinate()).getAttributes()) {
                if (a.getType() == AttributeType.RotatingBelt) {
                    orientations.set(playersOnBelt.indexOf(player), ((RotatingBelt) a).getOrientations()[0]);
                }
            }
        }

        handleBeltMovement(playersOnBelt, actionFinished, orientations, oldPositions);
    }

    /**
     * When a player is on a green belt at the end of a register, he gets moved 1 tile in direction of the belt
     */
    public void activateGreenBelts() {
        ArrayList<Player> playersOnBelt = new ArrayList<>();
        ArrayList<Boolean> actionFinished = new ArrayList<>();
        ArrayList<Orientation> orientations = new ArrayList<>();
        ArrayList<Coordinate> oldPositions = new ArrayList<>();

        for (Coordinate tileCoordinate : map.getGreenBelts()) {
            for (Player currentPlayer : playerList) {
                if (tileCoordinate.equals(currentPlayer.getRobot().getCoordinate())) {
                    playersOnBelt.add(currentPlayer);
                    actionFinished.add(false);
                    oldPositions.add(currentPlayer.getRobot().getCoordinate().clone());

                    for (Attribute a : map.getTile(tileCoordinate).getAttributes()) {
                        if (a.getType() == AttributeType.Belt) {
                            orientations.add(((Belt) a).getOrientation());
                        }
                        if (a instanceof RotatingBelt) {
                            orientations.add(((RotatingBelt) a).getOrientations()[0]);
                        }
                    }
                }
            }
        }

        handleBeltMovement(playersOnBelt, actionFinished, orientations, oldPositions);
    }

    /**
     * Method that gets used to handle belt movement. Includes the functionality to move all players in a certain list into certain directions.
     *
     * @param playersOnBelt  all players that are currently located on a belt.
     * @param actionFinished list that yields information about what players were already moved.
     * @param orientations   Directions of player movements
     * @param oldPositions   actual positions of the players. Later used to check whether player was moved or not
     */
    public void handleBeltMovement(ArrayList<Player> playersOnBelt, ArrayList<Boolean> actionFinished,
                                   ArrayList<Orientation> orientations, ArrayList<Coordinate> oldPositions) {
        boolean finished = false;
        while (!finished) {

            for (Player player : playersOnBelt) {
                if (!actionFinished.get(playersOnBelt.indexOf(player))) {
                    boolean move = true;
                    Coordinate newPos = calculateNew(player, orientations.get(playersOnBelt.indexOf(player)));
                    for (Player collisionPlayer : playerList) {
                        if (collisionPlayer.getRobot().getCoordinate().equals(newPos)) {
                            move = false;
                            if (!playersOnBelt.contains(collisionPlayer)) {
                                actionFinished.set(playersOnBelt.indexOf(player), true);
                            } else {
                                if (actionFinished.get(playersOnBelt.indexOf(collisionPlayer))) {
                                    collisionPlayer.getRobot().moveTo(oldPositions.get(playersOnBelt.indexOf(collisionPlayer)));
                                    actionFinished.set(playersOnBelt.indexOf(player), true);
                                }
                            }
                        }
                    }
                    if (move) {
                        player.getRobot().moveTo(newPos);
                        actionFinished.set(playersOnBelt.indexOf(player), true);
                    }
                }
            }
            if (!actionFinished.contains(false)) finished = true;
        }
        for (Player p : playersOnBelt) {
            if (!p.getRobot().getCoordinate().equals(oldPositions.get(playersOnBelt.indexOf(p)))) {

                //Eventually rotate player if he was moved onto a rotating belt.
                if (!p.getRobot().getCoordinate().isOutsideMap()) {
                    for (Attribute a : map.getTile(p.getRobot().getCoordinate()).getAttributes()) {
                        if (a.getType() == AttributeType.RotatingBelt) {
                            RotatingBelt temp = ((RotatingBelt) a);
                            if (temp.getOrientations()[0] != orientations.get(playersOnBelt.indexOf(p))) {
                                rotateOnBelt(p.getRobot(), temp.getOrientations());
                            }
                        }
                    }
                }
                activationPhase.handleTile(p);
            }
        }
    }

    /**
     * Calculates new coordinate if player moves one tile into a given direction
     *
     * @param player Player that the coordinate should be calculated for
     * @param o      Direction player gets moved to
     */
    public Coordinate calculateNew(Player player, Orientation o) {
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

        return newPosition;
    }

    /**
     * Implements rotating behavior of players when they enter a rotating belt.
     *
     * @param robot robot that gets rotated
     * @param o     Orientations attribute of rotating belt
     */
    public void rotateOnBelt(Robot robot, Orientation[] o) {
        if (o[0].getNext() == o[1]) robot.rotate(Rotation.RIGHT);
        else if (o[0].getPrevious() == o[1]) robot.rotate(Rotation.LEFT);
    }

}