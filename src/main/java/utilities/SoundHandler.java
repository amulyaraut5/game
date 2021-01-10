package utilities;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * This class loads different soundtracks from the resources and plays the music
 * based on different situations.
 * @author Amulya
 */
public class SoundHandler{

    private MediaPlayer mediaPlayer;

    public SoundHandler() {
    }

    /**
     * musicOn() plays  general soundtrack throughout the game
     * if the player wishes to play.
     */
    public void musicOn(){
        //String s = "C:\\Users\\Amulya\\IdeaProjects\\vp-astreine-akazien\\src\\main\\resources\\sounds\\robotDance.mp3";
        //Media m = new Media(Paths.get(s).toUri().toString());
        Media m = new Media(getClass().getResource("/sounds/robotDance.mp3").toExternalForm());
        mediaPlayer = new MediaPlayer(m);
        mediaPlayer.play();

    }

    /**
     * musicOff() stops the music upon called.
     */
    public void musicOff(){
        mediaPlayer.stop();
    }
}
