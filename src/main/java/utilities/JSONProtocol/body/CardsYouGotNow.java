package utilities.JSONProtocol.body;

import game.gameObjects.cards.Card;
import utilities.JSONProtocol.JSONBody;

import java.util.ArrayList;

public class CardsYouGotNow extends JSONBody {
    ArrayList<Card> cards; //all the selected register cards

    public CardsYouGotNow(ArrayList<Card> cards) {
        this.cards = cards;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }
}
