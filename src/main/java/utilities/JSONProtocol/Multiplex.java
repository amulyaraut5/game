package utilities.JSONProtocol;


import com.google.gson.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * This class contains 2 important methods namely serialize and deserialize whose functionality are described
 * in respective method.
 */
public class Multiplex {

    private static final Logger logger = LogManager.getLogger();

    /**
     * This method converts a Java Object into Json String with the use of Gson library.
     *
     * @param messageObj
     * @return
     * @throws IOException
     */

    public static String serialize(JSONMessage messageObj) {
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
            Gson gson = new Gson();
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            String messageType = jsonObject.get("messageType").getAsString();
            JsonObject messageBody = jsonObject.get("messageBody").getAsJsonObject();
            if (messageType != null) {
                try {
                    JSONBody body = gson.fromJson(messageBody, (Type) Class.forName("utilities.JSONProtocol.body." + messageType));
                    return new JSONMessage(body);
                } catch (ClassNotFoundException e) {
                    logger.error("messageType could not be converted to a JSONBody Class: " + e.getMessage());
                }
            }
            return null;
        }
    }
}
