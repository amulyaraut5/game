package Utilities.JSONProtocol.connection;

import Utilities.JSONProtocol.JSONBody;

public class Welcome extends JSONBody {

    private int id;
    public Welcome(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
/*
    package Utilities.JSONProtocol.connection;

import Utilities.JSONProtocol.JSONMessage;

public class Welcome extends JSONMessage {

    private ID messageBody = new ID();

    public Welcome() {
        setType("Welcome");
        messageBody.setId(); //TODO generate ID!
    }

    public ID getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(ID messageBody) {
        this.messageBody = messageBody;
    }
}
     */


}
