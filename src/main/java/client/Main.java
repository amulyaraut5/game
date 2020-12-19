package client;

import client.view.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage loginStage) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/loginView.fxml"));
		Parent root = loader.load();
		LoginController loginController = loader.getController();

		loginStage.setTitle("Roborally");
		loginStage.setResizable(false);
		loginStage.setScene(new Scene(root));
		loginStage.show();
		loginController.setStage(loginStage);

		loginStage.setOnCloseRequest(event -> {
			loginController.close();
			loginStage.close();
		});
	}
}
