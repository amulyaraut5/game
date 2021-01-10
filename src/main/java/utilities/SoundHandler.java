package utilities;

import game.gameObjects.maps.MapFactory;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.nio.file.Paths;


public class SoundHandler{

    private MediaPlayer mediaPlayer;

    public SoundHandler() {
    }

    public void musicOn(){
        //String s = "C:\\Users\\Amulya\\IdeaProjects\\vp-astreine-akazien\\src\\main\\resources\\sounds\\robotDance.mp3";
        //Media m = new Media(Paths.get(s).toUri().toString());
        Media m = new Media(getClass().getResource("/sounds/robotDance.mp3").toExternalForm());
        mediaPlayer = new MediaPlayer(m);
        mediaPlayer.play();

    }
    public void musicOff(){
        mediaPlayer.stop();
    }
}
