package com.example.medimageia;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class adminController implements Initializable {

    @FXML private Label top_profile;
    @FXML private Label date_time;

    // HEADER USER
    @FXML private Label top_userName;
    @FXML private Label nav_userName;

    // CENTRE IA
    @FXML private AnchorPane centre_ia;
    @FXML private AnchorPane centre_iaImage;
    @FXML private AnchorPane centre_iaReponse;
    @FXML private Button centre_iaBtn;
    @FXML private Button analyse_btn;

    // CENTRE SCAN
    @FXML private AnchorPane center_scan;
    @FXML private TableView<?> center_scanTabView;
    @FXML private TableColumn<?, ?> center_scanTabId;
    @FXML private TableColumn<?, ?> center_scanTabNom;
    @FXML private TableColumn<?, ?> center_scanTabImage;
    @FXML private TableColumn<?, ?> center_scanTabDiagnostic;
    @FXML private TableColumn<?, ?> center_scanTabAction;
    @FXML private Button listScan_btn;

    // PROFIL
    @FXML private AnchorPane centre_profil;
    @FXML private Button profile_btn;
    @FXML private Circle profil_image;
    @FXML private Button profil_importBtn;
    @FXML private Label profil_nom;
    @FXML private Label profil_mail;
    @FXML private Label profil_num;
    @FXML private Label profil_date;
    @FXML private TextField profil_nomM;
    @FXML private TextField profil_mailM;
    @FXML private TextField profil_numM;
    @FXML private TextField profil_dom;
    @FXML private ComboBox<String> profil_sexe;
    @FXML private ComboBox<String> profil_specialisation;
    @FXML private ComboBox<String> profil_status;
    @FXML private Button profil_updateBtn;
    // DB
    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;

    // Cette fonction aide pour afficher le nom
    public void displayAdminUsername() {

        String sql = "SELECT user_name FROM docteur WHERE user_name = ?";
        connect = DataBase.connectDB();

        try {
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, Data.admin_username);
            result = prepare.executeQuery();

            if (result.next()) {

                String username = result.getString("user_name");

                if (username != null && !username.isEmpty()) {
                    String formatted = username.toUpperCase();

                    top_userName.setText(formatted);
                    nav_userName.setText(formatted);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (result != null) result.close();
                if (prepare != null) prepare.close();
                if (connect != null) connect.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void profilFields(){
        String selectData = "SELECT * FROM docteur WHERE user_name = '" + Data.admin_username + "'";

        connect = DataBase.connectDB();

        try{

            prepare = connect.prepareStatement(selectData);
            result = prepare.executeQuery();

            if (result.next()) {

                profil_nomM.setText(result.getString("user_name"));
                profil_mailM.setText(result.getString("user_email"));
                profil_sexe.getSelectionModel().select(result.getString("user_sexe"));
                profil_numM.setText(result.getString("user_password"));
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    // La specialisation du docteur
    public String[] specialisation = {"Neurologie", "Neurochirurgie", "Psychiatrie", "Neuro-oncologie", "Neuropsychologie"};
    public void profileSpecialList(){
        List<String> lists = new ArrayList<>();
        for(String date : specialisation){
            lists.add(date);
        }
        ObservableList listData = FXCollections.observableList(lists);
        profil_specialisation.setItems(listData);
    }

    // La Status du docteur
    public String[] status = {"Actif", "Inactif", "Suspendu"};
    public void profileStatusList(){
        List<String> listST = new ArrayList<>();
        for(String date : status){
            listST.add(date);
        }
        ObservableList listData = FXCollections.observableList(listST);
        profil_status.setItems(listData);
    }

    // La Sexe du docteur
    public String[] sexe = {"Homme", "Femme", "Non Binaire"};
    public void profileSexeList(){
        List<String> listSE = new ArrayList<>();
        for(String date : sexe){
            listSE.add(date);
        }
        ObservableList listData = FXCollections.observableList(listSE);
        profil_sexe.setItems(listData);
    }

    public void profileLabel(){
        String selectData = "SELECT * FROM docteur WHERE user_name = '" + Data.admin_username + "'";
        connect = DataBase.connectDB();

        try{

            prepare = connect.prepareStatement(selectData);
            result = prepare.executeQuery();

            if (result.next()) {
                profil_nom.setText(result.getString("user_name"));
                profil_mail.setText(result.getString("user_email"));
                profil_num.setText(result.getString("user_password"));
                profil_date.setText(result.getString("created_at"));
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Ici c'est pour
    public void switchForm(ActionEvent event) {

        centre_ia.setVisible(false);
        center_scan.setVisible(false);
        centre_profil.setVisible(false);

        if (event.getSource() == analyse_btn) {
            centre_ia.setVisible(true);
        } else if (event.getSource() == listScan_btn) {
            center_scan.setVisible(true);
        } else if (event.getSource() == profile_btn) {
            centre_profil.setVisible(true);
        }
    }

    // Je gere la date et affiche le temps reel
    public void runTime() {
        Thread thread = new Thread(() -> {
            SimpleDateFormat format = new SimpleDateFormat("EEEE dd MMMM yyyy HH:mm:ss");

            try {
                while (true) {
                    Thread.sleep(1000);

                    Platform.runLater(() -> {
                        date_time.setText(format.format(new Date()));
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        thread.setDaemon(true);
        thread.start();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        runTime();
        displayAdminUsername();
        profileSpecialList();
        profileStatusList();
        profileSexeList();
        profilFields();
        profileLabel();
    }
}