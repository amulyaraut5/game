package utilities.JSONProtocol.chat;

import utilities.JSONProtocol.JSONBody;

public class ReceivedChat extends JSONBody {
    String message;

    String from;

    boolean privat;

    public ReceivedChat(String message, String from, boolean privat) {
        this.message = message;
        this.from = from;
        this.privat = privat;
    }

    public String getMessage() {
        return this.message;
    }

    public String getFrom() {
        return this.from;
    }

    public boolean isPrivat() {
        return this.privat;
    }
}
