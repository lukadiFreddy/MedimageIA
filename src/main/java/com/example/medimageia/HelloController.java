package com.example.medimageia;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;

public class HelloController {

    @FXML
    private AnchorPane main_form;

    @FXML
    private AnchorPane login_form;
    @FXML
    private TextField login_username;
    @FXML
    private PasswordField login_password;
    @FXML
    private CheckBox login_checkbox;
    @FXML
    private Button login_btn;
    @FXML
    private Hyperlink login_back;

    @FXML
    private AnchorPane register_form;
    @FXML
    private TextField register_email;
    @FXML
    private TextField register_username;
    @FXML
    private PasswordField register_password;
    @FXML
    private CheckBox register_checkbox;
    @FXML
    private Button register_btn;
    @FXML
    private Hyperlink register_back;

    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;


    // Vérifie que cette classe existe bien
    private Alertmessage alert = new Alertmessage();

    public void registerAcount() {

        if (register_email.getText().isEmpty()
                || register_username.getText().isEmpty()
                || register_password.getText().isEmpty()) {

            alert.errorMessage("Veuillez remplir tous les champs");
            return;
        }

        String email = register_email.getText();
        String username = register_username.getText();
        String password = register_password.getText();

//
//        if (connect == null) {
//            alert.errorMessage("La creation du compte a échouée");
//            return;
//        }

        String checkUsername = "SELECT * FROM docteur WHERE user_name = ?";

        try {
            connect = DataBase.connectDB();
            prepare = connect.prepareStatement(checkUsername);
            prepare.setString(1, username);
            result = prepare.executeQuery();

            if (result.next()) {
                alert.errorMessage("Le nom " + username + " existe déjà");
            } else {
                // Apres les verification il insert les infos dans la base de donnees
                String insertData = "INSERT INTO docteur (user_email, user_name, user_password, created_at) VALUES (?, ?, ?, ?)";

                Date sqlDate = new Date(System.currentTimeMillis());

                prepare = connect.prepareStatement(insertData);
                prepare.setString(1, email);
                prepare.setString(2, username);
                prepare.setString(3, password);
                prepare.setDate(4, sqlDate);

                prepare.executeUpdate();

                alert.successMessage("Enregistrement réussi");
            }

        } catch (Exception e) {
            e.printStackTrace();
            alert.errorMessage("Erreur lors de l'enregistrement");
        }
    }

    public void switchForm(ActionEvent event) {

        if (event.getSource() == login_back) {
            login_form.setVisible(false);
            register_form.setVisible(true);

        } else if (event.getSource() == register_back) {
            login_form.setVisible(true);
            register_form.setVisible(false);
        }
    }
}