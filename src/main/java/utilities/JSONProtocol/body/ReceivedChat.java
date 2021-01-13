package utilities.JSONProtocol.body;

import utilities.JSONProtocol.JSONBody;

public class ReceivedChat extends JSONBody {
    String message;

    int from;

    boolean privat;

    public ReceivedChat(String message, int from, boolean privat) {
        this.message = message;
        this.from = from;
        this.privat = privat;
    }

    public String getMessage() {
        return this.message;
    }

    public int getFrom() {
        return this.from;
    }

    public boolean isPrivat() {
        return this.privat;
    }
}
