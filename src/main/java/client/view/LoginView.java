package client.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginView extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage loginStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/loginView.fxml"));
        Parent root = loader.load();
        LoginController loginController = loader.getController();

        loginStage.setTitle("Roborally");
        loginStage.setScene(new Scene(root));
        loginStage.show();
        LoginController.setStage(loginStage);

        loginStage.setOnCloseRequest(event -> {
            loginController.close();
            loginStage.close();
        });
    }
}
