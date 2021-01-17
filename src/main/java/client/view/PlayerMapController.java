package client.view;



import game.Player;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import utilities.JSONProtocol.JSONBody;
import utilities.JSONProtocol.JSONMessage;
import utilities.JSONProtocol.body.PlayerAdded;
import utilities.JSONProtocol.body.SelectCard;

/**
 *
 * @author sarah
 */
public class PlayerMapController extends Controller{

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

    public void initialize(){
        widthRegisterCard = registerHBox.getPrefWidth()/5;
        heightRegisterCard = registerHBox.getPrefHeight();
        registerHBox.setSpacing(20);
        createRegisterNumberImages();
        int register = 5;
        for (int i = 0; i<register; i++){
            StackPane pane = createNewPane();
            registerHBox.getChildren().add(pane);
        }
    }

    private StackPane createNewPane(){
        StackPane pane = new StackPane();

        pane.setPrefHeight(heightRegisterCard);
        pane.setPrefWidth(widthRegisterCard);
        pane.setStyle("-fx-border-color: #d100ea;");
        pane.setStyle("-fx-background-color: #FFFFFF;");

        pane.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent dragEvent) {
                mouseDragOver(dragEvent, pane);
            }
        });
        pane.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent dragEvent) {
                mouseDragDropped(dragEvent, pane);
            }
        });
        pane.setOnDragExited(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent dragEvent) {
                pane.setStyle("-fx-border-color: #C6C6C6;");
                }
            });

        return pane;
        }


    private void setOnDragDetected(MouseEvent mouseEvent, ImageView imageView) {
        Dragboard db = imageView.startDragAndDrop(TransferMode.ANY);
        ClipboardContent content = new ClipboardContent();
        content.putImage(imageView.getImage());
        setImageDropped(imageView.getImage().getUrl());
        db.setContent(content);
        imageView.setImage(null);
        mouseEvent.consume();
    }

    public void loadPlayerMap(Player player){
        String name = robotNames[player.getFigure()];
        playerIcon.setImage(new Image(getClass().getResource("/lobby/" + name +".png").toString()));
        playerMapLabelName.setText(player.getName() + " " + player.getID());
    }


    void addImage(Image i, StackPane pane){
        ImageView imageView = new ImageView();
        imageView.setFitWidth(widthRegisterCard-20);
        imageView.setFitHeight(heightRegisterCard);
        imageView.setImage(i);
        imageView.setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                setOnDragDetected(mouseEvent, imageView);

            }
        });
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
            System.out.println("in PlayerMapController " + getImageDropped());
            //TODO getting url
            //JSONMessage jsonMessage = new JSONMessage( new SelectCard(getImageDropped(), registerHBox.getChildren().indexOf(pane)));


        }
        event.setDropCompleted(success);
        event.consume();
    }

    private  void mouseDragOver(DragEvent event, StackPane pane) {
        pane.setStyle("-fx-border-color: red;"
                + "-fx-border-width: 5;"
                + "-fx-background-color: #C6C6C6;"
                + "-fx-border-style: solid;");
        event.acceptTransferModes(TransferMode.ANY);
        event.consume();
    }

    private void createRegisterNumberImages(){
        double positionX = widthRegisterCard*2-20;
        double positionY = heightRegisterCard-20;
        for (int i = 1; i<=5; i++){
            ImageView imageView = new ImageView(new Image(getClass().getResource("/backgrounds/register/register_" + i +".png").toString()));
            imageView.setFitHeight(30);
            imageView.setFitWidth(30);
            imageView.setTranslateX(positionX);
            imageView.setTranslateY(positionY);
            positionX += widthRegisterCard;
            positionX += 3;
            playerMapAnchorPane.getChildren().add(imageView); //TODO other numbers of registers
        }

    }
    }




