package view;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;

public class Controller {
    public static void main (String [] args){

    }
    public  Button playButton;
    public  void handlePlayButton(){
        System.out.println("play button clicked");
        playButton.setTextFill(Color.GRAY);
        playButton.setText("Have Fun!");


    }
}
