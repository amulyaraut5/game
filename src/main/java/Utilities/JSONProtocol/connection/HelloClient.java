package Utilities.JSONProtocol.connection;

import Utilities.JSONProtocol.JSONBody;
import Utilities.JSONProtocol.JSONMessage;
public class HelloClient extends JSONBody {
    String protocol;

    public HelloClient(String protocol) {

        this.protocol = protocol;
    }

    public String getProtocol() {
        return protocol;
    }
}

/*
package Utilities.JSONProtocol.connection;

import Utilities.JSONProtocol.JSONMessage;

public class HelloClient extends JSONMessage {

    MessageBody messageBody = new MessageBody();

    public HelloClient(double protocol) {
        setType("HelloClient");
        messageBody.protocol = protocol;
    }

    public MessageBody getMessageBody() {
        return messageBody;
    }

    public static class MessageBody {
        public double protocol;

        public double getProtocol() {
            return protocol;
        }

    }


}
 */
