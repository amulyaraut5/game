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

    protected static DataFormat cardFormat = new DataFormat("com.example.myapp.formats.button");
    private static int positionRegister;
    private static ImageView programmingImageView;
    private static boolean wasFormerRegister = false;
    /**
     * instance of the ViewManager to call the next or previous scene
     */
    protected ViewManager viewManager = ViewManager.getInstance();
    /**
     * instance of the client to transfer inputs
     */
    protected Client client = Client.getInstance();
    /**
     * it stores a list of names of the robots
     */

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

    /**
     * This private class represents a robot with its name and id
     */
    protected class RobotPrivate {
        private final int id;
        private final String name;

        /**
         * constructor of RobotPrivate
         *
         * @param robotName of the robot
         * @param robotId   of the robot
         */
        public RobotPrivate(String robotName, int robotId) {
            id = robotId;
            name = robotName;
        }

        protected String getRobotName() {
            return name;
        }

        protected int getRobotID() {
            return id;
        }
    }
}
