package utilities.JSONProtocol.body;

import game.gameObjects.cards.Card;
import utilities.JSONProtocol.JSONBody;

import java.util.HashMap;

public class CurrentCards extends JSONBody {
    HashMap<Integer, Card> activeCards;

    public CurrentCards(HashMap<Integer, Card> activeCards) {
        this.activeCards = activeCards;
    }

    public HashMap<Integer, Card> getActiveCards() {
        return activeCards;
    }
}
