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
    @FXML
    public ImageView programCard1;


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
