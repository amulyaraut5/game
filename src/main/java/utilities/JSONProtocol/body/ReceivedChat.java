package utilities.JSONProtocol.body;

import utilities.JSONProtocol.JSONBody;

public class ReceivedChat extends JSONBody {
    private final String message;
    private final int from;
    private final boolean privat;

    public ReceivedChat(String message, int from, boolean privat) {
        this.message = message;
        this.from = from;
        this.privat = privat;
    }

    public String getMessage() {
        return message;
    }

    public int getFrom() {
        return from;
    }

    public boolean isPrivat() {
        return privat;
    }
}
