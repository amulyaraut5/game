package utilities.JSONProtocol;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import game.gameObjects.tiles.Attribute;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.BoardElement;
import utilities.JSONProtocol.body.GameStarted;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * This class contains 2 important methods namely serialize and deserialize whose functionality are described
 * in respective method.
 *
 * @author Amulya
 * @author Simon
 */
public class Multiplex {

    private static final Logger logger = LogManager.getLogger();

    /**
     * This method converts a Java Object into Json String with the use of Gson library.
     *
     * @param messageObj is the type which of message which need to be converted
     * @return converted Json String
     */

    public static String serialize(JSONMessage messageObj) {
        Gson gson = new Gson();
        return gson.toJson(messageObj);
    }

    /**
     * This method converts Json String back to Java Object with the use of custom
     * gson deserializer.
     *
     * @param jsonString String which needs to be converted back to java Object.
     * @return Java Object
     */

    public static JSONMessage deserialize(String jsonString) {
        Gson gson = new GsonBuilder().registerTypeAdapter(JSONMessage.class, new Deserializer()).create();
        return gson.fromJson(jsonString, JSONMessage.class);
    }

    /**
     * This class contains an overridden deserialize method from interface JsonDeserializer</> in order to parse messageBody
     * of different classes correctly.
     */

    private static class Deserializer implements JsonDeserializer<JSONMessage> {

        @Override
        public JSONMessage deserialize(JsonElement jsonElement, Type typeofT,
                                       JsonDeserializationContext jsonDeserializationContext)
                throws JsonParseException {
            Gson gson = new Gson();
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            String messageType = jsonObject.get("messageType").getAsString();
            JsonObject messageBody = jsonObject.get("messageBody").getAsJsonObject();

            if (messageType != null) {
                if (messageType.equals("GameStarted")) {
                    Gson gsonTile = new GsonBuilder()
                            .registerTypeAdapter(Attribute.class, new AttributeDeserializer()).create();

                    JsonArray mapArray = messageBody.get("map").getAsJsonArray();
                    Type mapType = new TypeToken<ArrayList<BoardElement>>() {
                    }.getType();
                    ArrayList<BoardElement> mapBody = gsonTile.fromJson(mapArray, mapType);
                    GameStarted gameStarted = new GameStarted(mapBody);
                    return JSONMessage.build(gameStarted);
                } else try {
                    JSONBody body = gson.fromJson(messageBody, (Type) Class.forName("utilities.JSONProtocol.body." + messageType));
                    return JSONMessage.build(body);
                } catch (ClassNotFoundException e) {
                    logger.error("messageType could not be converted to a JSONBody Class: " + e.getMessage());
                }
            }
            return null;
        }
    }

    /**
     * This class contains an overridden deserialize method from interface JsonDeserializer</> in order to parse
     * through different subclasses of abstract class Attribute.
     */

    private static class AttributeDeserializer implements JsonDeserializer<Attribute> {

        @Override
        public Attribute deserialize(JsonElement jsonElement, Type typeofT,
                                     JsonDeserializationContext jsonDeserializationContext)
                throws JsonParseException {
            JsonObject tileObject = jsonElement.getAsJsonObject();
            String tileType = tileObject.get("type").getAsString();

            Gson gson = new GsonBuilder().create();
            try {
                return gson.fromJson(jsonElement, (Type) Class.forName("game.gameObjects.tiles." + tileType));
            } catch (ClassNotFoundException e) {
                logger.error("tileType could not be converted to a Attribute Class: " + e.getMessage());
            }
            return null;
        }
    }
}