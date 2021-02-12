package utilities.JSONProtocol.body;

import utilities.JSONProtocol.JSONBody;
import utilities.enums.CardType;

import java.util.ArrayList;

public class YourCards extends JSONBody {
    private final ArrayList<CardType> cards;
    private final int cardsInPile;

    public YourCards(ArrayList<CardType> cards, int cardsInPile) {
        this.cards = cards;
        this.cardsInPile = cardsInPile;
    }

    public int getCardsInPile() {
        return cardsInPile;
    }

    public ArrayList<CardType> getCards() {
        return cards;
    }
}
