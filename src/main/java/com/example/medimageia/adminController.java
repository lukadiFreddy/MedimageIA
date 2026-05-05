package com.example.medimageia;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    // DB
    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;

    // ================= AFFICHER NOM =================
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

    // ================= SWITCH VUES =================
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

    // ================= TIME =================
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
    }
}