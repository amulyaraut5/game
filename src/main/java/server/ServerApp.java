package server;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ServerApp extends Application {

    public static void main(String[] args) {
        Server.getInstance().start();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader serverLoader = new FXMLLoader(getClass().getResource("/view/serverView.fxml"));
        primaryStage.setTitle("Server - RoboRallye");
        primaryStage.setScene(new Scene(serverLoader.load()));
        primaryStage.setResizable(false);

        Server.getInstance().setServerController(serverLoader.getController());

        primaryStage.setOnCloseRequest(event -> System.exit(0));
        primaryStage.show();
    }
}