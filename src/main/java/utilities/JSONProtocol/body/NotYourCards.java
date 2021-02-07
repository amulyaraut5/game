package utilities.JSONProtocol.body;

import utilities.JSONProtocol.JSONBody;

public class NotYourCards extends JSONBody {
    private final int playerID;
    private final int cardsInHand;
    private final int cardsInPile;

    public NotYourCards(int playerID, int cardsInHand, int cardsInPile) {
        this.playerID = playerID;
        this.cardsInHand = cardsInHand;
        this.cardsInPile = cardsInPile;
    }

    public int getPlayerID() {
        return playerID;
    }

    public int getCardsInHand() {
        return cardsInHand;
    }

    public int getCardsInPile() {
        return cardsInPile;
    }
}
