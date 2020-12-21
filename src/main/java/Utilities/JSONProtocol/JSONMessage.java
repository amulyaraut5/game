package Utilities.JSONProtocol;

/**
 * This abstract class is for serialization and deserialization of every message
 */
public  class JSONMessage {

    private String messageType = "default";

    private JSONBody messageBody;

    public JSONMessage(String messageType, JSONBody messageBody) {
        this.messageType = messageType;
        this.messageBody = messageBody;
    }

    public String getMessageType() {
        return messageType;
    }

    public Object getMessageBody() {
        return messageBody;
    }
}