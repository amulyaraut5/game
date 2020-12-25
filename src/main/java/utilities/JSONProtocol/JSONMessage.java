package utilities.JSONProtocol;

import utilities.Utilities.MessageType;

/**
 * This abstract class is for serialization and deserialization of every message
 */
public class JSONMessage {

    private MessageType messageType;

    private JSONBody messageBody;

    public JSONMessage(MessageType messageType, JSONBody messageBody) {
        this.messageType = messageType;
        this.messageBody = messageBody;
        //System.out.println(messageType);
        //System.out.println(messageBody.getClass().getSimpleName());//TODO should we use the classname as messageType?
    }

    public MessageType getType() {
        return messageType;
    }

    public Object getBody() {
        return messageBody;
    }
}