package Utilities.JSONProtocol;


import Utilities.JSONProtocol.connection.HelloClient;
import Utilities.JSONProtocol.connection.HelloServer;
import Utilities.JSONProtocol.connection.Welcome;
import Utilities.Utilities.MessageType;
import com.google.gson.*;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * This class contains 2 important methods namely serialize and deserialize whose functionality are described
 * in respective method.
 */
public class Multiplex {

    /**
     * This method converts a Java Object into Json String with the use of Gson library.
     *
     * @param messageObj
     * @return
     * @throws IOException
     */

    public static String serialize(JSONMessage messageObj) throws IOException {
        Gson gson = new Gson();
        String json = gson.toJson(messageObj);
        return json;
    }

    /**
     * This method converts Json String back to Java Object with the use of custom
     * gson deserializer.
     *
     * @param jsonString
     * @return
     */

    public static JSONMessage deserialize(String jsonString) {
        Gson gson = new GsonBuilder().registerTypeAdapter(JSONMessage.class, new Deserializer()).create();
        JSONMessage obj = gson.fromJson(jsonString, JSONMessage.class);
        return obj;
    }

    /**
     * This class contains an overriden deserialize method from interface JsonDeserializer</> in order to parse messageBody
     * of different classes correctly.
     */

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
                        HelloServer hs = new HelloServer(messageBody.get("protocol").getAsDouble(),
                                messageBody.get("group").getAsString(),
                                messageBody.get("isAI").getAsBoolean());
                        return new JSONMessage(MessageType.HelloServer, hs);
                    case "HelloClient":

                        HelloClient hc = new HelloClient((messageBody.get("protocol").getAsDouble()));
                        return new JSONMessage(MessageType.HelloClient, hc);
                    case "Welcome":
                        Welcome wc = new Welcome(messageBody.get("playerID").getAsInt());
                        return new JSONMessage(MessageType.Welcome, wc);
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
}
