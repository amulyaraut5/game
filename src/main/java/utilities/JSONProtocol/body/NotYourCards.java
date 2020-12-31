package utilities.JSONProtocol.body;

import utilities.JSONProtocol.JSONBody;

public class NotYourCards extends JSONBody {
    int playerID;
    int cards; //Die anderen Spieler werden lediglich über die Anzahl der Karten benachrichtigt

    public NotYourCards(int playerID, int cards) {
        this.playerID = playerID;
        this.cards = cards;
    }

    public int getPlayerID() {
        return playerID;
    }

    public int getCards() {
        return cards;
    }
}
