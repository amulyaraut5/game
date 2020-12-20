package Utilities.JSONProtocol.connection;

import Utilities.JSONProtocol.JSONMessage;
public class HelloClient {
    String protocol;

    public HelloClient(String protocol) {

        this.protocol = protocol;
    }

    public String getProtocol() {
        return protocol;
    }
}

/*
public class HelloClient extends JSONMessage {

    public static class MessageBody{
        public  double protocol;

        public  double getProtocol() {
            return protocol;
        }

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

 */
