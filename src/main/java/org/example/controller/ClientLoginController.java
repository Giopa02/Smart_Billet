package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.Main;
import org.example.dao.ClientDAO;
import org.example.model.Client;

public class ClientLoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private final ClientDAO clientDAO = new ClientDAO();

    @FXML
    private void handleLogin() {
        String email = emailField.getText().trim();
        String motDePasse = passwordField.getText().trim();

        if (email.isEmpty() || motDePasse.isEmpty()) {
            afficherErreur("Veuillez remplir tous les champs.");
            return;
        }

        Client client = clientDAO.getByEmail(email);

        if (client == null || client.getMotDePasse() == null ||
                at.favre.lib.crypto.bcrypt.BCrypt.verifyer()
                            .verify(motDePasse.toCharArray(), client.getMotDePasse()).verified == false) {
                                afficherErreur("Email ou mot de passe incorrect.");
                                return;
        }

        // Connexion réussie
        Main.naviguerVers("/fxml/ClientDashboardView.fxml", controller -> {
            ClientDashboardController dc = (ClientDashboardController) controller;
            dc.setClient(client);
        });
    }

    @FXML
    private void handleAllerInscription() {
        Main.naviguerVers("/fxml/ClientInscriptionView.fxml");
    }

    @FXML
    private void handleRetour() {
        Main.naviguerVers("/fxml/SelectionView.fxml");
    }

    private void afficherErreur(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
}