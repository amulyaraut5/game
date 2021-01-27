package utilities.JSONProtocol.body;

import utilities.JSONProtocol.JSONBody;
import utilities.enums.CardType;

import java.util.ArrayList;

public class YourCards extends JSONBody {
    private ArrayList<CardType> cards;
    private int cardsInPile;

    public YourCards(ArrayList<CardType> cards, int cardsInPile) {
        this.cards = cards;
        this.cardsInPile = cardsInPile;
    }

    public ArrayList<CardType> getCards() {
        return cards;
    }

    public void setCards(ArrayList<CardType> cards) {
        this.cards = cards;
    }

    public int getCardsInPile() {
        return cardsInPile;
    }

    public void setCardsInPile(int cardsInPile) {
        this.cardsInPile = cardsInPile;
    }
}
