package Utilities.JSONProtocol;

import Utilities.JSONProtocol.JSONMessage;

/**
 * Class for messages with the type ReceivedChat
 */
public class ReceivedChat extends JSONMessage {

    //class for the Body of the ReceivedChat
    public class MessageBody{
        @Override
        public String toString() {
            return "MessageBody{" +
                    "message='" + message + '\'' +
                    ", from='" + from + '\'' +
                    ", privat=" + privat +
                    '}';
        }

        public String message;
        public String from;
        public boolean privat;
    }

    //create MessageBody object which is used in the constructor of ReceivedChat
    MessageBody messageBody = new MessageBody();

    public ReceivedChat(String message, String from, boolean privat){
        setType("ReceivedChat");
        messageBody.message = message;
        messageBody.from = from;
        messageBody.privat = privat;
    }


    @Override
    public void clientMessage() {
    }

    @Override
    public void serverMessage() {
    }

    @Override
    public String toString() {
        return "ReceivedChat{" +
                  messageBody +
                '}';
    }
}
