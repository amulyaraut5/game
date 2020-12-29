package game.gameObjects.tiles;

import game.Player;

/**
 * @author Amulya
 */

public class EnergySpace extends Attribute {

    // Count attribute to count the energy cubes
    private int count;

    EnergySpace(int count) {
        this.count = count;
        this.type = "EnergySpace";
    }

    /**
     * Whenever the player finds in this tile, player gets the energy token.
     *
     * @param player The player that is positioned on the tile
     */

    @Override
    public void performAction(Player player) {
        // player.increaseEnergyCube();
    }
}