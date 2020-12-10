package game.gameObjects.cards;

public abstract class UpgradeCard extends Card {

    private int cost;

    @Override
    public abstract void handleCard();

    public int getCost() {
        return cost;
    }

}