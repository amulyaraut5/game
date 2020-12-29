package client.view;

import client.Main;
import client.ViewManager;
import client.model.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.Server;

import static utilities.Utilities.PORT;

public class MenuController {
    private static final Logger logger = LogManager.getLogger();
    private ViewManager viewManager = ViewManager.getInstance();

    @FXML
    public void joinGameClicked(ActionEvent event) {
        logger.info("Join Game Clicked");

        Client client = new Client("localhost", PORT);
        viewManager.setClient(client);
        viewManager.nextScene();
    }

    @FXML
    public void hostGameClicked(ActionEvent event) {
        logger.info("Host Game Clicked");

        //TODO test if a server already exists on localhost? or print out that the user only joined
        Thread t = new Thread(() -> {
            Server server = new Server(PORT);
            server.start();
        });
        t.setName("Server Thread");
        t.start();

        Client client = new Client("localhost", PORT);
        viewManager.setClient(client);
        viewManager.nextScene();
    }
}