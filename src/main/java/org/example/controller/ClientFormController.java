package org.example.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import org.example.dao.ClientDAO;
import org.example.model.Client;

import java.io.IOException;

public class ClientFormController {

    @FXML private Label titreFormLabel;
    @FXML private TextField nomField;
    @FXML private TextField emailField;
    @FXML private TextField telephoneField;
    @FXML private TextField adresseField;
    @FXML private DatePicker dateNaissancePicker;
    @FXML private Label errorLabel;

    private final ClientDAO clientDAO = new ClientDAO();
    private Client clientAModifier = null;
    private ClientController clientController;

    public void setClientController(ClientController controller) {
        this.clientController = controller;
    }

    public void setClient(Client client) {
        this.clientAModifier = client;
        titreFormLabel.setText("Modifier un client");

        nomField.setText(client.getNom());
        emailField.setText(client.getEmail());
        telephoneField.setText(client.getTelephone() != null ? client.getTelephone() : "");
        adresseField.setText(client.getAdresse() != null ? client.getAdresse() : "");
        dateNaissancePicker.setValue(client.getDateNaissance());
    }

    @FXML
    private void handleSauvegarder() {
        if (nomField.getText().trim().isEmpty()) {
            afficherErreur("Le nom est obligatoire.");
            return;
        }

        if (emailField.getText().trim().isEmpty()) {
            afficherErreur("L'email est obligatoire.");
            return;
        }

        // Validation email
        String email = emailField.getText().trim();
        if (!email.matches("^[\\w._%+\\-]+@[\\w.\\-]+\\.(fr|com|net|org|eu)$")) {
            afficherErreur("Email invalide. Ex: prenom.nom@email.com");
            return;
        }

        // Validation téléphone
        String telephone = telephoneField.getText().trim();
        if (!telephone.isEmpty()) {
            if (!telephone.matches("^0[0-9]{9}$")) {
                afficherErreur("Téléphone invalide. Doit commencer par 0 et contenir 10 chiffres.");
                return;
            }
        }

        if (clientAModifier == null) {
            Client nouveau = new Client(
                    0,
                    nomField.getText().trim(),
                    email,
                    telephone,
                    adresseField.getText().trim(),
                    dateNaissancePicker.getValue()
            );

            boolean succes = clientDAO.insert(nouveau);
            if (succes) {
                clientController.rafraichir();
                clientController.afficherSucces("Client ajouté avec succès !");
                handleAnnuler();
            } else {
                afficherErreur("Erreur lors de l'ajout. L'email existe peut-être déjà.");
            }

        } else {
            clientAModifier.setNom(nomField.getText().trim());
            clientAModifier.setEmail(email);
            clientAModifier.setTelephone(telephone);
            clientAModifier.setAdresse(adresseField.getText().trim());
            clientAModifier.setDateNaissance(dateNaissancePicker.getValue());

            boolean succes = clientDAO.update(clientAModifier);
            if (succes) {
                clientController.rafraichir();
                clientController.afficherSucces("Client modifié avec succès !");
                handleAnnuler();
            } else {
                afficherErreur("Erreur lors de la modification.");
            }
        }
    }

    @FXML
    private void handleAnnuler() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ClientView.fxml"));
            ScrollPane vue = loader.load();

            StackPane parent = (StackPane) nomField.getScene().lookup("#contentArea");
            parent.getChildren().clear();
            parent.getChildren().add(vue);
        } catch (IOException e) {
            System.err.println("Erreur annulation : " + e.getMessage());
        }
    }

    private void afficherErreur(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
}