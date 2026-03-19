package org.example.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import java.io.IOException;
import org.example.Main;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import org.example.model.Administrateur;

public class DashboardController {

    @FXML private Label adminNomLabel;
    @FXML private StackPane contentArea;

    @FXML private Button btnEvenements;
    @FXML private Button btnSeances;
    @FXML private Button btnClients;
    @FXML private Button btnBillets;
    @FXML private Button btnSalles;

    public void setAdmin(Administrateur admin) {
        adminNomLabel.setText(admin.getNom());
    }

    @FXML
    private void showEvenements() {
        setActiveButton(btnEvenements);
        chargerVue("/fxml/EvenementView.fxml");
    }

    @FXML
    private void showSeances() {
        setActiveButton(btnSeances);
        chargerVue("/fxml/SeanceView.fxml");
    }

    @FXML
    private void showClients() {
        setActiveButton(btnClients);
        chargerVue("/fxml/ClientView.fxml");
    }

    @FXML
    private void showBillets() {
        setActiveButton(btnBillets);
        chargerVue("/fxml/BilletView.fxml");
    }

    @FXML
    private void showSalles() {
        setActiveButton(btnSalles);
        chargerVue("/fxml/SalleView.fxml");
    }

    @FXML
    private void handleDeconnexion() {
        Main.naviguerVers("/fxml/SelectionView.fxml");
    }

    private void chargerVue(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            ScrollPane vue = loader.load();
            contentArea.getChildren().clear();
            contentArea.getChildren().add(vue);
        } catch (IOException e) {
            System.err.println("Erreur chargement vue " + fxmlPath + " : " + e.getMessage());
        }
    }

    private void setActiveButton(Button active) {
        for (Button btn : new Button[]{btnEvenements, btnSeances, btnClients, btnBillets, btnSalles}) {
            btn.getStyleClass().remove("sidebar-btn-active");
        }
        active.getStyleClass().add("sidebar-btn-active");
    }
}