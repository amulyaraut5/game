package utilities.JSONProtocol.body;

import utilities.JSONProtocol.JSONBody;
import utilities.enums.CardType;

import java.util.ArrayList;

public class DrawDamage extends JSONBody {
    private final int playerID;
    private final ArrayList<CardType> cards; //Schadenskarten

    public DrawDamage(int playerID, ArrayList<CardType> cards) {
        this.playerID = playerID;
        this.cards = cards;
    }

    public int getPlayerID() {
        return playerID;
    }

    public ArrayList<CardType> getCards() {
        return cards;
    }
}
