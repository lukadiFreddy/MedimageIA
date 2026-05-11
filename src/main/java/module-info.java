module com.example.medimageia {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    requires java.sql;
    requires java.desktop;

    opens com.example.medimageia to javafx.fxml;
    exports com.example.medimageia;
}