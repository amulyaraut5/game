package Utilities.JSONProtocol;


import Utilities.JSONProtocol.connection.HelloClient;
import Utilities.JSONProtocol.connection.HelloServer;
import Utilities.JSONProtocol.connection.Welcome;
import com.google.gson.*;

import java.io.IOException;
import java.lang.reflect.Type;

public class Multiplex {

    public static String serialize(JSONMessage messageObj) throws IOException {
        Gson gson = new Gson();
        String json = gson.toJson(messageObj);
        return json;
    }


    public static JSONMessage deserialize(String jsonString) {
        Gson gson = new GsonBuilder().registerTypeAdapter(JSONMessage.class, new Deserializer()).create();
        JSONMessage obj = gson.fromJson(jsonString, JSONMessage.class);
        return obj;
    }

    static class Deserializer implements JsonDeserializer<JSONMessage> {

        @Override
        public JSONMessage deserialize(JsonElement jsonElement, Type typeofT,
                                       JsonDeserializationContext jsonDeserializationContext)
                throws JsonParseException {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            JsonElement type = jsonObject.get("messageType");
            JsonObject messageBody = jsonObject.get("messageBody").getAsJsonObject();
            if (type != null) {
                switch (type.getAsString()) {
                    case "HelloServer":
                        //return jsonDeserializationContext.deserialize(jsonObject,HelloClient.class);

                        HelloServer hs = new HelloServer(messageBody.get("protocol").getAsDouble(),
                                messageBody.get("group").getAsString(),
                                messageBody.get("isAI").getAsBoolean());
                        return new JSONMessage("HelloServer", hs);
                    case "HelloClient":
                        //return jsonDeserializationContext.deserialize(jsonObject,HelloClient.class);

                        HelloClient hc = new HelloClient((messageBody.get("protocol").getAsDouble()));
                        return new JSONMessage("HelloClient", hc);
                    case "Welcome":
                        Welcome wc = new Welcome(messageBody.get("id").getAsInt());
                        return new JSONMessage("Welcome", wc);
                }
            }
            return null;
        }
    }



    /* <-----This version works too.--------->
    public static JSONMessage deserialize(String jsonString) {

        GsonBuilder gsonBuilder = new GsonBuilder();

        gsonBuilder.registerTypeAdapter(JSONMessage.class, Deserializer);

        Gson gson = gsonBuilder.setPrettyPrinting().create();

        JSONMessage messageObj = gson.fromJson(jsonString, JSONMessage.class);
        return messageObj;
    }
    public static JsonDeserializer<JSONMessage> Deserializer = new JsonDeserializer<JSONMessage>() {
        @Override
        public JSONMessage deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

            JsonObject jsonMessage = jsonElement.getAsJsonObject();

            JsonObject messageBody = jsonMessage.get("messageBody").getAsJsonObject();

            String messageType = jsonMessage.get("messageType").getAsString();

            if (messageType.equals("HelloClient")) {
                HelloClient helloClient = new HelloClient(
                        messageBody.get("protocol").getAsString()
                );

                return new JSONMessage("HelloClient", helloClient);
            }else if (messageType.equals("HelloServer")) {
                HelloServer helloServer = new HelloServer(
                        messageBody.get("group").getAsString(),
                        messageBody.get("isAI").getAsBoolean(),
                        messageBody.get("protocol").getAsString()
                );
                return new JSONMessage("HelloServer", helloServer);
            }
            return null;
        }
    };

     */



    /*
    public static JSONMessage  deserialize(String jsonString){
        Gson gson = new Gson();
        JSONMessage msg = gson.fromJson(jsonString, JSONMessage.class);


        String type = msg.getMessageType();
        Object messagebody = msg.getMessageBody();
        switch (type){
            case "HelloClient":
                msg = new JSONMessage("HelloClient", messagebody);
                return msg;

            case "HelloServer":
                msg = new JSONMessage("HelloServer", messagebody);
                return msg;
        }
        return msg;
    }

      */

}
