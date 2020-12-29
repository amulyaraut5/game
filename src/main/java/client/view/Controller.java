package client.view;

import client.ViewManager;
import client.model.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Abstract super class of all view-controller
 *
 * @author simon
 */
public abstract class Controller {
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

    /**
     * This private class represents a robot with its name and id
     */
    protected class RobotPrivate {
        int id = 0;
        String name = "default";

        /**
         * constructor of RobotPrivate
         *
         * @param robotName of the robot
         * @param robotId   of the robot
         */
        public RobotPrivate(String robotName, int robotId) {
            this.id = robotId + 1;
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
