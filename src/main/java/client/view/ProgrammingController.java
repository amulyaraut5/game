package client.view;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

import javax.swing.text.html.ImageView;

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
    private void setOnDragDetected(Event event) {
        //Dragboard db = programCard1.
        //Dragboard db = programCard1.startDragAndDrop(TransferMode.ANY);
        //Dragboard db = source.startDragAndDrop(TransferMode.ANY);
        /* Put a string on a dragboard */
        //ClipboardContent content = new ClipboardContent();
        //content.putImage(s.getImage());
        //db.setContent(content);

        //event.consume();

    }

}
