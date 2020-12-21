package Utilities.JSONProtocol.chat;

import Utilities.JSONProtocol.JSONBody;

public class SendChat extends JSONBody {
    String message;

    int to;

    public SendChat(String message, int to) {
        this.message = message;
        this.to = to;
    }

    public String getMessage() {
        return this.message;
    }

    public int getTo() {
        return this.to;
    }
}
