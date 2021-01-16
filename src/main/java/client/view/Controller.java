package client.view;

import client.ViewManager;
import client.model.Client;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import utilities.JSONProtocol.body.PlayerAdded;

import java.util.ArrayList;

/**
 * Abstract super class of all view-controller
 *
 * @author simon, sarah
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

    protected ArrayList<RobotIcon> playersAdded = new ArrayList<>();

    public ArrayList<RobotIcon> getPlayersAdded() {
        return playersAdded;
    }

    public void addRobotIcon(RobotIcon robotIcon) {
       playersAdded.add(robotIcon);
    }




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
