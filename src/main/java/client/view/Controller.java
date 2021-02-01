package client.view;

import client.ViewManager;
import client.model.Client;
import javafx.scene.image.ImageView;
import javafx.scene.input.DataFormat;
import utilities.enums.CardType;

/**
 * Abstract super class of all view-controller
 *
 * @author simon, sarah
 */
public abstract class Controller {

    protected static DataFormat cardFormat = new DataFormat("programmingCard");
    private static int positionRegister;
    private static ImageView programmingImageView;
    private static boolean wasFormerRegister = false;

    protected ViewManager viewManager = ViewManager.getInstance();
    protected Client client = Client.getInstance();

    protected String[] robotNames = {"hulkX90", "hammerbot", "smashbot",
            "twonky", "spinbot", "zoombot"};

    public boolean getWasFormerRegister() {
        return wasFormerRegister;
    }

    public void setWasFormerRegister(boolean wasFormerRegister) {
        Controller.wasFormerRegister = wasFormerRegister;
    }

    protected CardType generateCardType(String imageDropped) {
        String[] a = imageDropped.split("/");
        String imageName = a[a.length - 1];
        String cardName = imageName.substring(0, imageName.length() - 9);
        return CardType.valueOf(cardName);
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
