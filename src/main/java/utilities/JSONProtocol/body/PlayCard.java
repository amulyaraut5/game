package utilities.JSONProtocol.body;

import utilities.JSONProtocol.JSONBody;
import utilities.enums.CardType;

public class PlayCard extends JSONBody {
    private CardType card;

    public PlayCard(CardType card) {
        this.card = card;
    }

    public CardType getCard() {
        return card;
    }

}
