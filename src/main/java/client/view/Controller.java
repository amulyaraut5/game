package client.view;

import client.ViewManager;
import client.model.ViewClient;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DataFormat;
import utilities.enums.CardType;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Abstract super class of all view-controller, which also stores information and required attributes
 * for dragging and dropping between ProgrammingController and PlayerMatController
 *
 * @author simon, sarah
 */
public abstract class Controller {
    /**
     * the DataFormat which is used by creating ClipboardContent
     */
    protected static final DataFormat cardFormat = new DataFormat("programmingCard");

    /**
     * the position of one card in the registers (1 to 5)
     */
    private static int positionRegister;
    /**
     * the imageView gets used to save the imageView which gets dragged
     */
    private static ImageView programmingImageView;

    /**
     * it gets saved if card was on a register before dragging it to notice if sending null to server is required
     */
    private static boolean wasFormerRegister = false;

    /**
     * the instance of viewManager which coordinates different views
     */
    protected final ViewManager viewManager = ViewManager.getInstance();

    /**
     * the instance of the client of the player
     */
    protected final ViewClient viewClient = ViewClient.getInstance();

    /**
     * all available robot names which are also useful for creating the image and choosing a robot
     */
    protected final String[] robotNames = {"hulkX90", "hammerbot", "smashbot",
            "twonky", "spinbot", "zoombot"};

    /**
     * this method is useful for childclasses to quickly generate an ImageView
     *
     * @param path   the path of the image
     * @param width  the width the imageView should get
     * @param height the height the imageView should get
     * @return an ImageView created with given parameters
     */
    protected ImageView generateImageView(String path, int width, int height) {
        ImageView imageView = new ImageView(new Image(getClass().getResource(path).toString()));
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        return imageView;
    }

    /**
     * this method takes an url of an image and extracts the CardType
     *
     * @param imageDropped the url of an image
     * @return the CardType the image of a card represents
     */
    protected CardType extractCardType(String imageDropped) {
        String[] a = imageDropped.split("/");
        String imageName = a[a.length - 1];
        return CardType.valueOf(imageName.substring(0, imageName.length() - 9));
    }

    /**
     * this method returns the attribute wasFormerRegister
     *
     * @return if a card which gets dragged was in a register before dragging or not
     */
    public boolean getWasFormerRegister() {
        return wasFormerRegister;
    }

    /**
     * this method sets the attribute wasFormerRegister
     *
     * @param wasFormerRegister if a card was in a register before dragging
     */
    public void setWasFormerRegister(boolean wasFormerRegister) {
        Controller.wasFormerRegister = wasFormerRegister;
    }

    /**
     * this method returns the position of the dragged card
     *
     * @return the position of a register of the dragged card
     */
    protected int getRegisterPosition() {
        return positionRegister;
    }

    /**
     * this method saves a position
     *
     * @param registerPosition of a card
     */
    protected void setRegisterPosition(int registerPosition) {
        positionRegister = registerPosition;
    }

    /**
     * this method returns the programmingImageView
     *
     * @return the imageView which gets dragged and then dropped
     */
    public ImageView getProgrammingImageView() {
        return programmingImageView;
    }

    /**
     * this method sets an imageView
     *
     * @param programmingImageView the programmingImageView which gets saved in this class
     */
    public void setProgrammingImageView(ImageView programmingImageView) {
        Controller.programmingImageView = programmingImageView;
    }
}
