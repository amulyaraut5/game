package game.gameObjects.tiles;

import game.Player;

public class Empty extends Attribute {

    public Empty() {
        this.type = "Empty";
    }


    @Override
    public void performAction(Player player) {
        // No functionality
    }
}
