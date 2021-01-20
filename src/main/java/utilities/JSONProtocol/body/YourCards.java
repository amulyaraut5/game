package utilities.JSONProtocol.body;

import utilities.JSONProtocol.JSONBody;
import utilities.enums.CardType;

import java.util.ArrayList;

public class YourCards extends JSONBody {
    private ArrayList<CardType> cards;

    public YourCards(ArrayList<CardType> cards) {
        this.cards = cards;
    }

    public ArrayList<CardType> getCards() {
        return cards;
    }

    public void setCards(ArrayList<CardType> cards) {
        this.cards = cards;
    }
}
