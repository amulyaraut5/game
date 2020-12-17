package Utilities.JSONProtocol.connection;

import Utilities.JSONProtocol.JSONMessage;

public class Welcome extends JSONMessage {

    private ID messageBody = new ID();

    public Welcome(){
        setType("Welcome");
        messageBody.setId(); //TODO generate ID!
    }

    public ID getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(ID messageBody) {
        this.messageBody = messageBody;
    }

    @Override
    public void clientMessage() {

    }

    @Override
    public void serverMessage() {

    }
}
