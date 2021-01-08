package game.gameObjects.cards;

import game.Game;
import game.Player;
import utilities.Utilities.CardType;

public abstract class UpgradeCard extends Card {

    private int cost;

    public UpgradeCard(CardType card) {
        super(card);
    }

    @Override
    public void handleCard(Game game, Player player) {

    }

    public int getCost() {
        return cost;
    }

}