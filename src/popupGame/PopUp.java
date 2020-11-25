package popupGame;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PopUp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("popup.fxml"));
        primaryStage.setTitle("Love Letter");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

    }
}
