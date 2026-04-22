package com.example.medimageia;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class Alertmessage {

    private Alert alert;

    // Cette partie gere les messages d'erreurs
    public void errorMessage(String message){
        alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Message d'erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Cette partie gere les message de confirmations
    public boolean confirmationMessage(String message){
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Message de confirmation");
        alert.setHeaderText(null);
        alert.setContentText(message);
        Optional<ButtonType> option = alert.showAndWait();
        if (option.isPresent() && option.get() == ButtonType.OK) {
            return true;
        } else {
            return false;
        }
    }

    // Cette partie gere les message de success
    public void successMessage(String message){
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Message de success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
