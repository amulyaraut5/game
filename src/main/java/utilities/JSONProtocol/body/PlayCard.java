package utilities.JSONProtocol.body;

import utilities.JSONProtocol.JSONBody;

public class PlayCard extends JSONBody {
    private String card;

    public PlayCard(String card) {
        this.card = card;
    }

    public String getCard() {
        return card;
    }

}
