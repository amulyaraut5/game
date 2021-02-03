package game.gameObjects.tiles;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import utilities.ImageHandler;
import utilities.enums.AttributeType;
import utilities.enums.Orientation;

/**
 * @author Simon
 */

public class Wall extends Attribute {

    private final Orientation[] orientations;

    /**
     * Constructor for wall for those tiles having only one wall facing in one direction.
     *
     * @param orientation
     */
    public Wall(Orientation orientation) {
        orientations = new Orientation[]{orientation};
        type = AttributeType.Wall;
    }

    /**
     * Constructor for Wall for those tiles which have two walls in two different direction.
     *
     * @param orientations saves the x and y orientations of the wall
     */
    public Wall(Orientation[] orientations) {
        this.orientations = orientations;
        type = AttributeType.Wall;
    }

    /**
     * @return
     */
    @Override
    public Node createImage() {
        String pathOneWall = "/tiles/wall_up.png";
        String pathTwoWalls = "/tiles/wall_up_right.png";

        if (orientations != null) {
            if (orientations.length == 1) {
                return ImageHandler.createImageView(pathOneWall, orientations[0]);
            } else if (orientations.length >= 2) {
                Node imageView = ImageHandler.createImageView(pathTwoWalls);
                assert imageView != null;

                switch (orientations[0]) {
                    case UP -> {
                        switch (orientations[1]) {
                            case DOWN -> imageView = createParallelImage(Orientation.DOWN);
                            case LEFT -> imageView.setRotate(270);
                        }
                    }
                    case RIGHT -> {
                        switch (orientations[1]) {
                            case DOWN -> imageView.setRotate(90);
                            case LEFT -> imageView = createParallelImage(Orientation.LEFT);
                        }
                    }
                    case DOWN -> {
                        switch (orientations[1]) {
                            case UP -> imageView = createParallelImage(Orientation.UP);
                            case RIGHT -> imageView.setRotate(90);
                            case LEFT -> imageView.setRotate(180);
                        }
                    }
                    case LEFT -> {
                        switch (orientations[1]) {
                            case UP -> imageView.setRotate(270);
                            case RIGHT -> imageView = createParallelImage(Orientation.RIGHT);
                            case DOWN -> imageView.setRotate(180);
                        }
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

    public Orientation[] getOrientations() {
        return orientations;
    }
}

