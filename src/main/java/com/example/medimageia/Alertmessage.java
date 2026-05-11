package com.example.medimageia;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class Alertmessage {

    private Alert alert;

    // MESSAGE D'ERREUR
    public void errorMessage(String message){

        alert = new Alert(Alert.AlertType.ERROR);

        alert.setTitle("Message d'erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }

    // MESSAGE DE CONFIRMATION
    public boolean confirmationMessage(String message){

        alert = new Alert(Alert.AlertType.CONFIRMATION);

        alert.setTitle("Message de confirmation");
        alert.setHeaderText(null);
        alert.setContentText(message);

        Optional<ButtonType> option = alert.showAndWait();

        return option.isPresent() && option.get() == ButtonType.OK;
    }

    // MESSAGE DE SUCCÈS-
    public void successMessage(String message){

        alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("Message de succès");
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }
}