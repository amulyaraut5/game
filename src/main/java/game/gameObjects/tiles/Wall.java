package game.gameObjects.tiles;

import game.Player;
import utilities.Utilities.Orientation;

/**
 * @author Amulya
 */

public class Wall extends Attribute {

    private Orientation orientation;
    private Orientation orientation1;

    /**
     * Constructor for wall for those tiles having only one wall facing in one direction.
     * @param orientation
     */
    Wall(Orientation orientation){
        this.orientation = orientation;
    }

    /**
     * Constructor for Wall for those tiles which have  two walls in two different direction.
     * @param orientation
     * @param orientation1
     */
    Wall(Orientation orientation,Orientation orientation1){
        this.orientation = orientation;
        this.orientation1 = orientation1;
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

