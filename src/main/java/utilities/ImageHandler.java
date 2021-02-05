package utilities;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import utilities.enums.Orientation;

import java.io.InputStream;

/**
 * The ImageHandler provides methods for loading images easily and displaying them in javafx components.
 *
 * @author simon
 */
public final class ImageHandler {
    private ImageHandler() {
    }

    /**
     * Creates an ImageView from the image at given path.
     * The image size is specified by the size of tiles on the game field.
     *
     * @param path path to the desired image
     * @return A ImageView containing the image; null if no resource with this name is found.
     */
    public static ImageView createImageView(String path) {
        int size = Constants.FIELD_SIZE;
        return createImageView(path, size, size);
    }

    /**
     * Creates an ImageView from the image at given path. The image size can be specified.
     *
     * @param path   path to the desired image
     * @param width  specifies the width of the image
     * @param height specifies the height of the image
     * @return A ImageView containing the image; null if no resource with this name is found.
     */
    public static ImageView createImageView(String path, int width, int height) {
        InputStream stream = ImageHandler.class.getResourceAsStream(path);
        if (stream != null) {
            Image image = new Image(stream, width, height, true, true);
            return new ImageView(image);
        }
        return null;
    }

    /**
     * Creates an ImageView from the image at given path.
     *
     * @param path        path to the desired image
     * @param orientation An orientation, if the image from the path should also be rotated.
     * @return A ImageView containing the image; null if no resource with this name is found.
     */
    public static ImageView createImageView(String path, Orientation orientation) {
        InputStream stream = ImageHandler.class.getResourceAsStream(path);
        if (stream != null) {
            int size = Constants.FIELD_SIZE;
            var image = new Image(stream, size, size, true, true);
            var imageView = new ImageView(image);

            switch (orientation) {
                case RIGHT -> imageView.setRotate(90);
                case DOWN -> imageView.setRotate(180);
                case LEFT -> imageView.setRotate(270);
            }
            return imageView;
        }
        return null;
    }
}
