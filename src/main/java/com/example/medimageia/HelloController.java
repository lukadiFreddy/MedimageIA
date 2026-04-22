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
    private TextField login_password_visible;
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
    private TextField register_password_visible;
    @FXML
    private CheckBox register_checkbox;
    @FXML
    private Button register_btn;
    @FXML
    private Hyperlink register_back;
    // Elements pour la Base de donnees
    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;

    // Apres le clic du Button Connexion
    private void clearLoginFields() {
        login_username.clear();
        login_password.clear();
        login_checkbox.setSelected(false);
    }

    // Apres le clic duu Button Inscription
    private void clearRegisterFields() {
        register_email.clear();
        register_username.clear();
        register_password.clear();
        register_checkbox.setSelected(false);
    }

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

        String checkUser = "SELECT * FROM docteur WHERE user_email = ? AND user_name = ?";

        try {
            connect = DataBase.connectDB();
            prepare = connect.prepareStatement(checkUser);
            prepare.setString(1, email);
            prepare.setString(2, username);
            result = prepare.executeQuery();

            if (result.next()) {
                alert.errorMessage("L'adresse e-mail ou le nom d'utilisateur est déjà utilisé.");
            } else if(register_password.getText().length() < 6) {
                alert.errorMessage("Le mot de passe est trop court au moins 6 caracteres");
            }else {
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
                clearRegisterFields();
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

    @FXML
    public void initialize() {

        // LOGIN
        login_checkbox.setOnAction(e -> {
            boolean show = login_checkbox.isSelected();

            login_password_visible.setVisible(show);
            login_password_visible.setManaged(show);

            login_password.setVisible(!show);
            login_password.setManaged(!show);

            if (show) login_password_visible.setText(login_password.getText());
            else login_password.setText(login_password_visible.getText());
        });

        // REGISTER
        register_checkbox.setOnAction(e -> {
            boolean show = register_checkbox.isSelected();

            register_password_visible.setVisible(show);
            register_password_visible.setManaged(show);

            register_password.setVisible(!show);
            register_password.setManaged(!show);

            if (show) register_password_visible.setText(register_password.getText());
            else register_password.setText(register_password_visible.getText());
        });
    }


}