package Utilities.JSONProtocol.connection;

import Utilities.JSONProtocol.JSONMessage;
import com.google.gson.annotations.Expose;

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
