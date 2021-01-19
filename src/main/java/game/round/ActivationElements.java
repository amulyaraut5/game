package game.round;

import game.Game;
import game.Player;
import game.gameActions.MoveRobot;
import game.gameActions.RebootAction;
import game.gameObjects.maps.Map;
import game.gameObjects.tiles.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.Coordinate;
import utilities.JSONProtocol.JSONBody;
import utilities.JSONProtocol.body.CheckpointReached;
import utilities.JSONProtocol.body.Energy;
import utilities.JSONProtocol.body.GameWon;
import utilities.JSONProtocol.body.Reboot;
import utilities.enums.AttributeType;
import utilities.enums.Orientation;

import java.util.ArrayList;

public class ActivationElements {
    private static final Logger logger = LogManager.getLogger();
    Game game = Game.getInstance();
    ArrayList<Player> playerList = game.getPlayers();
    Map map = game.getMap();
    ActivationPhase activationPhase;


    public void activatePit() {
        for (Coordinate coordinate : map.getPitCoordinate()) {
            for(Player player: playerList){
                if (player.getRobot().getCoordinate() == coordinate) {
                    new RebootAction().doAction(player.getRobot().getOrientation(), player);

                    JSONBody jsonBody = new Reboot(player.getID());
                    player.message(jsonBody);
                }
            }
        }
    }

    public void activateGear(){
        for (Coordinate coordinate : map.getGearCoordinate()) {
            for(Player player: playerList){
                if (player.getRobot().getCoordinate() == coordinate) {
                    //TODO Change Rotation to Orientation or vice verse

                   //new RotateRobot().doAction();

                    JSONBody jsonBody = new Reboot(player.getID());
                    player.message(jsonBody);
                }
            }
        }
    }


    /**
     * Checkpoint is the final destination of the game and player wins the game as
     * soon as the player has reached all the checkpoints in chronological order.
     * The player gets the checkpoint token.
     *
     */

    public void activateControlPoint(){
        for(Coordinate coordinate: map.getEnergySpaceCoordinate()){

            Tile tile = map.getTile(coordinate);
            for(Attribute a : tile.getAttributes()){
                int count = ((game.gameObjects.tiles.Laser) a).getCount();

                for(Player player: playerList){
                    if (player.getRobot().getCoordinate() == coordinate){

                        JSONBody jsonBody = new CheckpointReached(player.getID(),count);
                        player.message(jsonBody);

                        if (game.getNoOfCheckPoints() == 1) {
                            JSONBody jsonBody1 = new GameWon(player.getID());
                            player.message(jsonBody1);
                            // TODO End the game:
                        }
                        else if (game.getNoOfCheckPoints() != 1) {
                            if (player.getCheckPointCounter() > count) {
                                logger.info("Checkpoint already reached");
                                // Maybe inform all the clients and users

                            } else if (player.getCheckPointCounter() < 1) {
                                logger.info(" 1st Checkpoint not reached.");

                            } else if (player.getCheckPointCounter() == 1) {
                                int checkPoint = player.getCheckPointCounter();
                                checkPoint++;
                                player.setCheckPointCounter(checkPoint);

                                if ((game.getNoOfCheckPoints() == 2) && (count == 2)) {
                                    JSONBody jsonBody1 = new GameWon(player.getID());
                                    player.message(jsonBody1);
                                    // TODO End the game:
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Whenever the player finds in this tile, player gets the energy token.
     *
     */

    public void activateEnergySpace(){
        for(Coordinate coordinate: map.getEnergySpaceCoordinate()){
            for(Player player: playerList){
                if (player.getRobot().getCoordinate() == coordinate) {

                    int energy = player.getEnergyCubes();
                    energy += energy;
                    player.setEnergyCubes(energy);
                    // Todo Decrease the energy cube number

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

        ArrayList<Player> movedPlayers = new ArrayList<>();

        for (Coordinate tileCoordinate : map.getGreenBelts()) {
            for (Player currentPlayer : playerList) {
                if (tileCoordinate.equals(currentPlayer.getRobot().getCoordinate())) {
                    playersOnBelt.add(currentPlayer);
                    actionFinished.add(false);

                    for (Attribute a : map.getTile(tileCoordinate).getAttributes()) {
                        if(a.getType() == AttributeType.Belt){
                            orientations.add(((Belt) a).getOrientation());
                        }
                        if(a.getType() == AttributeType.RotatingBelt){
                            orientations.add(((RotatingBelt) a).getOrientations()[1]);
                        }
                    }

                }
            }
        }

        for (Player player : playersOnBelt) {
            oldPositions.add(player.getRobot().getCoordinate());
        }
        boolean finished = false;
        while(!finished){

            for (Player player : playersOnBelt) {
                if(!actionFinished.get(playersOnBelt.indexOf(player))){
                    boolean move = true;
                    Coordinate newPos = calculateNew(player, orientations.get(playersOnBelt.indexOf(player)));
                    for (Player collisionPlayer : playerList) {
                        if(collisionPlayer.getRobot().getCoordinate().equals(newPos)) {
                            move = false;
                            if (!playersOnBelt.contains(collisionPlayer)) {
                                actionFinished.set(playersOnBelt.indexOf(player), true);
                            } else {
                                if (actionFinished.get(playersOnBelt.indexOf(collisionPlayer))) {
                                    collisionPlayer.getRobot().setCoordinate(oldPositions.get(playersOnBelt.indexOf(collisionPlayer)));
                                    movedPlayers.remove(collisionPlayer);
                                    actionFinished.set(playersOnBelt.indexOf(player), true);
                                }
                            }
                        }
                    }
                    if(move){
                        player.getRobot().setCoordinate(newPos);
                        movedPlayers.add(player);
                    }
                }
            }

            if(!actionFinished.contains(false)){
                finished = true;
            }
        }

        for (Player p : movedPlayers) {
            activationPhase.communicateBeltMovement(p);
        }
    }

    public Coordinate calculateNew(Player player, Orientation o){
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

    public void activateBlueBelts(){
        ArrayList<Player> playersOnBelt = new ArrayList<>();
        ArrayList<Boolean> actionFinished = new ArrayList<>();
        ArrayList<Orientation> orientations = new ArrayList<>();
        ArrayList<Coordinate> oldPositions = new ArrayList<>();

        ArrayList<Player> movedPlayers = new ArrayList<>();

        for (Coordinate tileCoordinate : map.getBlueBelts()) {
            for (Player currentPlayer : playerList) {
                if (tileCoordinate.equals(currentPlayer.getRobot().getCoordinate())) {
                    playersOnBelt.add(currentPlayer);
                    actionFinished.add(false);

                    for (Attribute a : map.getTile(tileCoordinate).getAttributes()) {
                        if(a.getType() == AttributeType.Belt){
                            orientations.add(((Belt) a).getOrientation());
                        }
                        if(a.getType() == AttributeType.RotatingBelt){
                            orientations.add(((RotatingBelt) a).getOrientations()[1]);
                        }
                    }

                }
            }
        }

        for (Player player : playersOnBelt) {
            oldPositions.add(player.getRobot().getCoordinate());
        }

        for (int i = 0; i < 2; i++){
            for (boolean b : actionFinished) {
                b = false;
            }

            boolean finished = false;
            while(!finished) {

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
                        }
                    }
                }

                if (!actionFinished.contains(false)) {
                    finished = true;
                }
            }
        }

        for (Player p : playersOnBelt) {
            if(p.getRobot().getCoordinate().equals(oldPositions.get(playersOnBelt.indexOf(p)))){
                activationPhase.communicateBeltMovement(p);
            }
        }
    }


    /**
     * Push panels push any robots resting on them into the next space in the direction the push
     * panel faces. They activate only in the register that corresponds to the number on them. For
     * example, if you end register two on a push panel labeled “2, 4” you will be pushed. If you end
     * register three on the same push panel, you won’t be pushed.
     */

    public void activatePushPanel(){
        for(Coordinate coordinate: map.getPushPanelCoordinate()){
            Tile tile = map.getTile(coordinate);
            for(Player player: playerList){
                if (player.getRobot().getCoordinate() == coordinate) {
                    for(Attribute a : tile.getAttributes()){
                        for(int i : ((game.gameObjects.tiles.PushPanel) a).getRegisters()){
                            if( i == player.getCurrentRegister()){
                                new MoveRobot().doAction(((game.gameObjects.tiles.Laser) a).getOrientation(), player);
                            }
                        }
                    }
                }
            }
        }
    }
}