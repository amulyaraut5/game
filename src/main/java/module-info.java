module roborally {
        requires javafx.controls;
        requires javafx.fxml;
    requires com.google.gson;

    opens client.view to javafx.fxml;
        exports client.view;
        }