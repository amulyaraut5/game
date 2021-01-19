package client.view;

import client.ViewManager;
import client.model.Client;
import game.Player;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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

    public static String imageDropped;

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
        //if(imageDropped!= null){
            String [] a = imageDropped.split("/");
            String imageName = a[a.length-1];
            String cardName = imageName.substring(0, imageName.length()-9);
            this.imageDropped = cardName;
        //}
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
         * @param player
         * @param imageViewPuffer
         * @param labelPuffer
         */
        public RobotIcon(int position, Player player, ImageView imageViewPuffer, Label labelPuffer, boolean thisUser) {
            this.position = position;
            this.userID = player.getID();
            this.userName = player.getName() + " " + player.getID();
            this.figure = player.getFigure();
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
