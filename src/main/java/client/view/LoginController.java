package client.view;

import com.jfoenix.controls.JFXTextField;
import game.Player;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    private static final Logger logger = LogManager.getLogger();
    /**
     * it stores the imageViews of the different robots,
     * so that name and id from the chosen robot
     * can be recognized
     */
    private final ObservableList<Figure> figures = FXCollections.observableArrayList();

    @FXML
    private TextField textUserName;
    /**
     * a label to check if everything works //TODO delete or change purpose
     */
    @FXML
    private Label infoLabel;
    /**
     * the button for checking whether input is valid
     */
    @FXML
    private Button okButton;
    /**
     * the listView for choosing one robot, it stores different ImageViews
     */
    @FXML
    private ListView<Figure> listView;

    /**
     * by initializing the view the listView gets filled with the imageViews of the robots and
     * it makes sure that only one item of the listView can get clicked
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
                    else{
                        setBackground(new Background(new BackgroundFill(Color.WHITE,
                                CornerRadii.EMPTY, Insets.EMPTY)));
                    }
                }
               /*styleProperty().bind(Bindings.when(hoverProperty())
                        .then("-fx-background-color: midnightblue; -fx-border-color: midnightblue")
                        .otherwise("-fx-background-color: transparent; -fx-border-color: transparent"));*/
            }
        });
    }

    /**
     * This method creates a list with the robots and additional it
     * adds imageViews for each robot to another list
     */
    private void createRobotList() {
        ImageView robot;
        double scaleSize = 50;
        for (String robotName : robotNames) {
            String path = "/lobby/" + robotName + ".png";
            robot = ImageHandler.createImageView(path, 50, 50);

            figures.add(new Figure(robot));
        }
    }

    /**
     * This method gets called by clicking on the button, it checks if the username is
     * valid and if a robot is selected and then it sends a PlayerValues protocol message
     * and switches to the gameStage
     */
    @FXML
    private void fxButtonClicked() {
        infoLabel.setText("");
        String userName = textUserName.getText();
        int chosenRobot = listView.getSelectionModel().getSelectedIndex();

        if (userName.isBlank()) infoLabel.setText("Please insert a Username!");
        else if (chosenRobot < 0) infoLabel.setText("You have to choose a robot!");
        else client.sendMessage(new PlayerValues(userName, chosenRobot));
    }

    public void setFigureTaken(int playerID, int id, boolean taken) {
        Figure figure = figures.get(id);
        figure.setTaken(taken);
        figure.setPlayerID(playerID);
        figures.set(id, figure);
    }

    public void removePlayer(Player player) {
        for (Figure figure : figures) {
            if (figure.getPlayerID() == player.getID()) {
                figure.setTaken(false);
                int index = figures.indexOf(figure);
                figures.set(index, figure);
            }
        }
    }

    public void ignorePlayer(Player player) {
        for (Figure figure : figures) {
            if (figure.getPlayerID() == player.getID())
                figure.getImageView().setImage(new Image(getClass().getResource("/cards/programming/backside-card.png").toString()));
        }
    }

    @Override
    public void update(JSONMessage message) {
        if (message.getType() == MessageType.Error) {
            Error error = (Error) message.getBody();
            infoLabel.setText(error.getError());
        }
    }

    private static class Figure {
        private final ImageView imageView;
        private boolean taken = false;
        private int playerID;

        public Figure(ImageView imageView) {
            this.imageView = imageView;
        }

        public boolean isTaken() {
            return taken;
        }

        public void setTaken(boolean taken) {
            this.taken = taken;
        }

        public ImageView getImageView() {
            return imageView;
        }

        public int getPlayerID() {
            return playerID;
        }

        public void setPlayerID(int playerID) {
            this.playerID = playerID;
        }
    }
}