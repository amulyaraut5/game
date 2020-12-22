package game.gameObjects.tiles;

import game.Player;

public class Empty extends Attribute {

    Empty(){
        this.name = "Empty Attribute";
    }


    @Override
    public void performAction(Player player) {
        // No functionality
    }
}
