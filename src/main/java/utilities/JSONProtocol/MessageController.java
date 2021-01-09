package utilities.JSONProtocol;

import server.Server;
import utilities.JSONProtocol.body.HelloClient;
import utilities.JSONProtocol.body.SelectionFinished;

import java.io.PrintWriter;

public class MessageController {
    private static MessageController instance;
    private PrintWriter writer;

    public static MessageController getInstance() {
        if (instance == null) instance = new MessageController();
        return instance;
    }

    public void sendMessage(JSONMessage msg) {

        String json = Multiplex.serialize(msg);
        //logger.debug("Protocol sent:" + json);
        writer.println(json);
        writer.flush();
    }

        public void sendSelectionFinished (int playerID) {
            JSONMessage selectionFinished = new JSONMessage(new SelectionFinished(playerID));
            Server.getInstance().communicateAll(selectionFinished);
        }

        public void sendHelloClient (double protocol) {
            JSONMessage jsonMessage = new JSONMessage(new HelloClient(protocol));
            sendMessage(jsonMessage);
        }
}
