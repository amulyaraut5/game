package utilities.JSONProtocol.body;

import utilities.JSONProtocol.JSONBody;
import utilities.enums.CardType;

import java.util.ArrayList;

public class SelectDamage extends JSONBody {
    private final ArrayList<CardType> cards;

    public SelectDamage(ArrayList<CardType> cards) {
        this.cards = cards;
    }

    public ArrayList<CardType> getCards() {
        return cards;
    }
}
