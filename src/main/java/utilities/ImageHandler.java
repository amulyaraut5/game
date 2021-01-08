package utilities;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.InputStream;

/**
 * The ImageHandler provides methods for loading images easily and displaying them in javafx components.
 *
 * @author simon
 */
public class ImageHandler {
    /**
     * Creates an ImageView from the image at given path.
     *
     * @param path path to the desired image
     * @return A ImageView containing the image; null if no resource with this name is found.
     */
    public static ImageView createImageView(String path) {
        InputStream stream = ImageHandler.class.getResourceAsStream(path);
        if (stream != null) {
            Image image = new Image(stream, 60, 60, true, true);
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
            var image = new Image(stream, 60, 60, true, true);
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
