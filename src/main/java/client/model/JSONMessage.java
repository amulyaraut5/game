package client.model;

/**
 * This class is a JSON message which is seperated in messagetype and messagebody
 */
public class JSONMessage {

    private String type = "default";

    private Object body;

    public JSONMessage(String messageType, String messageBody) {
        this.type = messageType;
        this.body = messageBody;
    }

    @Override
    public String toString() {
        return type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}