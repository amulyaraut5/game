package client.view;



import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import utilities.JSONProtocol.body.PlayerAdded;

import java.awt.*;


public class PlayerMapController extends Controller{

    @FXML
    private ImageView imageView;

    @FXML
    private HBox registerHBox;

    @FXML
    private ImageView playerIcon;

    @FXML
    private Label playerMapLabelName;



    private double widthRegisterCard;
    private double heightRegisterCard;

    public void initialize(){
        widthRegisterCard = registerHBox.getPrefWidth()/5;
        heightRegisterCard = registerHBox.getPrefHeight();
        registerHBox.setSpacing(20);

        int register = 5;

        for (int i = 0; i<5; i++){
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

        db.setContent(content);
        imageView.setImage(new Image(getClass().getResource("/programming-cards/backside-card.png").toString()));
        mouseEvent.consume();
    }

//        //Dragboard db = vBox.startDragAndDrop(TransferMode.ANY);
//        Dragboard db = source.startDragAndDrop(TransferMode.ANY);
//        /* Put a string on a dragboard */
//        ClipboardContent content = new ClipboardContent();
//        content.putImage(source.getImage());
//        db.setContent(content);
//
//        event.consume();
    }






    void addImage(Image i, StackPane pane){
        imageView = new ImageView();
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

        }
        event.setDropCompleted(success);
        event.consume();
    }

    private  void mouseDragOver(DragEvent event, StackPane pane) {


        pane.setStyle("-fx-border-color: red;"
                + "-fx-border-width: 5;"
                + "-fx-background-color: #C6C6C6;"
                + "-fx-border-style: solid;");
        event.acceptTransferModes(TransferMode.COPY);
        event.consume();
    }
    }




