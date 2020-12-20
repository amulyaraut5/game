module roborally {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    exports client.view to javafx.graphics;
    exports client to javafx.graphics;
    opens client.view to javafx.fxml;
    opens client to javafx.fxml;
    opens client.model to com.google.gson;
    opens Utilities to com.google.gson;
    opens Utilities.JSONProtocol to com.google.gson;
    opens Utilities.JSONProtocol.chat to com.google.gson;
    opens Utilities.JSONProtocol.connection to com.google.gson;
}