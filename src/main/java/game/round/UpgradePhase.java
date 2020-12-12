package game.round;

import game.Game;

/**
 * UpgradePhase, where the Upgrade Shop is filled/refilled and Upgrades can be purchased.
 */

public class UpgradePhase extends game.round.Round {


	public UpgradePhase(Game game) {
		super(game);
	}

	/**
	 * starts the Upgrade Phase.
	 *Calls the refill Method from the upgrade Shop and the purchase Upgrades method.
	 */
	public void startUpgradePhase() {

	}

	/**
	 * Handles the purchasing of upgrades.
	 * First the priorityList gets called and according to that each player is asked one by one
	 * if he wants to purchase upgrade cards.
	 * If yes it's checked if the player can afford the cardand if he has enough upgradeSlots left.
	 * If not he has to remove one of his cards and in the end the card is added to him.
	 */
	private void purchaseUpgrades() {
		// TODO - implement UpgradePhase.purchaseUpgrades
		/*
		game.getPriorityList
		view --> show UpgradeCards
		for (Player player : priorityList)
			view --> a if player
		 */
		//throw new UnsupportedOperationException();
	}

	/*private void refillUpgradeShop() {
		/*
		if (!roundCount == 1 && upgradeShopFill == getPlayerCount)  	--> if its not first round and the Upgrade Shop is full
			upgradeShop.clear()											--> clears the cards from the Shop
		else
			if (upgradeShop.getCardDeck < upgradeShop.getEmptySlots)	--> if there are still enough cards in the deck
				upgradeShop.shuffle()									--> shuffle discarded pile
			upgradeShop.refill()										--> refill UpgradeShop

		//throw new UnsupportedOperationException();
	} */

}