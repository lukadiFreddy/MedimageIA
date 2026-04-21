module com.example.medimageia {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.medimageia to javafx.fxml;
    exports com.example.medimageia;
}