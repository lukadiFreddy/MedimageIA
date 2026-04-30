package com.example.medimageia;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class adminController implements Initializable {

    //Elementt centre de l'IA
    @FXML
    private AnchorPane centre_ia;
    @FXML
    private AnchorPane centre_iaImage;
    @FXML
    private AnchorPane centre_iaReponse;
    @FXML
    private Button centre_iaBtn;

    //Element centre Scan
    @FXML
    private AnchorPane center_scan;
    @FXML
    private TableColumn center_scanTab;

    //Element centre profil
    @FXML
    private AnchorPane centre_profil;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
