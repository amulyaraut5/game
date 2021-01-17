package client.view;



import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import utilities.JSONProtocol.body.PlayerAdded;


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

    public AnchorPane playerMapAnchorPane;
    private double widthRegisterCard;
    private double heightRegisterCard;

    public void initialize(){
        widthRegisterCard = registerHBox.getPrefWidth()/5;
        heightRegisterCard = registerHBox.getPrefHeight();
        registerHBox.setSpacing(20);
        /*ImageView im = new ImageView(new Image(getClass().getResource("/backgrounds/register/register_2.png").toString()));
        im.setFitHeight(registerNumber.getFitHeight());
        im.setFitWidth(registerNumber.getFitWidth());
        double x = registerNumber.getX();
        double y= registerNumber.getX();
        im.setTranslateX(x);
        im.setY(y);
        playerMapAnchorPane.getChildren().add(im);*/ //TODO other numbers of registers
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
        //imageView.setImage(new Image(getClass().getResource("/cards/programming/backside-card.png").toString()));
        mouseEvent.consume();
    }

    public void loadPlayerMap(PlayerAdded playerAdded){
        String name = robotNames[playerAdded.getFigure()];
        playerIcon.setImage(new Image(getClass().getResource("/lobby/" + name +".png").toString()));
        playerMapLabelName.setText(playerAdded.getName() + " " + playerAdded.getID());
        /*System.out.println(getPlayersAdded().size());
        for (RobotIcon icon : getPlayersAdded()){
            System.out.println("twwwta");
            if (icon.isThisUser()){
                System.out.println("twwwta");
                //playerIcon = new ImageView(new Image());
            }
            System.out.println("twwwta");
        }*/

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




