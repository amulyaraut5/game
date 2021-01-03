module roborally {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires org.apache.logging.log4j;

    exports client.view to javafx.graphics;
    exports client to javafx.graphics;
    exports game.gameObjects.tiles to com.google.gson;
    exports game.gameObjects.maps to com.google.gson;
    opens game.gameObjects.maps;
    opens client.view to javafx.fxml;
    opens client to javafx.fxml;
    opens client.model to com.google.gson;
    opens utilities to com.google.gson;
    opens utilities.JSONProtocol to com.google.gson;
    opens utilities.JSONProtocol.body to com.google.gson;
    //opens Utilities.JSONProtocol.chat to com.google.gson;
    opens utilities.JSONProtocol.body.gameStarted to com.google.gson;
    opens game.gameObjects.tiles to com.google.gson;

}