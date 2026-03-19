package org.example.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import org.example.dao.BilletDAO;
import org.example.dao.ClientDAO;
import org.example.dao.SeanceDAO;
import org.example.model.BilletAffichage;
import org.example.model.Client;
import org.example.model.SeanceAffichage;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class BilletModifFormController implements Initializable {

    @FXML private TextField numeroField;
    @FXML private ComboBox<Client> clientCombo;
    @FXML private ComboBox<SeanceAffichage> seanceCombo;
    @FXML private TextField prixField;
    @FXML private ComboBox<String> statutCombo;
    @FXML private Label errorLabel;

    private final BilletDAO billetDAO = new BilletDAO();
    private final ClientDAO clientDAO = new ClientDAO();
    private final SeanceDAO seanceDAO = new SeanceDAO();

    private BilletAffichage billetAModifier;
    private BilletController billetController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Charger les clients
        List<Client> clients = clientDAO.getAll();
        clientCombo.setItems(FXCollections.observableArrayList(clients));

        // Charger les séances
        List<SeanceAffichage> seances = seanceDAO.getAllAvecDetails();
        seanceCombo.setItems(FXCollections.observableArrayList(seances));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        seanceCombo.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(SeanceAffichage item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setText(null);
                else setText(item.getNomEvenement() + " — "
                        + item.getDateHeure().format(formatter)
                        + " (" + item.getNomSalle() + ")");
            }
        });
        seanceCombo.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(SeanceAffichage item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setText(null);
                else setText(item.getNomEvenement() + " — "
                        + item.getDateHeure().format(formatter));
            }
        });

        // Charger les statuts
        statutCombo.setItems(FXCollections.observableArrayList("valide", "annule", "rembourse"));
    }

    public void setBilletController(BilletController controller) {
        this.billetController = controller;
    }

    public void setBillet(BilletAffichage billet) {
        this.billetAModifier = billet;

        numeroField.setText(billet.getNumeroUnique());
        prixField.setText(String.valueOf(billet.getPrix()));
        statutCombo.setValue(billet.getStatut());

        // Pré-sélectionner le client
        clientCombo.getItems().stream()
                .filter(c -> c.getId() == billet.getIdClient())
                .findFirst()
                .ifPresent(c -> clientCombo.setValue(c));

        // Pré-sélectionner la séance
        seanceCombo.getItems().stream()
                .filter(s -> s.getId() == billet.getIdSeance())
                .findFirst()
                .ifPresent(s -> seanceCombo.setValue(s));
    }

    @FXML
    private void handleSauvegarder() {
        if (clientCombo.getValue() == null) {
            afficherErreur("Veuillez sélectionner un client.");
            return;
        }
        if (seanceCombo.getValue() == null) {
            afficherErreur("Veuillez sélectionner une séance.");
            return;
        }
        if (statutCombo.getValue() == null) {
            afficherErreur("Veuillez sélectionner un statut.");
            return;
        }

        double prix;
        try {
            prix = Double.parseDouble(prixField.getText().trim().replace(",", "."));
        } catch (NumberFormatException e) {
            afficherErreur("Le prix doit être un nombre valide.");
            return;
        }

        boolean succes = billetDAO.updateComplet(
                billetAModifier.getId(),
                clientCombo.getValue().getId(),
                seanceCombo.getValue().getId(),
                prix,
                statutCombo.getValue()
        );

        if (succes) {
            billetController.rafraichir();
            billetController.afficherSucces("Billet modifié avec succès !");
            handleAnnuler();
        } else {
            afficherErreur("Erreur lors de la modification.");
        }
    }

    @FXML
    private void handleAnnuler() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/BilletView.fxml"));
            ScrollPane vue = loader.load();
            StackPane parent = (StackPane) numeroField.getScene().lookup("#contentArea");
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