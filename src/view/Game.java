package view;

import client.ChatClient;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import server.User;

public class Game extends Application {
    private volatile String userResponse;
    private ChatClient chatClient;
    private Controller controller;


    private Button card1;
    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("game.fxml"));
        Parent root = loader.load();
        controller =  loader.getController();
        primaryStage.setTitle("Love Letter");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
        //client
        String hostname = "localhost";
        int port = 4444;
        chatClient = new ChatClient(hostname, port);
        chatClient.establishConnection();
        System.out.println("run");




    }


    public static void main (String[]args){
        launch(args);
    }



}
