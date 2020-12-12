package json;

import java.util.ArrayList;
import java.util.List;

/**
 * This class contains the Message and all necessary members
 */
public class Message {

    private String sender;
    private List<String> receiver = new ArrayList<>();
    private String message;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public List<String> getReceiver() {
        return receiver;
    }

    public void setReceiver(List<String> receiver) {
        this.receiver = receiver;
    }

    public void addReceiver(String receiver) {
        if (!this.receiver.contains(receiver))
            this.receiver.add(receiver);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}

