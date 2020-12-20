package Utilities.JSONProtocol;

import com.google.gson.*;

import java.lang.reflect.Type;

public class Deserialize implements JsonDeserializer<JSONMessage> {

    @Override
    public JSONMessage deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jObject = jsonElement.getAsJsonObject(); //get the json as json object
        String typeClass = jObject.get("messageTyp").getAsString(); //get the MessageType of json object as String
        try { //try: Interface jsonDeserializationContext with derserialize method, which takes the json object and the type of the expected return value, in this case the class
            return jsonDeserializationContext.deserialize(jObject, Class.forName("Utilities.JSONProtocol." + typeClass)); //Class.forName: Returns the Class object associated with the class with the given string name.
            //returns an object of type Class
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    //takes json String and converts it into JSONMessage object
    public JSONMessage deserialize(String jsonString){
        Gson gson = new GsonBuilder().registerTypeAdapter(JSONMessage.class, new Deserialize()).create();
        JSONMessage jmessage = gson.fromJson(jsonString, JSONMessage.class);
        return jmessage;
    }


}