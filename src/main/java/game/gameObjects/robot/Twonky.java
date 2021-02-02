package game.gameObjects.robot;

import game.Player;
import javafx.scene.paint.Color;

public class Twonky extends Robot {

    public Twonky(Player player) {
        super(player, "/robots/Twonky.png", "Twonky", Color.ORANGE);
    }
}