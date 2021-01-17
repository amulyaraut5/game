package client.view;

import client.ViewManager;
import client.model.Client;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import utilities.JSONProtocol.body.PlayerAdded;

import java.util.ArrayList;
import java.util.LinkedList;

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

    private String imageDropped;
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


    public String getImageDropped() {
        return imageDropped;
    }

    public void setImageDropped(String imageDropped) {
        this.imageDropped = imageDropped;
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
    /**
     * This private class is a data structure to easily connect the different information
     * that are necessary to assign one user to a specific place in the lobby with his image, name etc.
     */
    protected class RobotIcon {
        private String userName;
        private int userID;
        private int position;
        private int figure;
        private ImageView robotImageView;
        private Label labelOfUser;
        private boolean thisUser;

        /**
         * the constructor of RobotIcon where one RobotIcon can be created with the
         * values of one player and the image of the figure he choosed
         *
         * @param position
         * @param playerAdded
         * @param imageViewPuffer
         * @param labelPuffer
         */
        public RobotIcon(int position, PlayerAdded playerAdded, ImageView imageViewPuffer, Label labelPuffer, boolean thisUser) {
            this.position = position;
            this.userID = playerAdded.getID();
            this.userName = playerAdded.getName() + " " + playerAdded.getID();
            this.figure = playerAdded.getFigure();
            this.robotImageView = imageViewPuffer;
            this.labelOfUser = labelPuffer;
            this.thisUser = thisUser;
        }

        public int getFigure() {
            return figure;
        }

        public String getUserName() {
            return userName;
        }

        public int getUserID() {
            return userID;
        }

        public ImageView getRobotImageView() {
            return robotImageView;
        }

        public Label getLabelOfUser() {
            return labelOfUser;
        }

        public boolean isThisUser() {
            return thisUser;
        }
    }

}
