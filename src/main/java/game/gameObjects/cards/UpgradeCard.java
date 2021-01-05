package game.gameObjects.cards;

public abstract class UpgradeCard extends Card {

    private int cost;

    public UpgradeCard(String cardName) {
        super(cardName);
    }

    @Override
    public abstract void handleCard();

    public int getCost() {
        return cost;
    }

}