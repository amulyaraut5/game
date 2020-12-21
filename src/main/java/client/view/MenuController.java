package client.view;

import client.Main;

public class MenuController {
    private Main main;

    public void joinGameClicked(){
        main.constructLoginStage();
    }

    public void setMain(Main main) {
        this.main = main;
    }
}