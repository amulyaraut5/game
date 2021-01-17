package client.view;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;


public class ProgrammingController extends Controller {
    //@FXML
    //public ImageView imageView;
    //@FXML
    //public ImageView programCard;
    //private ImageView imageView;
    public AnchorPane programmingPhasePane;
    @FXML
    private HBox hBox1;

    public HBox hBox2;
    private double widthHBox;
    private double heightHBox;

    public void initialize(){ //TODO method that gets called when cards were dealt
        widthHBox = hBox1.getPrefWidth()/5;
        heightHBox = hBox1.getPrefHeight();
        hBox1.setSpacing(20);
        hBox2.setSpacing(20);
        ArrayList<String> cardList = new ArrayList<>(); //HARDCODED
        cardList.add("Again");
        cardList.add("MoveI");
        cardList.add("MoveII");
        cardList.add("MoveIII");
        cardList.add("UTurn");
        cardList.add("Again");
        cardList.add("MoveI");
        cardList.add("MoveII");
        cardList.add("MoveIII");

        ////////////////////SHORT TEST HOW TO GET CARDNAME OUT OF URL
        Image image = new Image(getClass().getResource("/cards/programming/Again-card.png").toString());
        System.out.println("Url. " +image.getUrl());
        String [] a = image.getUrl().split("/");
        String imageName = a[a.length-1];
        System.out.println(imageName.substring(0, imageName.length()-9));

        Image image2 = new Image(getClass().getResource("/cards/programming/TurnLeft-card.png").toString());
        System.out.println("Url. " +image2.getUrl());
        String [] a2 = image2.getUrl().split("/");
        String imageName2 = a2[a2.length-1];
        System.out.println(imageName2.substring(0, imageName2.length()-9));

        ////////////////////

        for (String card : cardList){
            StackPane pane = createNewPane();
            addImage(new Image(getClass().getResource("/cards/programming/" + card +"-card.png").toString()), pane);
            //ImageView programCard = new ImageView();
            //programCard.setImage(new Image("/programming-cards/" + card +"-card.png"));
            //programCard.setFitWidth(90);
            //programCard.setFitHeight(116);
            /*pane.setOnDragDetected(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    setOnDragDetected(mouseEvent, programCard);
                }
            });*/
            if(!(hBox1.getChildren().size()>=5)){
                hBox1.getChildren().add(pane);
            } else {
                hBox2.getChildren().add(pane);
            }

        }

   }



/*
    @FXML
    private void setOnDragDetected(Event event, ImageView imageView) {
        Dragboard db = imageView.startDragAndDrop(TransferMode.ANY);
        //Dragboard db = programCard1.startDragAndDrop(TransferMode.ANY);
        //Dragboard db = source.startDragAndDrop(TransferMode.ANY);
        /* Put a string on a dragboard
        ClipboardContent content = new ClipboardContent();

        //content.put(new DataFormat(cardName),imageView.getImage());
        content.putImage(imageView.getImage());
        db.setContent(content);

        event.consume();

    }*/


    private StackPane createNewPane(){
        StackPane pane = new StackPane();

        pane.setPrefHeight(heightHBox);
        pane.setPrefWidth(widthHBox-20);
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
        //setImageDropped();
        db.setContent(content);
        //imageView.setImage(new Image(getClass().getResource("/cards/programming/backside-card.png").toString()));
        mouseEvent.consume();
    }



    void addImage(Image i, StackPane pane){
        ImageView imageView = new ImageView();
        imageView.setFitWidth(widthHBox-20);
        imageView.setFitHeight(heightHBox);
        imageView.setImage(i);
        imageView.setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                setOnDragDetected(mouseEvent, imageView);
            }
        });
        /*String [] url = i.getUrl().split("/");
        String imageName = url[url.length-1];
        String cardName = imageName.substring(0, imageName.length()-9);
        System.out.println(cardName);*/
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
        event.acceptTransferModes(TransferMode.ANY);
        event.consume();
    }
}


