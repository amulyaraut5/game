package game.gameObjects.robot;

import game.Player;
import javafx.scene.paint.Color;

public class HammerBot extends Robot {

    public HammerBot(Player player) {
        super(player, "/robots/HammerBot.png", "Hammer Bot", Color.PURPLE);
    }
}