module roborally {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires org.apache.logging.log4j;
    requires java.desktop;
    requires javafx.media;
    requires com.jfoenix;

    exports client;
    exports game;
    exports server;
    exports game.round;
    exports game.gameObjects.decks;
    exports game.gameObjects.cards;
    exports utilities.enums;
    exports utilities.JSONProtocol;
    exports utilities.JSONProtocol.body;
    exports game.gameObjects.robot;
    exports client.view to javafx.graphics;
    exports game.gameObjects.tiles to com.google.gson;
    exports game.gameObjects.maps to com.google.gson;
    exports utilities to javafx.graphics;
    exports game.gameObjects.cards.programming to com.google.gson;
    exports game.gameActions to com.google.gson;

    opens game.gameObjects.maps;
    opens client.view to javafx.fxml;
    opens client to javafx.fxml;
    opens client.model to com.google.gson;
    opens utilities to com.google.gson;
    opens utilities.JSONProtocol to com.google.gson;
    opens utilities.JSONProtocol.body to com.google.gson;
    opens game.gameObjects.tiles to com.google.gson;
    opens game to com.google.gson;
    opens game.gameObjects.cards to com.google.gson;
    opens game.gameObjects.robot to com.google.gson;
    opens game.gameActions to com.google.gson;
    opens server to com.google.gson;
}