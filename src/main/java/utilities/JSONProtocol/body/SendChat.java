package utilities.JSONProtocol.body;

import utilities.JSONProtocol.JSONBody;

public class SendChat extends JSONBody {
    private String message;
    private int to;

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
