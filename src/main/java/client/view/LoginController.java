package client.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.JSONProtocol.body.PlayerValues;


/**
 * This class controls the loginView.fxml view, it takes the name and the chosen robot, sends it to
 * client and switches to the game view
 *
 * @author sarah,
 */
public class LoginController extends Controller {
    private static final Logger logger = LogManager.getLogger();
    /**
     * it stores the imageViews of the different robots,
     * so that name and id from the chosen robot
     * can be recognized
     */
    private final ObservableList<Figure> figures = FXCollections.observableArrayList();
    /**
     * this list stores the different robots (with name and id)
     */
    private final ObservableList<RobotPrivate> robotList = FXCollections.observableArrayList();
    @FXML
    private TextField textUserName;
    /**
     * a label to check if everything works //TODO delete or change purpose
     */
    @FXML
    private Label responseLabel;
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
                    if (figure.isTaken()) {
                        figure.getImageView().setDisable(true);
                        setDisable(true);
                        setBackground(new Background(new BackgroundFill(Color.GRAY,
                                CornerRadii.EMPTY, Insets.EMPTY)));
                    }
                }
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
        for (int i = 0; i < robotNames.length; i++) {
            String path = "/lobby/" + robotNames[i] + ".png";
            robot = new ImageView(new Image(getClass().getResource(path).toString()));
            robot.setFitHeight(scaleSize);
            robot.setFitWidth(scaleSize);

            RobotPrivate robotPrivate = new RobotPrivate(robotNames[i], i);
            robotList.add(robotPrivate);
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
        responseLabel.setText("");
        String userName = textUserName.getText();
        int chosenRobot = listView.getSelectionModel().getSelectedIndex();

        if (userName.isBlank()) responseLabel.setText("Please insert a Username!");
        else if (chosenRobot < 0) responseLabel.setText("You have to choose a robot!");
        else client.sendMessage(new PlayerValues(userName, chosenRobot));
    }

    public void setImageViewDisabled(int figure) {
        //TODO set cell of figure not selectable with CellFactory from initialize method
        //robotImageViewList.get(figure).setDisable(true);
        //robotImageViewList.get(figure).setMouseTransparent(true);
    }

    /**
     * @param taken
     */
    public void serverResponse(boolean taken) {
        //TODO username can't be already taken, only robot could be taken
        if (taken) {
            responseLabel.setText("Already taken, try again");
        } else {
            viewManager.nextScene();
        }
    }

    public void setFigureTaken(int id, boolean taken) {
        Figure figure = figures.get(id);
        figure.setTaken(taken);
        figures.set(id, figure);
    }

    private class Figure {
        private boolean taken = false;
        private ImageView imageView;

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
    }
}