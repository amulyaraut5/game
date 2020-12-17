package Utilities.JSONProtocol.connection;

import Utilities.JSONProtocol.JSONMessage;

public class HelloClient extends JSONMessage {

    private class MessageBody{
        public double protocol;
    }

    MessageBody messageBody = new MessageBody();

    public HelloClient(double protocol){
        setType("HelloClient");
        messageBody.protocol = protocol;
    }

    @Override
    public void clientMessage() {

    }

    @Override
    public void serverMessage() {

    }
}
