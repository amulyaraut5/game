package utilities.JSONProtocol.body;

import utilities.JSONProtocol.JSONBody;
import utilities.enums.CardType;

public class SelectCard extends JSONBody {
    private CardType card;
    private int register;

    public SelectCard(CardType card, int register) {
        this.card = card;
        this.register = register;
    }

    public CardType getCard() {
        return card;
    }

    public int getRegister() {
        return register;
    }
}
