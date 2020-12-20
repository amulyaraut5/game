package Utilities.JSONProtocol;

import com.google.gson.Gson;

/**
 * This abstract class is for serialization and deserialization of every message
 */
public abstract class JSONMessage {

    private String messageTyp = "default";

    public String serialize(){
        Gson gson = new Gson();
        String json;
        json = gson.toJson(this);
        return json;
    }

    //change laster
    public JSONMessage deserialize(String jsonString) {
        Deserialize deserializer = new Deserialize();
        return deserializer.deserialize(jsonString);
    }


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

}