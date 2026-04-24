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
    private TextField register_showPassword;
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



    // Vérifie que cette classe existe bien
    private Alertmessage alert = new Alertmessage();

    // Cette partie sert pour la connexion au systeme
    private void loginAcount(){
        if(login_username.getText().isEmpty() || login_password.getText().isEmpty()){
            alert.errorMessage("Veuillez remplir tous les champs s'il vous plait");
        }else{
            String sql = "SELECT * FROM docteur WHERE username=? AND password=?";

            connect = DataBase.connectDB();

            try{

                prepare = connect.prepareStatement(sql);
                prepare.setString(1,login_username.getText());
                prepare.setString(2,login_password.getText());
                result = prepare.executeQuery();

                if(result.next()){
                    
                }else{
                    alert.errorMessage("Vous informations sont incorrectes");
                }

            } catch (Exception e){
                e.printStackTrace();
                alert.errorMessage("La connexion a échouée, verifiez vos informations");
            }
        }
    }

    // Cette partie sert pour la creation du compte
    public void registerAcount() {

        if (register_email.getText().isEmpty()
                || register_username.getText().isEmpty()
                || register_password.getText().isEmpty()) {

            alert.errorMessage("Veuillez remplir tous les champs s'il vous plait");
            return;
        }

        String email = register_email.getText();
        String username = register_username.getText();
        String password = register_password.getText();

        String checkUsername = "SELECT * FROM docteur WHERE user_name = ?";
        String checkEmail = "SELECT * FROM docteur WHERE user_email = ?";

        try {
            connect = DataBase.connectDB();
            if (connect == null) {
                alert.errorMessage("Connexion à la base échouée");
                return;
            }

            prepare = connect.prepareStatement(checkUsername);
            prepare.setString(1, username);
            result = prepare.executeQuery();

            if (result.next()) {
                alert.errorMessage("Le nom existe déjà  " );
                return;
            }

            prepare = connect.prepareStatement(checkEmail);
            prepare.setString(1, email);
            result = prepare.executeQuery();
            if (result.next()) {
                alert.errorMessage("L email existe déjà  " );
                return;
            }

            if (register_password.getText().length() < 6) {
                alert.errorMessage("Le mot de passe est trop court. Il doit contenir au moins 6 caractères.");
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

                alert.successMessage("L'inscription a réussi");
                clearRegisterFields();

                //Fermerture des iNterfase apres la connexion
                login_form.setVisible(true);
                register_form.setVisible(false);

            }

        } catch (Exception e) {
            e.printStackTrace();
            alert.errorMessage("L'inscription a échouée, verifiez vos informations");
        }
    }

    // Cette partie sert pour passer de Interface LOGIN a SIGN et vise versa
    public void switchForm(ActionEvent event) {

        if (event.getSource() == login_back) {
            login_form.setVisible(false);
            register_form.setVisible(true);
        } else if (event.getSource() == register_back) {
            login_form.setVisible(true);
            register_form.setVisible(false);
        }
    }

    // Cette partie c'est pour le button CheckBox qui affiche le mot de passe
    public void registerShowPassword(){
        if(register_checkbox.isSelected()){
            register_showPassword.setText(register_password.getText());
            register_showPassword.setVisible(true);
            register_password.setVisible(false);
        }else {
            register_password.setText(register_showPassword.getText());
            register_showPassword.setVisible(false);
            register_password.setVisible(true);
        }
    }

    // Apres le clic duu Button Inscription
    private void clearRegisterFields() {
        register_email.clear();
        register_username.clear();
        register_password.clear();
        register_checkbox.setSelected(false);
    }
}