package game.gameObjects.tiles;

import game.Player;
import game.gameObjects.Utilities;

public class Wall extends Attribute {
    private Utilities.Direction direction,direction2;


    /**
     * Constructor for wall for those tiles having only one wall facing in one direction.
     * @param direction
     */
    Wall(Utilities.Direction direction){
        this.direction = direction;
    }

    /**
     * Constructor for Wall for those tiles which have  two walls in two different direction.
     * @param direction
     * @param direction2
     */
    Wall(Utilities.Direction direction,Utilities.Direction direction2){
        this.direction = direction;
        this.direction2 = direction2;
    }

    @Override
    public void performAction(Player player) {
        // Does nothing
    }
}

