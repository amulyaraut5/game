package game.gameObjects.tiles;

import game.Player;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import utilities.Utilities.Orientation;

/**
 * @author Amulya
 */

public class Wall extends Attribute {

    private Orientation orientation;
    private Orientation[] orientations;

    /**
     * Constructor for wall for those tiles having only one wall facing in one direction.
     *
     * @param orientation
     */
    public Wall(Orientation orientation) {
        this.orientation = orientation;
        this.type = "Wall";
    }

    /**
     * Constructor for Wall for those tiles which have  two walls in two different direction.
     *
     * @param orientations saves the x and y orientations of the wall
     */
    public Wall(Orientation[] orientations) {
        this.orientations = orientations;
        this.type = "Wall";
    }

    /**
     * Wall itself does not have any functionality but robots cannot
     * move through walls and board lasers cannot pass through walls.
     *
     * @param player
     */
    @Override
    public void performAction(Player player) {
        // Does nothing
    }

    /**
     * @return
     */
    @Override
    public Node createImage() {
        String pathOneWall = "/tiles/wall_up.png";
        String pathTwoWalls = "/tiles/wall_up_right.png";

        if (orientation != null) {

            var stream = getClass().getResourceAsStream(pathOneWall);
            var image = new Image(stream, 60, 60, true, true);
            var imageView = new ImageView(image);

            switch (orientation) {
                case RIGHT -> imageView.setRotate(90);
                case DOWN -> imageView.setRotate(180);
                case LEFT -> imageView.setRotate(270);
            }
            return imageView;

        } else {
            if (orientations != null) {
                var stream = getClass().getResourceAsStream(pathTwoWalls);
                var image = new Image(stream, 60, 60, true, true);
                Node imageView = new ImageView(image);

                switch (orientations[0]) {
                    case UP: {
                        switch (orientations[1]) {
                            case DOWN -> imageView = createParallelImage(Orientation.DOWN);
                            case LEFT -> imageView.setRotate(270);
                        }
                        break;
                    }
                    case RIGHT: {
                        switch (orientations[1]) {
                            case DOWN -> imageView.setRotate(90);
                            case LEFT -> imageView = createParallelImage(Orientation.LEFT);
                        }
                        break;
                    }
                    case DOWN: {
                        switch (orientations[1]) {
                            case UP -> imageView = createParallelImage(Orientation.UP);
                            case RIGHT -> imageView.setRotate(90);
                            case LEFT -> imageView.setRotate(180);
                        }
                        break;
                    }
                    case LEFT: {
                        switch (orientations[1]) {
                            case UP -> imageView.setRotate(270);
                            case RIGHT -> imageView = createParallelImage(Orientation.RIGHT);
                            case DOWN -> imageView.setRotate(180);
                        }
                        break;
                    }
                }
                return imageView;
            }
        }
        return null;
    }

    private Group createParallelImage(Orientation o) {
        var stream = getClass().getResourceAsStream("/tiles/wall_up.png");
        var image = new Image(stream, 60, 60, true, true);
        var imageView1 = new ImageView(image);
        var imageView2 = new ImageView(image);

        if (o == Orientation.UP || o == Orientation.DOWN) {
            imageView2.setRotate(180);
        } else {
            imageView1.setRotate(90);
            imageView2.setRotate(270);
        }

        return new Group(imageView1, imageView2);
    }
}

