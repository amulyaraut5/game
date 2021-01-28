package utilities.JSONProtocol;

import utilities.enums.MessageType;

/**
 * This abstract class is for serialization and deserialization of every message
 */
public class JSONMessage {

    private MessageType messageType;

    private JSONBody messageBody;

    private JSONMessage() {
    }

    public static JSONMessage build(JSONBody messageBody) {
        var msg = new JSONMessage();
        msg.messageBody = messageBody;
        String type = messageBody.getClass().getSimpleName();
        msg.messageType = MessageType.valueOf(type);
        return msg;
    }

    public MessageType getType() {
        return messageType;
    }

    public JSONBody getBody() {
        return messageBody;
    }
}