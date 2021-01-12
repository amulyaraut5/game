package server;

import utilities.JSONProtocol.JSONMessage;

public class QueueMessage {
    private JSONMessage jsonMessage;
    private User user;


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
