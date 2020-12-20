package Utilities.JSONProtocol;

import Utilities.JSONProtocol.connection.HelloClient;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

import java.lang.reflect.Type;

/**
 * This abstract class is for serialization and deserialization of every message
 */
public  class JSONMessage {

    private String messageType = "default";

    private Object messageBody;

    public JSONMessage(String messageType, Object messageBody) {
        this.messageType = messageType;
        this.messageBody = messageBody;
    }

    public String getMessageType() {
        return messageType;
    }

    public Object getMessageBody() {
        return messageBody;
    }

    /*
    public String serialize(){
        Gson gson = new Gson();
        String json = gson.toJson(this);
        return json;
    }
    //--------->
    //change later
    public static JSONMessage deserialize(String jsonString){
        Gson gson = new Gson();
        JSONMessage obj = gson.fromJson(jsonString, JSONMessage.class);
        return obj;
    }


    //---------->

    //handle the received messages for client side and server side
    //Maybe with switch case, different methods in classes for different cases of messages
    public abstract void clientMessage();
    public abstract void serverMessage();

    @Override
    public String toString() {
        return messageTyp;
    }

    public String getType() {
        return messageTyp;
    }

    public void setType(String type) {
        this.messageTyp = type;
    }

     */

}