package game.gameObjects.robot;

import game.Player;
import javafx.scene.paint.Color;

public class Hulk extends Robot {

    public Hulk(Player player) {
        super(player, "/robots/HulkX90.png", "Hulk x90", Color.RED);
    }
}