package utilities.JSONProtocol.body;

import game.gameObjects.cards.Card;
import utilities.JSONProtocol.JSONBody;

import java.util.ArrayList;

public class SelectDamage extends JSONBody {
    ArrayList<Card> cards; //Schadenskarten

    public SelectDamage(ArrayList<Card> cards) {
        this.cards = cards;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }
}
