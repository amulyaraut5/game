package utilities.JSONProtocol;

import utilities.Utilities.MessageType;

/**
 * This abstract class is for serialization and deserialization of every message
 */
public class JSONMessage {

    private MessageType messageType;

    private JSONBody messageBody;

    public JSONMessage(JSONBody messageBody) {
        this.messageBody = messageBody;

        var type = messageBody.getClass().getSimpleName();
        messageType = MessageType.valueOf(type);
    }

    public JSONMessage() {

    }

    public static JSONMessage create(JSONBody messageBody) {
        var msg = new JSONMessage();
        msg.messageBody = messageBody;
        var type = messageBody.getClass().getSimpleName();
        msg.messageType = MessageType.valueOf(type);
        return msg;
    }

    public MessageType getType() {
        return messageType;
    }

    public Object getBody() {
        return messageBody;
    }
}