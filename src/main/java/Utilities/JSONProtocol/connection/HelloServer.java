package Utilities.JSONProtocol.connection;

import Utilities.JSONProtocol.JSONBody;

public class HelloServer extends JSONBody {

    private String group;

    private Boolean isAI;

    private double protocol;

    public HelloServer(double protocol, String group, Boolean isAI) {
        this.protocol = protocol;
        this.group = group;
        this.isAI = isAI;
    }

    public String getGroup() {
        return group;
    }

    public Boolean isAI() {
        return isAI;
    }

    public double getProtocol() {
        return protocol;
    }
}





    /*
    package Utilities.JSONProtocol.connection;

import Utilities.JSONProtocol.JSONMessage;

public class HelloServer extends JSONMessage {

    private class MessageBody{
        public double protocol;
        public String group;
        public boolean isAI;
    }

    MessageBody messageBody = new MessageBody();

    public HelloServer(double protocol, String group, boolean isAI){
        setType("HelloServer");

        messageBody.protocol = protocol;
        messageBody.group = group;
        messageBody.isAI = isAI;
    }
}
*/
