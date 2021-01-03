package game.gameObjects.tiles;

import game.Player;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import utilities.JSONProtocol.JSONMessage;
import utilities.JSONProtocol.body.Energy;

/**
 * @author Amulya
 */

public class EnergySpace extends Attribute {

    private int count; //number of energy cubes

    public EnergySpace(int count) {
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

        // First we need to check if there are any energy cube left in cell
        if (true) {
            int energy = player.getEnergyCubes();
            energy += energy;
            player.setEnergyCubes(energy);
            // Todo Decrease the energy cube number
        }
        JSONMessage jsonMessage = new JSONMessage(new Energy(player.getId(), player.getEnergyCubes()));
        userThread.sendMessage(jsonMessage);
    }

    @Override
    public Node createImage() {
        String path;
        if (count > 0) {
            path = "/tiles/energySpace_green.png";
        } else {
            path = "/tiles/energySpace_red.png";
        }

        var stream = getClass().getResourceAsStream(path);
        var image = new Image(stream, 60, 60, true, true);

        return new ImageView(image);
    }
}