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

    @Override
    public void clientMessage() {

    }

    @Override
    public void serverMessage() {

    }


}
