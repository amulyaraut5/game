package client.view;

import client.ViewManager;
import client.model.Client;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import utilities.enums.CardType;

import java.util.ArrayList;

/**
 * Abstract super class of all view-controller
 *
 * @author simon, sarah
 */
public abstract class Controller {

    protected static DataFormat cardFormat = new DataFormat("com.example.myapp.formats.button");
    private static int positionRegister;
    private static ImageView programmingImageView ;

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

    private static boolean wasFormerRegister = false;

    public  boolean getWasFormerRegister() {
        return wasFormerRegister;
    }

    public  void setWasFormerRegister(boolean wasFormerRegister) {
        Controller.wasFormerRegister = wasFormerRegister;
    }

    protected CardType generateCardType(String imageDropped) {
        String[] a = imageDropped.split("/");
        String imageName = a[a.length - 1];
        String cardName = imageName.substring(0, imageName.length() - 9);
        CardType cardType = CardType.valueOf(cardName);
        return cardType;
    }


    public ImageView getProgrammingImageView() {
        return programmingImageView;
    }

    public void setProgrammingImageView(ImageView programmingImageView) {
        this.programmingImageView = programmingImageView;
    }

    protected int getPosition(){
        return positionRegister;
    }
    protected void setPosition(int position){
        this.positionRegister = position;
    }




    /**
     * This private class represents a robot with its name and id
     */
    protected class RobotPrivate {
        private int id;
        private String name;

        /**
         * constructor of RobotPrivate
         *
         * @param robotName of the robot
         * @param robotId   of the robot
         */
        public RobotPrivate(String robotName, int robotId) {
            this.id = robotId;
            this.name = robotName;
        }

        protected String getRobotName() {
            return this.name;
        }

        protected int getRobotID() {
            return this.id;
        }
    }

}
