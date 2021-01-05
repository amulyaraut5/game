package utilities;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.InputStream;

public class ImageHandler {
    public static ImageView createImageView(String path) {
        InputStream stream = ImageHandler.class.getResourceAsStream(path);
        if (stream != null) {
            Image image = new Image(stream, 60, 60, true, true);
            return new ImageView(image);
        }
        return null;
    }

    public static ImageView createImageView(String path, Utilities.Orientation orientation) {
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
