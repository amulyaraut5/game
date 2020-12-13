package json;

import com.google.gson.Gson;
import com.google.gson.JsonObject;


/**
 * This class contains the JSON commands send between client and server
 */
public class Commands {

        /**
         * creates a Message JSON object
         */
        public static JsonObject Message(Message textMessage) {
            JsonObject jmsg = new JsonObject();
            JsonObject jsubmsg = new JsonObject();
            Gson gson = new Gson();

            try {
                String strTextMessage = gson.toJson(textMessage); // serializes textMessage to Json
                jsubmsg.addProperty("Message", strTextMessage);
                jmsg.add("TextMessage", jsubmsg);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return jmsg;
        }

}
