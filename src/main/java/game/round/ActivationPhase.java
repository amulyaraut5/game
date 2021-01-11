package game.round;

import game.Player;

import java.util.ArrayList;

/**
 * Activation Phase, where the Programming Cards and GameBoard Tiles are activated
 *
 * @author janau, sarah
 */

public class ActivationPhase extends Phase {

    private ArrayList<Player> priorityList;

    public ActivationPhase() {

    }


    /**
     * starts the ActivationPhase.
     * In every register the priority is determined and the players cards get activated
     * in priority order.
     * After each register the method for activating the board tiles ist called.
     */
    @Override
    public void startPhase() {
        for (int register = 1; register < 6; register++) {

        }
		/*
		for(register=0;register<5;register++) 		--> loops five times = Register
		flipRegister 							--> all players turn over their cards (in view?)
		game.getPriorityList 					--> returns priorityList
		for(Player player : priorityList)		--> oder Robots?
		if (!playerRebooted)
			card.activateCard(player, register)	--> robot?
		 */
        //throw new UnsupportedOperationException();
    }

    /**
     * Method that activates the board elements in their right order.
     */

    private void activateBoard() {
        // TODO - implement ActivationPhase.activateBoard
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


}