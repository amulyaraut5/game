package Utilities.JSONProtocol;

import Utilities.JSONProtocol.connection.HelloClient;
import Utilities.JSONProtocol.connection.HelloServer;
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
}
    /*package Utilities.JSONProtocol;

import Utilities.JSONProtocol.connection.HelloClient;
import Utilities.JSONProtocol.connection.HelloServer;
import com.google.gson.Gson;

/**
 * This abstract class is for serialization and deserialization of every message

    public class JSONMessage {

        private String messageTyp = "default";
        private static Gson gson = new Gson();

        public String serialize() {
            return gson.toJson(this);//TODO test if this works correctly for specific JSON-Messages
        }

        public static JSONMessage deserialize(String jsonString) {
            System.out.println(jsonString);
            JSONMessage msg = gson.fromJson(jsonString, JSONMessage.class);

            String type = msg.getType();

            JSONMessage concreteMessage;

            switch (type) {
                case "HelloClient":
                    concreteMessage = gson.fromJson(jsonString, HelloClient.class);
                    break;
                case "HelloServer":
                    concreteMessage = gson.fromJson(jsonString, HelloServer.class);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + type);
            }
            return concreteMessage;
        }

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
    }*/

}