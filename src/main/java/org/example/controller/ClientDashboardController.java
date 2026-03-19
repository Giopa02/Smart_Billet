package org.example.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import org.example.Main;
import org.example.model.Client;

import java.io.IOException;

public class ClientDashboardController {

    @FXML private StackPane contentArea;
    @FXML private Label clientNomLabel;
    @FXML private Button btnEvenements;
    @FXML private Button btnMesBillets;
    @FXML private Button btnMonCompte;

    private Client clientConnecte;

    public void setClient(Client client) {
        this.clientConnecte = client;
        clientNomLabel.setText("👤 " + client.getNom());
        // Afficher les événements par défaut
        setActiveButton(btnEvenements);
        chargerVue("/fxml/ClientEvenementsView.fxml");
    }

    @FXML
    public void showEvenements() {
        setActiveButton(btnEvenements);
        chargerVue("/fxml/ClientEvenementsView.fxml");
    }

    @FXML
    private void showMonCompte() {
        setActiveButton(btnMonCompte);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ClientMonCompteView.fxml"));
            ScrollPane vue = loader.load();

            ClientMonCompteController controller = loader.getController();
            controller.setClient(clientConnecte, this);

            contentArea.getChildren().clear();
            contentArea.getChildren().add(vue);
        } catch (IOException e) {
            System.err.println("Erreur chargement mon compte : " + e.getMessage());
        }
    }

    public void rafraichirNomClient(String nom) {
        clientNomLabel.setText("👤 " + nom);
    }

    @FXML
    private void showMesBillets() {
        setActiveButton(btnMesBillets);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ClientMesBilletsView.fxml"));
            ScrollPane vue = loader.load();

            ClientMesBilletsController controller = loader.getController();
            controller.setClient(clientConnecte);

            contentArea.getChildren().clear();
            contentArea.getChildren().add(vue);
        } catch (IOException e) {
            System.err.println("Erreur chargement mes billets : " + e.getMessage());
        }
    }

    @FXML
    private void handleDeconnexion() {
        Main.naviguerVers("/fxml/SelectionView.fxml");
    }

    private void chargerVue(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            ScrollPane vue = loader.load();

            // Passer le client si c'est la vue événements
            if (fxmlPath.contains("ClientEvenementsView")) {
                ClientEvenementsController controller = loader.getController();
                controller.setClient(clientConnecte);
            }

            contentArea.getChildren().clear();
            contentArea.getChildren().add(vue);
        } catch (IOException e) {
            System.err.println("Erreur chargement vue : " + e.getMessage());
        }
    }

    private void setActiveButton(Button active) {
        for (Button btn : new Button[]{btnEvenements, btnMesBillets, btnMonCompte}) {
            btn.getStyleClass().remove("sidebar-btn-active");
        }
        active.getStyleClass().add("sidebar-btn-active");
    }
}