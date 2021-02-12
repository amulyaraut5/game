package utilities.JSONProtocol.body;

import utilities.JSONProtocol.JSONBody;

public class SendChat extends JSONBody {
    private final String message;
    private final int to;

    public SendChat(String message, int to) {
        this.message = message;
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public int getTo() {
        return to;
    }
}
