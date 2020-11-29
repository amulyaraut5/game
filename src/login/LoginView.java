package login;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginView extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage loginStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("loginView.fxml"));
        loginStage.setTitle("Love Letter");
        loginStage.setResizable(false);

        loginStage.setScene(new Scene(root));
        loginStage.show();
        LoginController.setStage(loginStage);
    }
}
