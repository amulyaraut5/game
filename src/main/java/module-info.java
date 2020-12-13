module roborally {
        requires javafx.controls;
        requires javafx.fxml;
    requires com.google.gson;
    exports client to javafx.graphics;
    opens client.view to javafx.fxml;
        exports client.view;
        }