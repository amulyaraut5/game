package server;

/**
 * This class is the wrapper class for all JSON messages of the protocol. It both
 * contains the type and body of the message.
 * Depending on the type of message, the needed variables will be initialized by
 * the appropriate constructor for the {@link }.
 *
 * @author Manuel Neumayer
 */
public class JSONMessage {

    private String messageType = "default";

    private Object messageBody;

    public JSONMessage(String messageType, Object messageBody) {
        this.messageType = messageType;
        this.messageBody = messageBody;
    }

    public void setMessageType(String type) {
        messageType = type;
    }
    public String getMessageType() {
        return messageType;
    }

    public Object getMessageBody() {
        return messageBody;
    }

    @Override
    public String toString() {
        return messageType;
    }
}