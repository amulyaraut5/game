package client.view;


import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;


public class PlayerMapController {

    @FXML
    private ImageView imageView;
    @FXML
    public StackPane register1;


    @FXML
    public ImageView source;

    @FXML
    private HBox registerHBox;

    public void initialize(){
        System.out.println("j");
        int register = 5;
        for (int i = 0; i<5; i++){
            StackPane pane = new StackPane();
            pane.setPrefHeight(200);
            pane.setPrefWidth(300);
            pane.setStyle("-fx-border-color: #000000;");
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
            registerHBox.getChildren().add(pane);

    @FXML
    private void setOnDragOver(Event event) {
        mouseDragOver(event);
    }




    @FXML
    private void setOnDragDetected(Event event) {

        //Dragboard db = vBox.startDragAndDrop(TransferMode.ANY);
        Dragboard db = source.startDragAndDrop(TransferMode.ANY);
        /* Put a string on a dragboard */
        ClipboardContent content = new ClipboardContent();
        content.putImage(source.getImage());
        db.setContent(content);

        event.consume();

    }


    @FXML
    private void setOnDragDropped(Event event) {
        System.out.println("set on drag dropped");
        mouseDragDropped(event);
        }

   @FXML
   private void setOnDragExited(Event event) {
        register1.setStyle("-fx-border-color: #C6C6C6;");
        }




    void addImage(Image i, StackPane pane){

        imageView = new ImageView();
        imageView.setImage(i);
        imageView.setFitWidth(120);
        imageView.setFitHeight(165);
        pane.getChildren().add(imageView);
    }

    private void mouseDragDropped(final Event e) {
        DragEvent event = (DragEvent) e;
        final Dragboard db = event.getDragboard();
        boolean success = false;
        if (db.hasImage()) {
            success = true;
            // Only get the first file from the list
            //Platform.runLater(new Runnable() {
              //  @Override
                //public void run() {
                    if (!register1.getChildren().isEmpty()) {
                        register1.getChildren().remove(0);
                    }
                    Image img = db.getImage();

                    addImage(img, register1);
                //}
            //});

            //final File file = db.getFiles().get(0);
            /*Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    System.out.println(file.getAbsolutePath());
                    try {
                        if(!contentPane.getChildren().isEmpty()){
                            contentPane.getChildren().remove(0);
                        }
                        Image img = new Image(new FileInputStream(file.getAbsolutePath()));

                        addImage(img, contentPane);
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(DragAndDropExample.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });*/
        }
        event.setDropCompleted(success);
        event.consume();
    }

    private  void mouseDragOver(final Event e) {
        DragEvent event = (DragEvent) e;
        final Dragboard db = event.getDragboard();

        /*final boolean isAccepted = db.getImage().g.getName().toLowerCase().endsWith(".png")
                || db.getFiles().get(0).getName().toLowerCase().endsWith(".jpeg")
                || db.getFiles().get(0).getName().toLowerCase().endsWith(".jpg");

        if (db.hasFiles()) {
            if (isAccepted) {
                contentPane.setStyle("-fx-border-color: red;"
                        + "-fx-border-width: 5;"
                        + "-fx-background-color: #C6C6C6;"
                        + "-fx-border-style: solid;");
                event.acceptTransferModes(TransferMode.COPY);
            }
        } else {
            event.consume();
        }*/
        register1.setStyle("-fx-border-color: red;"
                + "-fx-border-width: 5;"
                + "-fx-background-color: #C6C6C6;"
                + "-fx-border-style: solid;");
        event.acceptTransferModes(TransferMode.COPY);
        event.consume();
    }
    }




