package Utilities.JSONProtocol.connection;

import Utilities.JSONProtocol.JSONMessage;

public class HelloServer {

    private String group;

    private Boolean isAI;

    private String protocol;

    public HelloServer(String group, Boolean isAI, String protocol) {
        this.group = group;
        this.isAI = isAI;
        this.protocol = protocol;
    }

    public String getGroup() {
        return group;
    }

    public Boolean isAI() {
        return isAI;
    }

    public String getProtocol() {
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
