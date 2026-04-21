module com.example.medimageia {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.xml.crypto;


    opens com.example.medimageia to javafx.fxml;
    exports com.example.medimageia;
}