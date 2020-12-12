module roborally {
        requires javafx.controls;
        requires javafx.fxml;

        opens client.view.login to javafx.fxml;
        exports client.view.login;
        }