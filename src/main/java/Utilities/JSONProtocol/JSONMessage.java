package Utilities.JSONProtocol;

import Utilities.Utilities.MessageType;

/**
 * This abstract class is for serialization and deserialization of every message
 */
public class JSONMessage {

    private MessageType messageType;

    private JSONBody messageBody;

    public JSONMessage(MessageType messageType, JSONBody messageBody) {
        this.messageType = messageType;
        this.messageBody = messageBody;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public Object getMessageBody() {
        return messageBody;
    }
}