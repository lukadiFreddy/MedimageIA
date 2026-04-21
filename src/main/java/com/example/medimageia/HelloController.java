package com.example.medimageia;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class HelloController {
    //la partie principale du module LOGIN: main_form
    @FXML
    private AnchorPane main_form;

    //La partie secondaire du module lOGIN: login_form
    @FXML
    private  AnchorPane login_form;
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

    //La troisieme partie du module LOGIN: register_form
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

    //la fonction qui s'occupe de la connexion avec la base de donnees
    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;

    public void registerAcount(){
        if(register_email.getText().isEmpty()
                || register_username.getText().isEmpty()
                || register_password.getText().isEmpty()){
        }
    }

    //Fonction qui switch du Login ou SIGN et vise versa
    public void switchForm(ActionEvent event){
        if(event.getSource() == login_back){
            login_form.setVisible(false);
            register_form.setVisible(true);
        }else if(event.getSource() == register_back){
            login_form.setVisible(true);
            register_form.setVisible(false);
        }
    }
}
