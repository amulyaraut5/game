package game.gameObjects.tiles;

import game.Player;
import game.gameObjects.Utilities;

/**
 * @author Amulya
 */

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

    /**
     * Wall itself does not have any functionality but robots cannot
     * move through walls and board lasers cannot pass through walls.
     * @param player
     */
    @Override
    public void performAction(Player player) {
        // Does nothing
    }
}

