package client.view;

import game.gameObjects.cards.Card;
import game.gameObjects.cards.programming.*;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import utilities.JSONProtocol.body.PlayerAdded;

import java.util.ArrayList;


public class ProgrammingController {
    //@FXML
    //public ImageView imageView;
    //@FXML
    //public ImageView programCard;
    private ImageView imageView;
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
        for (String card : cardList){
            StackPane pane = createNewPane();
            addImage(new Image("/programming-cards/" + card +"-card.png"), pane);
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

    }

}
