package utilities.JSONProtocol.body;

import utilities.JSONProtocol.JSONBody;
import utilities.enums.CardType;

import java.util.ArrayList;

public class CardsYouGotNow extends JSONBody {
    private final ArrayList<CardType> cards; //all the selected register cards

    public CardsYouGotNow(ArrayList<CardType> cards) {
        this.cards = cards;
    }

    public ArrayList<CardType> getCards() {
        return cards;
    }
}
