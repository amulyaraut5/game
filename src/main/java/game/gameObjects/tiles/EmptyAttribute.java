package game.gameObjects.tiles;

import game.Player;

public class EmptyAttribute extends Attribute {

    EmptyAttribute(){
        this.name = "Empty Attribute";
    }


    @Override
    public void performAction(Player player) {
        // No functionality
    }
}
