package com.example.medimageia;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import static java.lang.System.out;

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
    @FXML private TextField profil_passM;
    @FXML private TextField profil_dom;
    @FXML private ComboBox<String> profil_sexe;
    @FXML private ComboBox<String> profil_specialisation;
    @FXML private ComboBox<String> profil_status;
    @FXML private Button profil_updateBtn;
    @FXML private Label nav_userSpect;
    // DB
    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;
    private Image image;
    private Alertmessage alert = new Alertmessage();

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
                profil_passM.setText(result.getString("user_password"));
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
    public String[] sexe = {"M", "F", "AUTRE"};

    public void profileSexeList(){
        List<String> listSE = new ArrayList<>();
        for(String date : sexe){
            listSE.add(date);
        }
        ObservableList listData = FXCollections.observableList(listSE);
        profil_sexe.setItems(listData);
    }

    public void profileUpdateBtn() {

        if (profil_nomM.getText().isEmpty()
                || profil_mailM.getText().isEmpty()
                || profil_passM.getText().isEmpty()
                || profil_sexe.getSelectionModel().getSelectedItem() == null
                || profil_specialisation.getSelectionModel().getSelectedItem() == null
                || profil_status.getSelectionModel().getSelectedItem() == null) {

            alert.errorMessage("Veuillez remplir tous les champs");
            return;
        }

        String path = Data.path;

        if (path == null || path.isEmpty()) {
            path = "";
        }

        String updateData =
                "UPDATE docteur SET " +
                        "user_name = ?, " +
                        "user_email = ?, " +
                        "user_password = ?, " +
                        "user_sexe = ?, " +
                        "user_specialisation = ?, " +
                        "user_status = ?, " +
                        "user_image = ? " +
                        "WHERE user_name = ?";

        connect = DataBase.connectDB();

        try {

            prepare = connect.prepareStatement(updateData);

            prepare.setString(1, profil_nomM.getText());
            prepare.setString(2, profil_mailM.getText());
            prepare.setString(3, profil_passM.getText());
            prepare.setString(4, profil_sexe.getSelectionModel().getSelectedItem());
            prepare.setString(5, profil_specialisation.getSelectionModel().getSelectedItem());
            prepare.setString(6, profil_status.getSelectionModel().getSelectedItem());
            prepare.setString(7, path);

            // ancien username connecté
            prepare.setString(8, Data.admin_username);

            prepare.executeUpdate();

            // mettre à jour username session
            Data.admin_username = profil_nomM.getText();

            alert.successMessage("Mise à jour réussie");

            displayAdminUsername();
            profileLabel();

        } catch (Exception e) {
            e.printStackTrace();
            alert.errorMessage("Erreur lors de la mise à jour");
        }
    }

    public void profileChange() {
        FileChooser open = new FileChooser();

        open.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(
                        "Image Files",
                        "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp", "*.webp"
                )
        );

        File file = open.showOpenDialog(profil_importBtn.getScene().getWindow());

        if (file != null) {
            Data.path = file.getAbsolutePath();

            Image image = new Image(file.toURI().toString(), 173, 98, false, true);
            profil_image.setFill(new ImagePattern(image));
        }
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

    // Je gère la date et affiche le temps reel
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