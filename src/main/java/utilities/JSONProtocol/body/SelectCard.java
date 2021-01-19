package utilities.JSONProtocol.body;

import utilities.JSONProtocol.JSONBody;

public class SelectCard extends JSONBody {
    private String card;
    private int register;

    public SelectCard(String card, int register) {
        this.card = card;
        this.register = register;
    }

    public String getCard() {
        return card;
    }

    public int getRegister() {
        return register;
    }
}
