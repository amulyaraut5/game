package utilities.JSONProtocol.body;

import utilities.JSONProtocol.JSONBody;
import utilities.RegisterCard;

import java.util.ArrayList;

public class CurrentCards extends JSONBody {
    private final ArrayList<RegisterCard> activeCards;

    public CurrentCards(ArrayList<RegisterCard> activeCards) {
        this.activeCards = activeCards;
    }

    public ArrayList<RegisterCard> getActiveCards() {
        return activeCards;
    }
}
