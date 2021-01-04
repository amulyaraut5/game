package utilities.JSONProtocol;


import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import game.gameObjects.tiles.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.JSONProtocol.body.GameStarted;
import utilities.JSONProtocol.body.gameStarted.BoardElement;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

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

            /*Gson gsonTile = new GsonBuilder()
                    .registerTypeAdapter(Attribute.class, tileJsonDeserializer).create();*/
            Gson gsonTile = new GsonBuilder()
                    .registerTypeAdapter(Attribute.class, new AttributeDeserializer()).create();

            if (messageType.equals("GameStarted")) {

                JsonArray mapArray = messageBody.get("map").getAsJsonArray();
                //JsonArray field = map.get(1).getAsJsonArray();
                Type mapType = new TypeToken<ArrayList<BoardElement>>() {}.getType();
                ArrayList<BoardElement> mapBody = gsonTile.fromJson(mapArray, mapType);
                GameStarted gameStarted = new GameStarted(mapBody);
                return new JSONMessage(gameStarted);
            } else if (messageType != null) {
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

    static class AttributeDeserializer implements JsonDeserializer<Attribute> {

        @Override
        public Attribute deserialize(JsonElement jsonElement, Type typeofT,
                                     JsonDeserializationContext jsonDeserializationContext)
                throws JsonParseException {
            JsonObject tileObject = jsonElement.getAsJsonObject();
            String tileType = tileObject.get("type").getAsString();

            Gson gson = new GsonBuilder().create();

            if (tileType.equals("ControlPoint")) {
                ControlPoint result = gson.fromJson(jsonElement, ControlPoint.class);
                return result;
            } else if (tileType.equals("Antenna")) {
                Antenna result = gson.fromJson(jsonElement, Antenna.class);
                return result;
            }else if (tileType.equals("Belt")) {
                Belt result = gson.fromJson(jsonElement, Belt.class);
                return result;
            }else if (tileType.equals("Empty")) {
                Empty result = gson.fromJson(jsonElement, Empty.class);
                return result;
            } else if (tileType.equals("EnergySpace")) {
                EnergySpace result = gson.fromJson(jsonElement, EnergySpace.class);
                return result;
            } else if (tileType.equals("Gear")) {
                Gear result = gson.fromJson(jsonElement, Gear.class);
                return result;
            } else if (tileType.equals("Laser")) {
                Laser result = gson.fromJson(jsonElement, Laser.class);
                return result;
            } else if (tileType.equals("Pit")) {
                Pit result = gson.fromJson(jsonElement, Pit.class);
                return result;
            } else if (tileType.equals("PushPanel")) {
                PushPanel result = gson.fromJson(jsonElement, PushPanel.class);
                return result;
            } else if (tileType.equals("RotatingBelt")) {
                RotatingBelt result = gson.fromJson(jsonElement, RotatingBelt.class);
                return result;
            } else if (tileType.equals("Wall")) {
                Wall result = gson.fromJson(jsonElement, Wall.class);
                return result;
            }
            return null;
        }
    }
}