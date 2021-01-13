package server;

import utilities.JSONProtocol.JSONMessage;

public class QueueMessage {
    private final JSONMessage jsonMessage;
    private final User user;


    public QueueMessage(JSONMessage jsonMessage, User user) {
        this.jsonMessage = jsonMessage;
        this.user = user;
    }

    public JSONMessage getJsonMessage() {
        return jsonMessage;
    }

    public User getUser() {
        return user;
    }
}
