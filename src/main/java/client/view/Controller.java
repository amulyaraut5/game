package client.view;

import client.ViewManager;
import client.model.ViewClient;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DataFormat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.Constants;
import utilities.enums.CardType;

import java.util.ArrayList;

/**
 * Abstract super class of all view-controller
 *
 * @author simon, sarah
 */
public abstract class Controller {
    protected static final DataFormat cardFormat = new DataFormat("programmingCard");
    private static final Logger logger = LogManager.getLogger();

    private static int positionRegister;
    private static ImageView programmingImageView;
    private static boolean wasFormerRegister = false;

    protected final ViewManager viewManager = ViewManager.getInstance();
    protected final ViewClient client = ViewClient.getInstance();
    protected final String[] robotNames = {"hulkX90", "hammerbot", "smashbot",
            "twonky", "spinbot", "zoombot"};

    public boolean getWasFormerRegister() {
        return wasFormerRegister;
    }

    public void setWasFormerRegister(boolean wasFormerRegister) {
        Controller.wasFormerRegister = wasFormerRegister;
    }


    protected ImageView generateImageView(String path, int width, int height) {
        ImageView imageView = new ImageView(new Image(getClass().getResource(path).toString()));
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        return imageView;
    }

    protected CardType generateCardType(String imageDropped) {
        String[] a = imageDropped.split("/");
        String imageName = a[a.length - 1];
        return CardType.valueOf(imageName.substring(0, imageName.length() - 9));
    }

    public ImageView getProgrammingImageView() {
        return programmingImageView;
    }

    public void setProgrammingImageView(ImageView programmingImageView) {
        Controller.programmingImageView = programmingImageView;
    }

    protected int getPosition() {
        return positionRegister;
    }

    protected void setPosition(int position) {
        positionRegister = position;
    }
}
