package client.model;

/**
 * This class is a JSON message which is seperated in messagetype and messagebody
 */
public class JSONMessage {

    private String messageType = "default";

    private String messageBody; //TODO cast to object

    public JSONMessage(String messageType, String messageBody) {
        this.messageType = messageType;
        this.messageBody = messageBody;
    }

    public void setMessageType(String type) {
        messageType = type;
    }
    public String getMessageType() {
        return messageType;
    }

    public String getMessageBody() {
        return messageBody.substring(1, messageBody.length()-1);
    }

    @Override
    public String toString() {
        return messageType;
    }
}