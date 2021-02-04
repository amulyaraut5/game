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
    private AudioClip audioClip;


    public SoundHandler() { }

    /**
     * musicOn() plays  general soundtrack throughout the game
     * if the player wishes to play.
     */
    public void musicOn(){
        Media m = new Media(getClass().getResource("/sounds/background_music.mp3").toExternalForm());
        mediaPlayer = new MediaPlayer(m);
        mediaPlayer.play();
    }

    /**
     * musicOff() stops the music upon called.
     */
    public void musicOff(){
        mediaPlayer.stop();
    }


    public void playSoundEffects(String s, boolean play){
        if(play){
            switch(s){
                case "PitSound" ->{
                    audioClip = new AudioClip(getClass().getResource("/sounds/pit-scream.mp3").toExternalForm());
                    audioClip.play();
                }
                case "CheckPoint" -> {
                    audioClip = new AudioClip(getClass().getResource("/sounds/checkPoint.wav").toExternalForm());
                    audioClip.play();
                }
                case "Laser" -> {
                    audioClip = new AudioClip(getClass().getResource("/sounds/laser.mp3").toExternalForm());
                    audioClip.play();
                }
            }
        }
    }
}
