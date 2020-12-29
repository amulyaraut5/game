package game.gameObjects.tiles;

import game.Player;
import utilities.Utilities.Orientation;

/**
 * @author Amulya
 */

public class Wall extends Attribute {

    /**
     * Constructor for wall for those tiles having only one wall facing in one direction.
     * @param orientation
     */
    Wall(Orientation orientation){
        this.orientation = orientation;
    }

    /**
     * Constructor for Wall for those tiles which have  two walls in two different direction.
     * @param orientations saves the x and y orientations of the wall
     */
    Wall(Orientation[] orientations){
        this.orientations = orientations;
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

