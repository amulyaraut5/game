package utilities;

import javafx.scene.media.AudioClip;
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
        Media m = new Media(getClass().getResource("/sounds/background_music.mp3").toExternalForm());
        mediaPlayer = new MediaPlayer(m);
        mediaPlayer.play();
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
    }

    /**
     * musicOff() stops the music upon called.
     */
    public void musicOff(){
        mediaPlayer.stop();
    }

    /**
     * This method gets triggered during laserAction.
     */
    public void laserSound(){

    }

    /**
     * This method gets triggered if the player falls in the pit.
     */
    public void pitSound(){
        AudioClip audioClip = new AudioClip(getClass().getResource("/sounds/pitSound.wav").toExternalForm());
        audioClip.play();
    }

    /**
     * This method gets triggered if the player reaches any one of checkpoint..
     */
    public void checkPoint(){
        AudioClip audioClip = new AudioClip(getClass().getResource("/sounds/checkPoint.wav").toExternalForm());
        audioClip.play();
    }

    /**
     * This method gets triggered if the player reaches final checkpoint..
     */
    public void victorySound(){
        AudioClip audioClip = new AudioClip(getClass().getResource("/sounds/checkPoint.wav").toExternalForm());
        audioClip.play();
    }
}
