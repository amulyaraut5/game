package game.gameObjects.cards;

import game.Game;
import game.Player;

public abstract class UpgradeCard extends Card {

    private int cost;

    public UpgradeCard() {
    }

    @Override
    public void handleCard(Game game, Player player) {

    }

    public int getCost() {
        return cost;
    }

}