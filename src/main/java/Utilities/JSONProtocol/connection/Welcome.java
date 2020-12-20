package Utilities.JSONProtocol.connection;

public class Welcome {
    /*
    Random r = new Random();
    int low = 10;
    int high = 100;
    int result = r.nextInt(high-low) + low;

 */
    public String message;
    private int id;

    public Welcome(String message, int id) {
        this.message = message;
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    /*
    package Utilities.JSONProtocol.connection;

import Utilities.JSONProtocol.JSONMessage;

public class Welcome extends JSONMessage {

    private ID messageBody = new ID();

    public Welcome() {
        setType("Welcome");
        messageBody.setId(); //TODO generate ID!
    }

    public ID getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(ID messageBody) {
        this.messageBody = messageBody;
    }
}
     */


}
