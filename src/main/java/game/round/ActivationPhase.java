package game.round;

import game.Player;
import game.gameObjects.cards.Card;
import game.gameObjects.maps.Map;
import game.gameObjects.tiles.Attribute;
import game.gameObjects.tiles.RotatingBelt;
import game.gameObjects.tiles.Tile;
import utilities.Coordinate;
import utilities.JSONProtocol.body.CurrentCards;
import utilities.Orientation;
import utilities.Utilities;

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
        gameMapTiles = game.getMap().getTiles();
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

    public void activateGreenBelts(){
        for(Coordinate tileCoordinate : gameMap.getGreenBelts()) {
            for(Player currentPlayer : playerList) {
                if(tileCoordinate.equals(currentPlayer.getRobot().getPosition())) {
                    for (Attribute a : gameMap.getTile(tileCoordinate).getAttributes()){
                        if(a.getType() == Utilities.AttributeType.Belt){
                            handleMove(currentPlayer, a.getOrientation());
                        }
                        if(a.getType() == Utilities.AttributeType.RotatingBelt){
                            RotatingBelt temp = (RotatingBelt) a;
                            handleMove(currentPlayer, temp.getOrientations()[1]);
                        }
                    }
                }
            }
        }
    }

    public void activateBlueBelts(){
        for (int i = 0; i < 2; i++) {
            for(Coordinate tileCoordinate : gameMap.getBlueBelts()) {
                for(Player currentPlayer : playerList) {
                    if(tileCoordinate.equals(currentPlayer.getRobot().getPosition())) {
                        for (Attribute a : gameMap.getTile(tileCoordinate).getAttributes()){
                            if(a.getType() == Utilities.AttributeType.Belt){
                                handleMove(currentPlayer, a.getOrientation());

                            }
                            if(a.getType() == Utilities.AttributeType.RotatingBelt){
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
    public void handleMove(Player player, Orientation o){
        Coordinate newPosition = null;
        if(o == Orientation.UP){
            newPosition = player.getRobot().getPosition().clone();
            newPosition.addToY(-1);
        }
        if(o == Orientation.RIGHT){
            newPosition = player.getRobot().getPosition().clone();
            newPosition.addToX(1);
        }
        if(o == Orientation.DOWN){
            newPosition = player.getRobot().getPosition().clone();
            newPosition.addToY(1);
        }
        if(o == Orientation.LEFT){
            newPosition = player.getRobot().getPosition().clone();
            newPosition.addToX(-1);


        }

        for (Attribute a : gameMap.getTile(newPosition).getAttributes()) {
            switch(a.getType()){
                //handle different tile effects here
            }
        }



    }


}