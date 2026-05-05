package com.example.medimageia;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;

public class HelloController {

    @FXML private AnchorPane main_form;
    @FXML private AnchorPane login_form;
    @FXML private TextField login_username;
    @FXML private PasswordField login_password;
    @FXML private TextField login_showPassword;
    @FXML private CheckBox login_checkbox;
    @FXML private Button login_btn;
    @FXML private Hyperlink login_back;

    @FXML private AnchorPane register_form;
    @FXML private TextField register_email;
    @FXML private TextField register_username;
    @FXML private PasswordField register_password;
    @FXML private TextField register_showPassword;
    @FXML private CheckBox register_checkbox;
    @FXML private Button register_btn;
    @FXML private Hyperlink register_back;

    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;

    private Alertmessage alert = new Alertmessage();

    // ================= LOGIN =================
    public void loginAcount(){

        if(login_username.getText().isEmpty() || getLoginPassword().isEmpty()){
            alert.errorMessage("Veuillez remplir tous les champs");
            return;
        }

        String sql = "SELECT * FROM docteur WHERE user_name=? AND user_password=?";
        connect = DataBase.connectDB();

        if(connect == null){
            alert.errorMessage("Connexion à la base échouée");
            return;
        }

        try{
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, login_username.getText());
            prepare.setString(2, getLoginPassword());
            result = prepare.executeQuery();

            if(result.next()){

                // ✔ IMPORTANT
                Data.admin_username = login_username.getText();

                alert.successMessage("Connexion réussie");

                Parent root = FXMLLoader.load(getClass().getResource("/com/example/medimageia/main.fxml"));
                Stage stage = (Stage) login_btn.getScene().getWindow();

                stage.setScene(new Scene(root));
                stage.setResizable(true);
                stage.centerOnScreen();
                stage.show();

            } else {
                alert.errorMessage("Informations incorrectes");
            }

        } catch (Exception e){
            e.printStackTrace();
            alert.errorMessage("Erreur de connexion");
        }
    }

    private String getLoginPassword(){
        return login_checkbox.isSelected()
                ? login_showPassword.getText()
                : login_password.getText();
    }

    public void loginShowPassword(){
        if(login_checkbox.isSelected()){
            login_showPassword.setText(login_password.getText());
            login_showPassword.setVisible(true);
            login_password.setVisible(false);
        } else {
            login_password.setText(login_showPassword.getText());
            login_password.setVisible(true);
            login_showPassword.setVisible(false);
        }
    }

    // ================= REGISTER =================
    public void registerAcount() {

        if (register_email.getText().isEmpty()
                || register_username.getText().isEmpty()
                || getRegisterPassword().isEmpty()) {

            alert.errorMessage("Veuillez remplir tous les champs");
            return;
        }

        try {
            connect = DataBase.connectDB();
            if (connect == null) return;

            String insertData = "INSERT INTO docteur (user_email, user_name, user_password, created_at) VALUES (?, ?, ?, ?)";

            prepare = connect.prepareStatement(insertData);
            prepare.setString(1, register_email.getText());
            prepare.setString(2, register_username.getText());
            prepare.setString(3, getRegisterPassword());
            prepare.setDate(4, new Date(System.currentTimeMillis()));

            prepare.executeUpdate();

            // ✔ IMPORTANT
            Data.admin_username = register_username.getText();

            alert.successMessage("Inscription réussie");

            Parent root = FXMLLoader.load(getClass().getResource("/com/example/medimageia/main.fxml"));
            Stage stage = (Stage) register_btn.getScene().getWindow();

            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            alert.errorMessage("Erreur inscription");
        }
    }

    private String getRegisterPassword(){
        return register_checkbox.isSelected()
                ? register_showPassword.getText()
                : register_password.getText();
    }

    public void registerShowPassword(){
        if(register_checkbox.isSelected()){
            register_showPassword.setText(register_password.getText());
            register_showPassword.setVisible(true);
            register_password.setVisible(false);
        } else {
            register_password.setText(register_showPassword.getText());
            register_password.setVisible(true);
            register_showPassword.setVisible(false);
        }
    }

    // ================= SWITCH FORM =================
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