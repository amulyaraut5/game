package utilities.JSONProtocol.body;

import game.gameObjects.cards.Card;
import utilities.JSONProtocol.JSONBody;

import java.util.ArrayList;

public class CurrentCards extends JSONBody {
    ArrayList<Card> activeCards;

    public CurrentCards(ArrayList<Card> activeCards) {
        this.activeCards = activeCards;
    }

    public ArrayList<Card> getActiveCards() {
        return activeCards;
    }
}
