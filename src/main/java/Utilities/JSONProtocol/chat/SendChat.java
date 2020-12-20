package Utilities.JSONProtocol.chat;

import Utilities.JSONProtocol.JSONMessage;

/**
 * Class for messages with the type SendChat
 */
public class SendChat {
}

/*
package Utilities.JSONProtocol.chat;

import Utilities.JSONProtocol.JSONMessage;


* Class for messages with the type SendChat

public class SendChat extends JSONMessage {

    //create MessageBody object which is used in the constructor of SendChat
    SendChat.MessageBody messageBody = new SendChat.MessageBody();

    public SendChat(String message, int to) {
        setType("ReceivedChat");
        messageBody.message = message;
        messageBody.to = to;
    }

    //class for the Body of SendChat
    private class MessageBody {
        public String message;
        public int to;
    }
}
     */
