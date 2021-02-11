package client.view;

import game.Player;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import utilities.ImageHandler;
import utilities.JSONProtocol.JSONMessage;
import utilities.JSONProtocol.body.Error;
import utilities.JSONProtocol.body.PlayerValues;
import utilities.Updatable;
import utilities.enums.MessageType;

/**
 * This class controls the loginView.fxml view, it takes the name and the chosen robot, sends it to
 * client and switches to the game view
 *
 * @author sarah,
 */
public class LoginController extends Controller implements Updatable {
    /**
     * It stores the imageViews of the different robots,
     * so that name and id from the chosen robot
     * can be recognized.
     */
    private final ObservableList<Figure> figures = FXCollections.observableArrayList();

    /**
     * The player writes his username in this TextField.
     */
    @FXML
    private TextField textUserName;
    /**
     * A label to check if everything works. //TODO delete or change purpose
     */
    @FXML
    private Label infoLabel;
    /**
     * The listView for choosing one robot, it stores different ImageViews.
     */
    @FXML
    private ListView<Figure> listView;

    /**
     * By initializing the view the listView gets filled with the imageViews of the robots and
     * it makes sure that only one item of the listView can get clicked.
     * And that the awarded figures cannot be picked.
     */
    public void initialize() {
        createRobotList();
        listView.setItems(figures);
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        listView.setCellFactory(listCell -> new ListCell<>() {
            @Override
            public void updateItem(Figure figure, boolean empty) {
                super.updateItem(figure, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setGraphic(figure.getImageView());
                    figure.getImageView().setDisable(figure.isTaken());
                    setDisable(figure.isTaken());
                    if (figure.isTaken()) setBackground(new Background(new BackgroundFill(Color.GRAY,
                            CornerRadii.EMPTY, Insets.EMPTY)));
                    else {
                        setBackground(new Background(new BackgroundFill(Color.WHITE,
                                CornerRadii.EMPTY, Insets.EMPTY)));
                    }
                }
            }
        });
    }

    /**
     * This method creates a list with the robots and additional it
     * adds imageViews for each robot to another list.
     */
    private void createRobotList() {
        ImageView robot;
        int scaleSize = 65;
        for (String robotName : robotNames) {
            String path = "/lobby/" + robotName + ".png";
            robot = ImageHandler.createImageView(path, scaleSize, scaleSize);

            figures.add(new Figure(robot));
        }
    }

    /**
     * This method gets called by clicking on the 'Login' button, it checks if the username is
     * valid and if a robot is selected and then it sends a PlayerValues protocol message
     * and switches to the gameStage.
     */
    @FXML
    private void fxButtonClicked() {
        String userName = textUserName.getText();
        int chosenRobot = listView.getSelectionModel().getSelectedIndex();

        if (userName.isBlank()) Updatable.showInfo(infoLabel, "Please insert a Username!");
        else if (chosenRobot < 0) Updatable.showInfo(infoLabel, "You have to choose a robot!");
        else viewClient.sendMessage(new PlayerValues(userName, chosenRobot));
    }

    /**
     * This method makes a figure ineligible if it has already been taken by the added player.
     *
     * @param playerID the id of the player which gets added
     * @param id       the id of the figure the player chose
     */
    public void setFigureTaken(int playerID, int id) {
        Figure figure = figures.get(id);
        figure.setTaken(true);
        figure.setPlayerID(playerID);
        figures.set(id, figure);
    }

    /**
     * This method is called when a player leaves the game. His figure is then set to re-selectable.
     *
     * @param player who has been removed
     */
    public void removePlayer(Player player) {
        for (Figure figure : figures) {
            if (player != null) {
                if (figure.getPlayerID() == player.getID()) {
                    figure.setTaken(false);
                    int index = figures.indexOf(figure);
                    figures.set(index, figure);
                }
            }
        }
    }

    /**
     * This message gets errors related to login and displays them for a hort amount of timer in the infoLabel.
     *
     * @param message which is to be handled specifically in the LoginController
     */
    @Override
    public void update(JSONMessage message) {
        if (message.getType() == MessageType.Error) {
            Error error = (Error) message.getBody();
            Updatable.showInfo(infoLabel, error.getError());
        }
    }

    /**
     * This private class is used to store the imageView of the figure and if it's taken or not.
     * And if it's taken also the player which chose the figure.
     */
    private static class Figure {
        private final ImageView imageView;
        private boolean taken = false;
        private int playerID;

        /**
         * The constructor of the private class sets the ImageView with an image of the robot
         *
         * @param imageView with the robot image
         */
        public Figure(ImageView imageView) {
            this.imageView = imageView;
        }

        /**
         * This method returns if the figure is already taken from a player.
         *
         * @return if the figure is taken
         */
        public boolean isTaken() {
            return taken;
        }

        /**
         * This method sets if a figure is already taken, e.g. by joining or exiting.
         *
         * @param taken if the figure is taken
         */
        public void setTaken(boolean taken) {
            this.taken = taken;
        }

        /**
         * This method returns the imageView of the figure
         *
         * @return the imageView
         */
        public ImageView getImageView() {
            return imageView;
        }

        /**
         * This method returns the name of the player who took the figure.
         *
         * @return player ID
         */
        public int getPlayerID() {
            return playerID;
        }

        /**
         * This method sets the player ID of a player who took the figure.
         *
         * @param playerID of the player who took a figure
         */
        public void setPlayerID(int playerID) {
            this.playerID = playerID;
        }
    }
}