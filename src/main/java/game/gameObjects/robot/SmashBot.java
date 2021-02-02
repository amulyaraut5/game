package game.gameObjects.robot;

import game.Player;
import javafx.scene.paint.Color;

public class SmashBot extends Robot {

    public SmashBot(Player player) {
        super(player, "/robots/SmashBot.png", "Smash Bot", Color.YELLOW);
    }
}