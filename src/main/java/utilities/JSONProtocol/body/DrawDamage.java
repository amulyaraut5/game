package utilities.JSONProtocol.body;

import game.gameObjects.cards.Card;
import utilities.JSONProtocol.JSONBody;

import java.util.ArrayList;

public class DrawDamage extends JSONBody {
    private int playerID;
    private ArrayList<Card> cards; //Schadenskarten

    public DrawDamage(int playerID, ArrayList<Card> cards) {
        this.playerID = playerID;
        this.cards = cards;
    }

    public int getPlayerID() {
        return playerID;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }
}
