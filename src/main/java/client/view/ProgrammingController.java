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

import java.util.ArrayList;


public class ProgrammingController {
    //@FXML
    //public ImageView imageView;
    //@FXML
    //public ImageView programCard;

    public AnchorPane programmingPhasePane;
    public HBox hBox;

    public void initialize(){ //TODO method that gets called when cards were dealt
        ArrayList<String> cardList = new ArrayList<>(); //HARDCODED
        cardList.add("Again");
        cardList.add("MoveI");
        cardList.add("MoveII");
        cardList.add("MoveIII");
        cardList.add("UTurn");
        for (String card : cardList){
            ImageView programCard = new ImageView();
            programCard.setImage(new Image("/programming-cards/" + card +"-card.png"));
            programCard.setFitWidth(90);
            programCard.setFitHeight(116);
            programCard.setOnDragDetected(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    setOnDragDetected(mouseEvent, programCard);
                }
            });
            hBox.getChildren().add(programCard);
        }

    }




    @FXML
    private void setOnDragDetected(Event event, ImageView imageView) {
        Dragboard db = imageView.startDragAndDrop(TransferMode.ANY);
        //Dragboard db = programCard1.startDragAndDrop(TransferMode.ANY);
        //Dragboard db = source.startDragAndDrop(TransferMode.ANY);
        /* Put a string on a dragboard */
        ClipboardContent content = new ClipboardContent();

        //content.put(new DataFormat(cardName),imageView.getImage());
        content.putImage(imageView.getImage());
        db.setContent(content);

        event.consume();

    }

}
