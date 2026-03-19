package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.Main;
import org.example.dao.ClientDAO;
import org.example.model.Client;

public class ClientInscriptionController {

    @FXML private TextField nomField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private TextField telephoneField;
    @FXML private TextField adresseField;
    @FXML private DatePicker dateNaissancePicker;
    @FXML private Label errorLabel;

    private final ClientDAO clientDAO = new ClientDAO();

    @FXML
    private void handleInscription() {
        // Validations champs obligatoires
        if (nomField.getText().trim().isEmpty()) {
            afficherErreur("Le nom est obligatoire.");
            return;
        }

        if (emailField.getText().trim().isEmpty()) {
            afficherErreur("L'email est obligatoire.");
            return;
        }

        // Validation format email
        if (!emailField.getText().trim().matches("^[\\w._%+\\-]+@[\\w.\\-]+\\.(fr|com|net|org|eu)$")) {
            afficherErreur("Format d'email invalide.");
            return;
        }

        if (passwordField.getText().trim().isEmpty()) {
            afficherErreur("Le mot de passe est obligatoire.");
            return;
        }

        // Validation longueur mot de passe
        if (passwordField.getText().trim().length() < 6) {
            afficherErreur("Le mot de passe doit contenir au moins 6 caractères.");
            return;
        }

        // Validation confirmation mot de passe
        if (!passwordField.getText().equals(confirmPasswordField.getText())) {
            afficherErreur("Les mots de passe ne correspondent pas.");
            return;
        }

        // Validation téléphone si renseigné
        String telephone = telephoneField.getText().trim();
        if (!telephone.isEmpty() && !telephone.matches("^0[0-9]{9}$")) {
            afficherErreur("Téléphone invalide. Doit commencer par 0 et contenir 10 chiffres.");
            return;
        }

        // Vérifier si l'email existe déjà
        if (clientDAO.getByEmail(emailField.getText().trim()) != null) {
            afficherErreur("Un compte avec cet email existe déjà.");
            return;
        }

        // Créer le client
        String motDePasseHashe = at.favre.lib.crypto.bcrypt.BCrypt.withDefaults()
                .hashToString(12, passwordField.getText().trim().toCharArray());

        Client nouveau = new Client(
                0,
                nomField.getText().trim(),
                emailField.getText().trim(),
                motDePasseHashe,
                telephone,
                adresseField.getText().trim(),
                dateNaissancePicker.getValue()
        );

        boolean succes = clientDAO.insert(nouveau);
        if (succes) {
            // Connexion automatique après inscription
            Client clientCree = clientDAO.getByEmail(emailField.getText().trim());
            Main.naviguerVers("/fxml/ClientDashboardView.fxml", controller -> {
                ClientDashboardController dc = (ClientDashboardController) controller;
                dc.setClient(clientCree);
            });
        } else {
            afficherErreur("Erreur lors de la création du compte.");
        }
    }

    @FXML
    private void handleAllerConnexion() {
        Main.naviguerVers("/fxml/ClientLoginView.fxml");
    }

    private void afficherErreur(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
}