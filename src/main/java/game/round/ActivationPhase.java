package game.round;

import game.Player;
import game.gameObjects.cards.Card;
import game.gameObjects.maps.Map;
import game.gameObjects.tiles.Attribute;
import game.gameObjects.tiles.Belt;
import game.gameObjects.tiles.RotatingBelt;
import game.gameObjects.tiles.Tile;
import utilities.Coordinate;
import utilities.JSONProtocol.body.CurrentCards;
import utilities.Orientation;
import utilities.Utilities;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The Activation Phase is the third phase in the Round.
 * In this class the Programming Cards and GameBoard Tiles are activated.
 *
 * @author janau, sarah
 */

public class ActivationPhase extends Phase {

    // TODO when we transfer StartBoard: private ArrayList<Player> priorityList;

    /**
     * saves the Player ID and the card for the current register
     */
    private HashMap<Integer, Card> currentCards = new HashMap<>();

    private Tile [][] gameMapTiles;
    private Map gameMap;

    public ActivationPhase() {

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

    private void activateBoard() {
        // TODO - implement ActivationPhase.activateBoard
        gameMapTiles = game.getMap().getTiles();
        gameMap = game.getMap();

        for(Coordinate tileCoordinate : gameMap.getBeltCoordinates()) {
            for(Player player : playerList) {
                if(player.getRobot().getPosition() == tileCoordinate) {

                }
            }
        }
		/*
		blueConveyor.performAction();
		greenConveyor.performAction();
		pushPanel.performAction();
		gear.performAction();
		boardLaser.performAction();
		robotLaser.performAction(); TODO is there a robot laser?
		energySpace.performAction();
		checkPoint.performAction();
		 */


        //throw new UnsupportedOperationException();
    }

    private void activateGreenBelts(){

        for(Coordinate tileCoordinate : gameMap.getGreenBelts()) {
            for(Player player : playerList) {
                if(player.getRobot().getPosition() == tileCoordinate) {
                    for (Attribute a : gameMap.getTile(tileCoordinate).getAttributes() ) {
                        if(a.getType() == Utilities.AttributeType.Belt){
                            if(a.getOrientation() == Orientation.UP){
                                //move up
                            }
                            if(a.getOrientation() == Orientation.RIGHT){
                                //move right
                            }
                            if(a.getOrientation() == Orientation.LEFT){
                                //move left
                            }
                            if(a.getOrientation() == Orientation.DOWN){
                                //move down
                            }

                        }

                        if(a.getType() == Utilities.AttributeType.RotatingBelt){
                            if(((RotatingBelt) a).getOrientations()[1] == Orientation.UP){
                                //move up
                            }
                            if(((RotatingBelt) a).getOrientations()[1] == Orientation.RIGHT){
                                //move right
                            }
                            if(((RotatingBelt) a).getOrientations()[1] == Orientation.LEFT){
                                //move left
                            }
                            if(((RotatingBelt) a).getOrientations()[1] == Orientation.DOWN){
                                //move down
                            }
                        }
                    }


                }
            }
        }






    }


}