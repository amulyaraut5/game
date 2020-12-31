package utilities.JSONProtocol.body;

import utilities.JSONProtocol.JSONBody;

public class PlayCard extends JSONBody {

    String card;

    public PlayCard(String card) {
        this.card = card;
    }

    public String getCard() {
        return card;
    }

}
