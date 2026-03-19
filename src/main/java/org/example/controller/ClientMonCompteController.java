package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.dao.ClientDAO;
import org.example.model.Client;

public class ClientMonCompteController {

    @FXML private TextField nomField;
    @FXML private TextField emailField;
    @FXML private TextField telephoneField;
    @FXML private TextField adresseField;
    @FXML private DatePicker dateNaissancePicker;
    @FXML private PasswordField ancienPasswordField;
    @FXML private PasswordField nouveauPasswordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label messageLabel;

    private final ClientDAO clientDAO = new ClientDAO();
    private Client client;
    private ClientDashboardController dashboardController;

    public void setClient(Client client, ClientDashboardController dashboardController) {
        this.client = client;
        this.dashboardController = dashboardController;

        // Pré-remplir les champs
        nomField.setText(client.getNom());
        emailField.setText(client.getEmail());
        telephoneField.setText(client.getTelephone() != null ? client.getTelephone() : "");
        adresseField.setText(client.getAdresse() != null ? client.getAdresse() : "");
        dateNaissancePicker.setValue(client.getDateNaissance());
    }

    @FXML
    private void handleSauvegarder() {
        // Validation nom
        if (nomField.getText().trim().isEmpty()) {
            afficherErreur("Le nom est obligatoire.");
            return;
        }

        // Validation email
        if (emailField.getText().trim().isEmpty()) {
            afficherErreur("L'email est obligatoire.");
            return;
        }
        if (!emailField.getText().trim().matches("^[\\w._%+\\-]+@[\\w.\\-]+\\.(fr|com|net|org|eu)$")) {
            afficherErreur("Format d'email invalide.");
            return;
        }

        // Validation téléphone
        String telephone = telephoneField.getText().trim();
        if (!telephone.isEmpty() && !telephone.matches("^0[0-9]{9}$")) {
            afficherErreur("Téléphone invalide. Doit commencer par 0 et contenir 10 chiffres.");
            return;
        }

        // Validation mot de passe si renseigné
        String ancienMdp = ancienPasswordField.getText();
        String nouveauMdp = nouveauPasswordField.getText();
        String confirmMdp = confirmPasswordField.getText();

        if (!ancienMdp.isEmpty() || !nouveauMdp.isEmpty() || !confirmMdp.isEmpty()) {
            if (!at.favre.lib.crypto.bcrypt.BCrypt.verifyer()
                    .verify(ancienMdp.toCharArray(), client.getMotDePasse()).verified) {
                afficherErreur("Le mot de passe actuel est incorrect.");
                return;
            }
            if (nouveauMdp.length() < 6) {
                afficherErreur("Le nouveau mot de passe doit contenir au moins 6 caractères.");
                return;
            }
            if (!nouveauMdp.equals(confirmMdp)) {
                afficherErreur("Les nouveaux mots de passe ne correspondent pas.");
                return;
            }
            client.setMotDePasse(at.favre.lib.crypto.bcrypt.BCrypt.withDefaults()
                    .hashToString(12, nouveauMdp.toCharArray()));
        }

        // Mettre à jour le client
        client.setNom(nomField.getText().trim());
        client.setEmail(emailField.getText().trim());
        client.setTelephone(telephone);
        client.setAdresse(adresseField.getText().trim());
        client.setDateNaissance(dateNaissancePicker.getValue());

        boolean succes = clientDAO.update(client);
        if (succes) {
            afficherSucces("Modifications sauvegardées avec succès !");
            // Mettre à jour le nom affiché dans la navbar
            if (dashboardController != null) {
                dashboardController.rafraichirNomClient(client.getNom());
            }
        } else {
            afficherErreur("Erreur lors de la sauvegarde.");
        }
    }

    @FXML
    private void handleAnnuler() {
        if (dashboardController != null) {
            dashboardController.showEvenements();
        }
    }

    private void afficherErreur(String message) {
        messageLabel.setText("❌ " + message);
        messageLabel.setStyle("-fx-text-fill: #f44336; -fx-font-size: 13px;");
        messageLabel.setVisible(true);
    }

    private void afficherSucces(String message) {
        messageLabel.setText("✅ " + message);
        messageLabel.setStyle("-fx-text-fill: #4caf50; -fx-font-size: 13px;");
        messageLabel.setVisible(true);
    }
}