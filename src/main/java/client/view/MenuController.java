package client.view;

import client.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class MenuController {
    private Main main;

    @FXML
    public void joinGameClicked(ActionEvent event) {
        main.constructLoginStage();
    }

    @FXML
    public void hostGameClicked(ActionEvent event) {
        //Server server = new Server(PORT);
        //server.start();

        main.constructLoginStage();
    }

    public void setMain(Main main) {
        this.main = main;
    }
}