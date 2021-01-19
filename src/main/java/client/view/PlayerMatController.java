package client.view;


import game.Player;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import utilities.JSONProtocol.body.SelectCard;

/**
 * @author sarah
 */
public class PlayerMatController extends Controller {

    public HBox registerHBoxBackground;
    @FXML
    private ImageView imageView;
    @FXML
    private HBox registerHBox;
    @FXML
    private ImageView playerIcon;

    @FXML
    private Label playerMapLabelName;

    @FXML
    private ImageView registerNumber;

    @FXML
    private AnchorPane playerMapAnchorPane;

    private double widthRegisterCard;

    private double heightRegisterCard;

    public void initialize() {
        widthRegisterCard = registerHBox.getPrefWidth() / 5;
        heightRegisterCard = registerHBox.getPrefHeight();
        registerHBox.setSpacing(20);
        registerHBoxBackground.setSpacing(20);
        createRegisterNumberImages();
        createRegisterBackground();
        int register = 5;
        for (int i = 0; i < register; i++) {
            StackPane pane = createNewPane();
            registerHBox.getChildren().add(pane);
        }
    }


    private StackPane createNewPane() {
        StackPane pane = new StackPane();

        pane.setPrefHeight(heightRegisterCard);
        pane.setPrefWidth(widthRegisterCard);


        pane.setOnDragOver(dragEvent -> mouseDragOver(dragEvent, pane));
        pane.setOnDragDropped(dragEvent -> mouseDragDropped(dragEvent, pane));
        pane.setOnDragExited(dragEvent -> pane.setStyle("-fx-border-color: #C6C6C6;"));

        return pane;
    }


    private void setOnDragDetected(MouseEvent mouseEvent, ImageView imageView, StackPane pane) {
        Dragboard db = imageView.startDragAndDrop(TransferMode.ANY);
        ClipboardContent content = new ClipboardContent();
        content.putImage(imageView.getImage());
        //setImageDropped(imageView.getImage().getUrl());
        db.setContent(content);
        imageView.setImage(null);
        client.sendMessage(new SelectCard("null", registerHBox.getChildren().indexOf(pane)));
        mouseEvent.consume();
    }

    public void loadPlayerMap(Player player) {
        String name = robotNames[player.getFigure()];
        playerIcon.setImage(new Image(getClass().getResource("/lobby/" + name + ".png").toString()));
        playerMapLabelName.setText(player.getName() + " " + player.getID());
    }


    void addImage(Image i, StackPane pane) {
        ImageView imageView = new ImageView();
        imageView.setFitWidth(widthRegisterCard - 20);
        imageView.setFitHeight(heightRegisterCard);
        imageView.setImage(i);
        imageView.setOnDragDetected(mouseEvent -> setOnDragDetected(mouseEvent, imageView, pane));
        pane.getChildren().add(imageView);


    }

    private void mouseDragDropped(DragEvent event, StackPane pane) {
        Dragboard db = event.getDragboard();
        boolean success = false;
        if (db.hasImage()) {
            success = true;
            if (!pane.getChildren().isEmpty()) {
                pane.getChildren().remove(0);
            }
            Image img = db.getImage();
            addImage(img, pane);
            System.out.println("CardName playerMapController" + getImageDropped());
            System.out.println("Register " + registerHBox.getChildren().indexOf(pane) + 1);
            String cardName = getImageDropped();
            int registerNumber = registerHBox.getChildren().indexOf(pane) + 1;
            client.sendMessage(new SelectCard(cardName, registerNumber));
            //TODO getting url
            //JSONMessage jsonMessage = new JSONMessage( new SelectCard(getImageDropped(), registerHBox.getChildren().indexOf(pane)));


        }
        event.setDropCompleted(success);
        event.consume();
    }

    private void mouseDragOver(DragEvent event, StackPane pane) {
        pane.setStyle("-fx-border-color: #ff0000;"
                + "-fx-border-width: 5;"
                + "-fx-background-color: #C6C6C6;"
                + "-fx-border-style: solid;");
        event.acceptTransferModes(TransferMode.ANY);
        event.consume();
    }

    private void createRegisterNumberImages() {
        double positionX = widthRegisterCard * 2 - 20;
        double positionY = heightRegisterCard - 20;
        for (int i = 1; i <= 5; i++) {
            ImageView imageView = new ImageView(new Image(getClass().getResource("/backgrounds/register/register_" + i + ".png").toString()));
            imageView.setFitHeight(30);
            imageView.setFitWidth(30);
            imageView.setTranslateX(positionX);
            imageView.setTranslateY(positionY);
            positionX += widthRegisterCard;
            positionX += 3;
            playerMapAnchorPane.getChildren().add(imageView);
        }

    }

    private void createRegisterBackground() {
        for (int i = 0; i <= 4; i++) {
            ImageView background = new ImageView(new Image(getClass().getResource("/cards/programming/backside-card.png").toString()));
            background.setFitHeight(heightRegisterCard);
            background.setFitWidth(widthRegisterCard - 20);
            background.setDisable(true);
            registerHBoxBackground.getChildren().add(background);
        }
    }
}




