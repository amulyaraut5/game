package client.view;

import client.ViewManager;
import client.model.Client;

/**
 * Abstract super class of all view-controller
 *
 * @author simon, sarah
 */
public abstract class Controller {
    public static String imageDropped;
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

    public String getImageDropped() {
        return imageDropped;
    }

    public void setImageDropped(String imageDropped) {
        //if(imageDropped!= null){
        String[] a = imageDropped.split("/");
        String imageName = a[a.length - 1];
        String cardName = imageName.substring(0, imageName.length() - 9);
        this.imageDropped = cardName;
        //}
    }

    /**
     * This private class represents a robot with its name and id
     */
    protected class RobotPrivate {
        private int id = 0;
        private String name = "default";

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
