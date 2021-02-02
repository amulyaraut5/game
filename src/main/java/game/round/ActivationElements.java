package game.round;

import game.Game;
import game.Player;
import game.gameObjects.maps.Map;
import game.gameObjects.tiles.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.Server;
import utilities.Coordinate;
import utilities.JSONProtocol.JSONBody;
import utilities.JSONProtocol.body.CheckpointReached;
import utilities.JSONProtocol.body.Energy;
import utilities.JSONProtocol.body.GameWon;
import utilities.JSONProtocol.body.PlayerTurning;
import utilities.enums.AttributeType;
import utilities.enums.Orientation;
import utilities.enums.Rotation;

import java.util.ArrayList;

public class ActivationElements {
    private static final Logger logger = LogManager.getLogger();
    private final Game game = Game.getInstance();
    private final ArrayList<Player> playerList = game.getPlayers();
    private final Map map = game.getMap();
    private final ActivationPhase activationPhase;
    private final Server server = Server.getInstance();

    public ActivationElements(ActivationPhase activationPhase) {
        this.activationPhase = activationPhase;
    }

    /**
     * Gears rotate robots resting on them 90 degrees in the direction of the arrows.
     * Red gears rotate left, and green gears rotate right.
     * PlayerTurning Protocol is sent to all players.
     */
    public void activateGear() {
        for (Coordinate coordinate : map.getGearCoordinates()) {
            for (Player player : playerList) {
                if (player.getRobot().getCoordinate() == coordinate) {
                    for (Attribute a : map.getTile(coordinate.getX(), coordinate.getY()).getAttributes()) {
                        Rotation rotation = ((Gear) a).getOrientation();
                        switch (rotation) {
                            case LEFT -> {
                                player.getRobot().rotate(Rotation.LEFT);
                                server.communicateAll(new PlayerTurning(player.getID(), Rotation.LEFT));
                            }
                            case RIGHT -> {
                                player.getRobot().rotate(Rotation.RIGHT);
                                server.communicateAll(new PlayerTurning(player.getID(), Rotation.RIGHT));
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Checkpoint is the final destination of the game and player wins the game as
     * soon as the player has reached all the checkpoints in numerical order.
     * The player gets the checkpoint token.
     * CheckPointReached and GameWon Protocol are sent.
     */

    public void activateControlPoint() {
        outerLoop:
        for (Coordinate coordinate : map.readControlPointCoordinate()) {
            for (Attribute a : map.getTile(coordinate).getAttributes()) {
                if (a.getType() == AttributeType.ControlPoint) {
                    int checkPointID = ((game.gameObjects.tiles.ControlPoint) a).getCount();
                    int totalCheckPoints = map.readControlPointCoordinate().size();
                    for (Player player : playerList) {
                        if (player.getRobot().getCoordinate().equals(coordinate)) {
                            logger.info("Total CheckPoints:" + totalCheckPoints);
                            if (player.getCheckPointCounter() == (checkPointID - 1)) {
                                if (checkPointID < totalCheckPoints) {
                                    int checkPoint = player.getCheckPointCounter();
                                    checkPoint++;
                                    player.setCheckPointCounter(checkPoint);
                                    player.message(new CheckpointReached(player.getID(), checkPointID));
                                } else if (checkPointID == totalCheckPoints) {
                                    server.communicateAll(new GameWon(player.getID()));
                                    break outerLoop;
                                }
                            } else if (player.getCheckPointCounter() > checkPointID) {
                                logger.info("CheckPoint Already Reached");
                            } else {
                                logger.info("You need to go CheckPoint " + (player.getCheckPointCounter() + 1) + " first");
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Push panels push any robots resting on them into the next space in the direction the push
     * panel faces. They activate only in the register that corresponds to the number on them. For
     * example, if you end register two on a push panel labeled “2, 4” you will be pushed. If you end
     * register three on the same push panel, you won’t be pushed.
     * Movement Protocol is sent to the players.
     */

    public void activatePushPanel() {
        for (Coordinate coordinate : map.getPushPanel()) {
            Tile tile = map.getTile(coordinate);
            for (Player player : playerList) {
                if (player.getRobot().getCoordinate() == coordinate) {
                    for (Attribute a : tile.getAttributes()) {
                        for (int i : ((PushPanel) a).getRegisters()) {
                            if (i == activationPhase.getCurrentRegister()) {
                                //new MoveRobot().doAction(((PushPanel) a).getOrientation(), player);
                                activationPhase.handleMove(player, ((PushPanel) a).getOrientation());
                                //FIXME I think we should use the handleMove method instead because MoveRobot is deprecated? -simon
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
        for (Coordinate coordinate : map.getEnergySpaces()) {
            for (Player player : playerList) {
                if (player.getRobot().getCoordinate() == coordinate) {
                    int energy = player.getEnergyCubes();
                    energy += energy;
                    player.setEnergyCubes(energy);
                    JSONBody jsonBody = new Energy(player.getID(), player.getEnergyCubes());
                    player.message(jsonBody);
                }
            }
        }
    }

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
                        if (a.getType() == AttributeType.RotatingBelt) {
                            orientations.add(((RotatingBelt) a).getOrientations()[0]);
                        }
                    }
                }
            }
        }

        handleBeltMovement(playersOnBelt, actionFinished, orientations, oldPositions);

        for (Player p : playersOnBelt) {
            if (!(p.getRobot().getCoordinate() == oldPositions.get(playersOnBelt.indexOf(p)))) {
                activationPhase.handleTile(p);
            }
        }
    }

    public void handleBeltMovement(ArrayList<Player> playersOnBelt, ArrayList<Boolean> actionFinished, ArrayList<Orientation> orientations, ArrayList<Coordinate> oldPositions) {
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
                                    collisionPlayer.getRobot().setCoordinate(oldPositions.get(playersOnBelt.indexOf(collisionPlayer)));
                                    actionFinished.set(playersOnBelt.indexOf(player), true);
                                }
                            }
                        }
                    }
                    if (move) {
                        player.getRobot().setCoordinate(newPos);
                        actionFinished.set(playersOnBelt.indexOf(player), true);
                    }
                }
            }

            if (!actionFinished.contains(false)) {
                finished = true;
            }
        }
    }

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
                        if (a.getType() == AttributeType.Belt) {
                            orientations.add(((Belt) a).getOrientation());
                        }
                        if (a.getType() == AttributeType.RotatingBelt) {
                            orientations.add(((RotatingBelt) a).getOrientations()[0]);
                        }
                    }
                }
            }
        }

        for (Player player : playersOnBelt) {
            oldPositions.add(player.getRobot().getCoordinate().clone());
        }

        handleBeltMovement(playersOnBelt, actionFinished, orientations, oldPositions);
        for (int j = 0; j < actionFinished.size(); j++) {
            actionFinished.set(j, false);
        }

        for (Player player : playersOnBelt) {
            boolean stillOnBelt = false;
            for (Attribute a : map.getTile(player.getRobot().getCoordinate()).getAttributes()) {
                if (a.getType() == AttributeType.Belt) {
                    stillOnBelt = true;
                } else {
                    if (a.getType() == AttributeType.RotatingBelt) {
                        stillOnBelt = true;
                    }
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

        for (Player p : playersOnBelt) {
            if (!p.getRobot().getCoordinate().equals(oldPositions.get(playersOnBelt.indexOf(p)))) {
                activationPhase.handleTile(p);
            }
        }
    }
}