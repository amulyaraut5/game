package game.gameObjects.tiles;

import game.Player;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import utilities.Utilities.Orientation;


/**
 * @author Amulya
 */

public class Laser extends Attribute {

    private Orientation orientation; //firing direction
    private int count; // number of lasers


    public Laser(Orientation orientation, int count) {
        this.orientation = orientation;
        this.count = count;
        this.type = "Laser";
    }

    /**
     * The effect of Laser is handled separately in Laser class,
     * as the laser should not only fire when the robot steps on laser tile
     * but also at every instance after every register.
     * The robot can get hit at any tile as long as it is in the range of laser.
     *
     * @param player
     */
    @Override
    public void performAction(Player player) {
        // Read javadoc
    }

    @Override
    public Node createImage() {
        String path = "/tiles/laser/laser_" + count + "a.png";

        var stream = getClass().getResourceAsStream(path);
        var image = new Image(stream, 60, 60, true, true);
        var imageView = new ImageView(image);
        switch(orientation){
            case RIGHT -> imageView.setRotate(90);
            case DOWN -> imageView.setRotate(180);
            case LEFT -> imageView.setRotate(270);
        }

        return new ImageView(image);
    }
}