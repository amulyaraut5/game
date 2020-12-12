module roborally {
        requires javafx.controls;
        requires javafx.fxml;

        opens client.view to javafx.fxml;
        exports client.view;
        }