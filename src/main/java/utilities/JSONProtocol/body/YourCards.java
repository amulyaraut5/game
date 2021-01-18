package utilities.JSONProtocol.body;

import game.gameObjects.cards.Card;
import utilities.JSONProtocol.JSONBody;

import java.util.ArrayList;

public class YourCards extends JSONBody {
    ArrayList<String> cards;

    public YourCards(ArrayList<String> cards) {
        this.cards = cards;
    }

    public ArrayList<String> getCards() {
        return cards;
    }

    public void setCards(ArrayList<String> cards) {
        this.cards = cards;
    }
}
