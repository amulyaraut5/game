package game.gameObjects.robot;

import game.Player;
import javafx.scene.paint.Color;

public class ZoomBot extends Robot {

    public ZoomBot(Player player) {
        super(player, "/robots/ZoomBot.png", "Zoom Bot", Color.GREEN);
    }
}