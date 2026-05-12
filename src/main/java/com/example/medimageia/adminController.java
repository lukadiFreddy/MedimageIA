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
    @FXML private Circle top_profil;
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
    @FXML private Label nbr;


    // DB
    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;
    private Image image;
    private Alertmessage alert = new Alertmessage();
    // C'est dans ce controleur qu'on gere les action et methode de l'interface central
    //Une liste des informations relatifs a l'utilisateur non obligatoire dans la BD
    public String[] specialisation = {"Neurologie", "Neurochirurgie", "Psychiatrie", "Neuro-oncologie", "Neuropsychologie"};
    public String[] status = {"Actif", "Inactif", "Suspendu"};
    public String[] sexe = {"M", "F", "AUTRE"};

    // Cette partie sert a afficher le nom de l'utilisateur recuperer depuis
    // La base de donnees sur l'interface
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

    //Cette methode sert a afficher les differentens informations de l'utilisateur sur la partie MODIFIER
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

    // La liste des specialisation d'un Dr par Selection
    public void profileSpecialList() {
        ObservableList<String> listData =
                FXCollections.observableArrayList(specialisation);

        profil_specialisation.setItems(listData);
    }

    // L'affichage en temps reel des specialisation d'un Dr
    public void initSpecialisationListener() {
        profil_specialisation.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                nav_userSpect.setText(newVal);
            }
        });
    }

    // La liste du status d'un Dr par Selection
    public void profileStatusList(){
        List<String> listST = new ArrayList<>();
        for(String date : status){
            listST.add(date);
        }
        ObservableList listData = FXCollections.observableList(listST);
        profil_status.setItems(listData);
    }

    // La liste du sexe d'un Dr par Selection
    public void profileSexeList(){
        List<String> listSE = new ArrayList<>();
        for(String date : sexe){
            listSE.add(date);
        }
        ObservableList listData = FXCollections.observableList(listSE);
        profil_sexe.setItems(listData);
    }

    // Cette Methode sert a importer la photo de profil dans la partie Setting
    public void profileDisplayImage() {

        String selectData =
                "SELECT * FROM docteur WHERE user_name = ?";

        connect = DataBase.connectDB();

        try {

            prepare = connect.prepareStatement(selectData);
            prepare.setString(1, Data.admin_username);

            result = prepare.executeQuery();

            if (result.next()) {

                String userImage = result.getString("user_image");

                // Vérification importante
                if (userImage != null && !userImage.isEmpty()) {

                    File file = new File(userImage);

                    // Vérifie si le fichier existe
                    if (file.exists()) {

                        String imagePath = file.toURI().toString();

                        Image topImage = new Image(
                                imagePath,
                                1088,
                                23,
                                false,
                                true
                        );

                        top_profil.setFill(new ImagePattern(topImage));

                        Image profileImage = new Image(
                                imagePath,
                                173,
                                98,
                                false,
                                true
                        );

                        profil_image.setFill(new ImagePattern(profileImage));

                    } else {

                        System.out.println("Image introuvable : " + userImage);
                    }
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

    // Cette section permet de modifier les informations relatifs a l'utilisateur dans la BD
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

        connect = DataBase.connectDB();

        try {

            // récupérer ancienne image si aucune nouvelle image choisie
            String currentImage = "";

            String getImage = "SELECT user_image FROM docteur WHERE user_name = ?";

            prepare = connect.prepareStatement(getImage);
            prepare.setString(1, Data.admin_username);

            result = prepare.executeQuery();

            if (result.next()) {
                currentImage = result.getString("user_image");
            }

            // si aucune nouvelle image
            String finalPath;

            if (Data.path == null || Data.path.isEmpty()) {
                finalPath = currentImage;
            } else {
                finalPath = Data.path;
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

            prepare = connect.prepareStatement(updateData);

            prepare.setString(1, profil_nomM.getText());
            prepare.setString(2, profil_mailM.getText());
            prepare.setString(3, profil_passM.getText());
            prepare.setString(4, profil_sexe.getSelectionModel().getSelectedItem());
            prepare.setString(5, profil_specialisation.getSelectionModel().getSelectedItem());
            prepare.setString(6, profil_status.getSelectionModel().getSelectedItem());
            prepare.setString(7, finalPath);

            prepare.setString(8, Data.admin_username);

            prepare.executeUpdate();

            // mise à jour session
            Data.admin_username = profil_nomM.getText();

            alert.successMessage("Mise à jour réussie");

            Data.path = null;

            displayAdminUsername();
            profileLabel();
            profileDisplayImage();

        } catch (Exception e) {
            e.printStackTrace();
            alert.errorMessage("Erreur lors de la mise à jour");
        }
    }

    // Cette Methode sert a importer la photo de profil dans les deux cercles dedier
    public void profileChange() {

        FileChooser open = new FileChooser();

        open.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(
                        "Image Files",
                        "*.png",
                        "*.jpg",
                        "*.jpeg",
                        "*.gif",
                        "*.bmp",
                        "*.webp"
                )
        );

        File file = open.showOpenDialog(profil_importBtn.getScene().getWindow());

        if (file != null) {

            try {

                // dossier destination
                String folder = "profile_images/";

                File directory = new File(folder);

                if (!directory.exists()) {
                    directory.mkdirs();
                }

                // nom unique image
                String fileName = System.currentTimeMillis()
                        + "_"
                        + file.getName();

                // destination finale
                Path destination = Paths.get(folder + fileName);

                // copie image
                Files.copy(
                        file.toPath(),
                        destination,
                        StandardCopyOption.REPLACE_EXISTING
                );

                // chemin sauvegardé DB
                Data.path = destination.toString();

                // affichage image
                String imagePath =
                        destination.toFile().toURI().toString();

                Image image = new Image(
                        imagePath,
                        173,
                        98,
                        false,
                        true
                );

                profil_image.setFill(new ImagePattern(image));
                top_profil.setFill(new ImagePattern(image));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Cette partie sert a afficher le nom d'utilisateur dans la section navBar
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

            nbr.setText("ffggg");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // La Methode permet de changer d'un module a un autre
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

    // la creation de l'horloge en temps reel
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

    public void affichageTotalDr(){

        String sql = "SELECT COUNT(*) AS total FROM docteur";

        connect = DataBase.connectDB();

        int getTp = 0;

        try{

            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            if(result.next()){

                getTp = result.getInt("total");
            }

            nbr.setText(String.valueOf(getTp));

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    // La partei Ressource au demarrage de l'application JAVA
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        runTime();
        displayAdminUsername();
        profileSpecialList();
        profileStatusList();
        profileSexeList();
        profilFields();
        profileLabel();
        initSpecialisationListener();
        profileDisplayImage();
        affichageTotalDr();
    }
}