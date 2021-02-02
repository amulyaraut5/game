package game.gameObjects.robot;

import game.Player;
import javafx.scene.paint.Color;

public class SpinBot extends Robot {

    public SpinBot(Player player) {
        super(player, "/robots/SpinBot.png", "Spin Bot", Color.BLUE);
    }
}