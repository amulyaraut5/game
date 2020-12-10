package game.round;

import game.Player;
import game.round.Round;

import java.util.ArrayList;

public class ActivationPhase extends Round {

	private ArrayList<Player> priorityList;

	public ActivationPhase() {
		// TODO - implement ActivationPhase.ActivationPhase
		//throw new UnsupportedOperationException();
	}

	public void startActivationPhase() {

	}

	private void activateRobots() {
		/* TODO - implement ActivationPhase.activateRobots
		for(register=0;register<5;register++) 		--> loops five times = Register
			flipRegister 							--> all players turn over their cards (in view?)
			game.getPriorityList 					--> returns priorityList
			for(Player player : priorityList)		--> oder Robots?
				if (!playerRebooted)
					card.activateCard(player, register)	--> robot?
		 */
		//throw new UnsupportedOperationException();
	}

	private void activateBoard() {
		// TODO - implement ActivationPhase.activateBoard
		//blueConveyor.performAction();

		//throw new UnsupportedOperationException();
	}

}