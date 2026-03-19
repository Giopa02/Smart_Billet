package org.example.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import org.example.dao.BilletDAO;
import org.example.dao.ClientDAO;
import org.example.dao.SeanceDAO;
import org.example.model.Billet;
import org.example.model.Client;
import org.example.model.SeanceAffichage;
import java.util.stream.Collectors;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class BilletFormController implements Initializable {

    @FXML private ComboBox<Client> clientCombo;
    @FXML private ComboBox<SeanceAffichage> seanceCombo;
    @FXML private TextField prixField;
    @FXML private Label errorLabel;

    private final BilletDAO billetDAO = new BilletDAO();
    private final ClientDAO clientDAO = new ClientDAO();
    private final SeanceDAO seanceDAO = new SeanceDAO();

    private BilletController billetController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Charger les clients avec autocomplétion
        List<Client> tousLesClients = clientDAO.getAll();
        clientCombo.setEditable(true);
        clientCombo.setItems(FXCollections.observableArrayList(tousLesClients));

        clientCombo.getEditor().textProperty().addListener((obs, oldVal, newVal) -> {
            if (clientCombo.getValue() instanceof Client) return;

            if (newVal == null || newVal.isEmpty()) {
                clientCombo.setItems(FXCollections.observableArrayList(tousLesClients));
                clientCombo.hide();
                return;
            }

            ObservableList<Client> filtres = tousLesClients.stream()
                    .filter(c -> c.getNom().toLowerCase().contains(newVal.toLowerCase()))
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));
            clientCombo.setItems(filtres);
            if (!filtres.isEmpty()) clientCombo.show();
        });

        clientCombo.getEditor().setOnKeyPressed(e -> {
            if (clientCombo.getValue() instanceof Client) {
                clientCombo.setValue(null);
            }
        });

        // Charger les séances avec détails
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

        // Pré-remplir le prix quand une séance est sélectionnée
        seanceCombo.setOnAction(e -> {
            SeanceAffichage seance = seanceCombo.getValue();
            if (seance != null) {
                prixField.setText(String.valueOf(seance.getPrix()));
            }
        });
    }

    public void setBilletController(BilletController controller) {
        this.billetController = controller;
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

        double prix;
        try {
            prix = Double.parseDouble(prixField.getText().trim().replace(",", "."));
        } catch (NumberFormatException e) {
            afficherErreur("Le prix doit être un nombre valide.");
            return;
        }

        // Générer le numéro unique
        String numeroUnique = genererNumeroUnique();

        Billet nouveau = new Billet(
                0,
                numeroUnique,
                clientCombo.getValue().getId(),
                seanceCombo.getValue().getId(),
                LocalDateTime.now(),
                prix,
                "valide"
        );

        boolean succes = billetDAO.insert(nouveau);
        if (succes) {
            billetController.rafraichir();
            billetController.afficherSucces("Billet " + numeroUnique + " créé avec succès !");
            handleAnnuler();
        } else {
            afficherErreur("Erreur lors de la création du billet.");
        }
    }

    private String genererNumeroUnique() {
        int annee = LocalDateTime.now().getYear();
        // Récupérer tous les billets pour trouver le prochain numéro
        int prochain = billetDAO.getAll().size() + 1;
        return String.format("TNG-%d-%05d", annee, prochain);
    }

    @FXML
    private void handleAnnuler() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/BilletView.fxml"));
            ScrollPane vue = loader.load();

            StackPane parent = (StackPane) prixField.getScene().lookup("#contentArea");
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