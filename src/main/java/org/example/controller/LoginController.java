package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.Main;
import org.example.dao.AdministrateurDAO;
import org.example.model.Administrateur;

public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private final AdministrateurDAO administrateurDAO = new AdministrateurDAO();

    @FXML
    private void handleLogin() {
        String email = emailField.getText().trim();
        String motDePasse = passwordField.getText().trim();

        if (email.isEmpty() || motDePasse.isEmpty()) {
            afficherErreur("Veuillez remplir tous les champs.");
            return;
        }

        Administrateur admin = administrateurDAO.getByEmail(email);

        if (admin == null) {
            afficherErreur("Email ou mot de passe incorrect.");
            return;
        }

        if (!at.favre.lib.crypto.bcrypt.BCrypt.verifyer()
                .verify(motDePasse.toCharArray(), admin.getMotDePasse()).verified) {
            afficherErreur("Email ou mot de passe incorrect.");
            return;
        }

        // Connexion réussie → Dashboard
        Main.naviguerVers("/fxml/DashboardView.fxml", controller -> {
            DashboardController dc = (DashboardController) controller;
            dc.setAdmin(admin);
        });
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