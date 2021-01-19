package client.view;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;


public class ProgrammingController extends Controller {
    //@FXML
    //public ImageView imageView;
    //@FXML
    //public ImageView programCard;
    //private ImageView imageView;
    public AnchorPane programmingPhasePane;
    public HBox hBox1Background;
    public HBox hBox2Background;
    @FXML
    private HBox hBox1;
    @FXML
    private HBox hBox2;
    private double widthHBox;
    private double heightHBox;

    public void initialize() { //TODO method that gets called when cards were dealt
        widthHBox = hBox1.getPrefWidth() / 5;
        heightHBox = hBox1.getPrefHeight();
        hBox1.setSpacing(20);
        hBox2.setSpacing(20);
        hBox1Background.setSpacing(20);
        hBox2Background.setSpacing(20);
        /*HBox backgroundHBox1 = new HBox();
        HBox backgroundHBox2 = new HBox();
        backgroundHBox1.setSpacing(20);
        backgroundHBox1.setLayoutX(hBox1.getLayoutX());
        System.out.println(hBox1.getTranslateX());
        System.out.println(hBox1.getLayoutX());
        System.out.println(hBox1.getScaleX());
        System.out.println(backgroundHBox1.getLayoutX());*/
        /*ImageView background = new ImageView(new Image(getClass().getResource("/cards/programming/underground-card.png").toString()));
        background.setFitHeight(heightHBox);
        background.setFitWidth(widthHBox - 20);
        for(int i=0; i<5; i++) hBox1Background.getChildren().add(background);
        for(int i=0; i<4; i++) hBox2Background.getChildren().add(background);*/
        for (int i = 0; i < 5; i++) {
            ImageView background = new ImageView(new Image(getClass().getResource("/cards/programming/underground-card.png").toString()));
            background.setFitHeight(heightHBox);
            background.setFitWidth(widthHBox - 20);
            hBox1Background.getChildren().add(background);
        }
        for (int i = 0; i < 4; i++) {
            ImageView background = new ImageView(new Image(getClass().getResource("/cards/programming/underground-card.png").toString()));
            background.setFitHeight(heightHBox);
            background.setFitWidth(widthHBox - 20);
            hBox2Background.getChildren().add(background);
        }
    }

    public void startProgrammingPhase(ArrayList<String> cardList) {

        ArrayList<String> cardLists = new ArrayList<>(); //HARDCODED
        cardList.add("Again");
        cardList.add("MoveI");
        cardList.add("MoveII");
        cardList.add("MoveIII");
        cardList.add("UTurn");
        cardList.add("Again");
        cardList.add("MoveI");
        cardList.add("MoveII");
        cardList.add("MoveIII");

        /*
        for (String card : cardList){ //TODO send (?) only 9 cards
            StackPane pane = createNewPane();
            addImage(new Image(getClass().getResource("/cards/programming/" + card +"-card.png").toString()), pane);
            if(!(hBox1.getChildren().size()>=5)) hBox1.getChildren().add(pane);
            else hBox2.getChildren().add(pane);

            }*/
        for (int i = 0; i <= 8; i++) {
            StackPane pane = createNewPane();
            addImage(new Image(getClass().getResource("/cards/programming/" + cardList.get(i) + "-card.png").toString()), pane);
            if (!(hBox1.getChildren().size() >= 5)) hBox1.getChildren().add(pane);
            else hBox2.getChildren().add(pane);
        }

    }



/*
    @FXML
    private void setOnDragDetected(Event event, ImageView imageView) {
        DragBoard db = imageView.startDragAndDrop(TransferMode.ANY);
        //DragBoard db = programCard1.startDragAndDrop(TransferMode.ANY);
        //DragBoard db = source.startDragAndDrop(TransferMode.ANY);
        /* Put a string on a DragBoard
        ClipboardContent content = new ClipboardContent();

        //content.put(new DataFormat(cardName),imageView.getImage());
        content.putImage(imageView.getImage());
        db.setContent(content);

        event.consume();

    }*/


    private StackPane createNewPane() {
        StackPane pane = new StackPane();

        pane.setPrefHeight(heightHBox);
        pane.setPrefWidth(widthHBox - 20);

        pane.setOnDragOver(dragEvent -> mouseDragOver(dragEvent, pane));
        pane.setOnDragDropped(dragEvent -> mouseDragDropped(dragEvent, pane));
        pane.setOnDragExited(dragEvent -> pane.setStyle("-fx-border-color: #C6C6C6;"));

        return pane;
    }


    private void setOnDragDetected(MouseEvent mouseEvent, ImageView imageView) {

        Dragboard db = imageView.startDragAndDrop(TransferMode.ANY);
        ClipboardContent content = new ClipboardContent();
        content.putImage(imageView.getImage());
        //System.out.println("setOnDragDetected" + imageView.getImage().getUrl());
        setImageDropped(imageView.getImage().getUrl());
        //System.out.println(imageDropped);
        db.setContent(content);
        imageView.setImage(null);
        mouseEvent.consume();

    }


    void addImage(Image i, StackPane pane) {
        ImageView imageView = new ImageView();
        imageView.setFitWidth(widthHBox - 20);
        imageView.setFitHeight(heightHBox);
        imageView.setImage(i);
        imageView.setOnDragDetected(mouseEvent -> setOnDragDetected(mouseEvent, imageView));

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
            System.out.println("CardName programmingController" + getImageDropped());
            addImage(img, pane);


        }
        event.setDropCompleted(success);
        event.consume();
    }

    private void mouseDragOver(DragEvent event, StackPane pane) {

        pane.setStyle("-fx-border-color: red;"
                + "-fx-border-width: 5;"
                + "-fx-background-color: #C6C6C6;"
                + "-fx-border-style: solid;");
        event.acceptTransferModes(TransferMode.ANY);
        event.consume();
    }
}


