package org.example.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import org.example.dao.EvenementDAO;
import org.example.dao.SalleDAO;
import org.example.dao.SeanceDAO;
import org.example.model.Evenement;
import org.example.model.Salle;
import org.example.model.Seance;
import org.example.model.SeanceAffichage;
import javafx.scene.control.ScrollPane;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;

public class SeanceFormController implements Initializable {

    @FXML private Label titreFormLabel;
    @FXML private ComboBox<Evenement> evenementCombo;
    @FXML private ComboBox<Salle> salleCombo;
    @FXML private DatePicker datePicker;
    @FXML private TextField heureField;
    @FXML private TextField minutesField;
    @FXML private TextField prixField;
    @FXML private TextField capaciteField;
    @FXML private Label errorLabel;

    private final SeanceDAO seanceDAO = new SeanceDAO();
    private final EvenementDAO evenementDAO = new EvenementDAO();
    private final SalleDAO salleDAO = new SalleDAO();

    private SeanceAffichage seanceAModifier = null;
    private SeanceController seanceController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Charger les événements dans la ComboBox
        List<Evenement> evenements = evenementDAO.getAll();
        evenementCombo.setItems(FXCollections.observableArrayList(evenements));

        // Charger les salles dans la ComboBox
        List<Salle> salles = salleDAO.getAll();
        salleCombo.setItems(FXCollections.observableArrayList(salles));

        // Afficher automatiquement la capacité quand une salle est sélectionnée
        salleCombo.setOnAction(e -> {
            Salle salle = salleCombo.getValue();
            if (salle != null) {
                capaciteField.setText(String.valueOf(salle.getCapacite()));
            }
        });
    }

    public void setSeanceController(SeanceController controller) {
        this.seanceController = controller;
    }

    public void setSeance(SeanceAffichage seance) {
        this.seanceAModifier = seance;
        titreFormLabel.setText("Modifier une séance");

        // Pré-sélectionner l'événement
        evenementCombo.getItems().stream()
                .filter(e -> e.getId() == seance.getIdEvenement())
                .findFirst()
                .ifPresent(e -> evenementCombo.setValue(e));

        // Pré-sélectionner la salle
        salleCombo.getItems().stream()
                .filter(s -> s.getId() == seance.getIdSalle())
                .findFirst()
                .ifPresent(s -> salleCombo.setValue(s));

        datePicker.setValue(seance.getDateHeure().toLocalDate());
        heureField.setText(String.valueOf(seance.getDateHeure().getHour()));
        minutesField.setText(String.format("%02d", seance.getDateHeure().getMinute()));
        prixField.setText(String.valueOf(seance.getPrix()));
        capaciteField.setText(String.valueOf(seance.getCapacite()));
    }

    @FXML
    private void handleSauvegarder() {
        if (evenementCombo.getValue() == null) {
            afficherErreur("Veuillez sélectionner un événement.");
            return;
        }
        if (salleCombo.getValue() == null) {
            afficherErreur("Veuillez sélectionner une salle.");
            return;
        }
        if (datePicker.getValue() == null) {
            afficherErreur("Veuillez sélectionner une date.");
            return;
        }

        int heure, minutes;
        double prix;

        try {
            heure = Integer.parseInt(heureField.getText().trim());
            if (heure < 0 || heure > 23) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            afficherErreur("L'heure doit être un nombre entre 0 et 23.");
            return;
        }

        try {
            minutes = Integer.parseInt(minutesField.getText().trim());
            if (minutes < 0 || minutes > 59) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            afficherErreur("Les minutes doivent être entre 0 et 59.");
            return;
        }

        try {
            prix = Double.parseDouble(prixField.getText().trim().replace(",", "."));
        } catch (NumberFormatException e) {
            afficherErreur("Le prix doit être un nombre valide.");
            return;
        }

        // Validation et mise à jour capacité salle
        String capaciteStr = capaciteField.getText().trim();
        if (!capaciteStr.isEmpty()) {
            try {
                int capacite = Integer.parseInt(capaciteStr);
                if (capacite <= 0) {
                    afficherErreur("La capacité doit être un nombre positif.");
                    return;
                }
                salleDAO.updateCapacite(salleCombo.getValue().getId(), capacite);
            } catch (NumberFormatException e) {
                afficherErreur("La capacité doit être un nombre entier.");
                return;
            }
        }

        LocalDateTime dateHeure = LocalDateTime.of(
                datePicker.getValue(), java.time.LocalTime.of(heure, minutes));

        if (seanceAModifier == null) {
            Seance nouvelle = new Seance(
                    0,
                    evenementCombo.getValue().getId(),
                    salleCombo.getValue().getId(),
                    dateHeure,
                    prix
            );

            boolean succes = seanceDAO.insert(nouvelle);
            if (succes) {
                seanceController.rafraichir();
                seanceController.afficherSucces("Séance ajoutée avec succès !");
                handleAnnuler();
            } else {
                afficherErreur("Erreur lors de l'ajout.");
            }

        } else {
            Seance modifiee = new Seance(
                    seanceAModifier.getId(),
                    evenementCombo.getValue().getId(),
                    salleCombo.getValue().getId(),
                    dateHeure,
                    prix
            );

            boolean succes = seanceDAO.update(modifiee);
            if (succes) {
                seanceController.rafraichir();
                seanceController.afficherSucces("Séance modifiée avec succès !");
                handleAnnuler();
            } else {
                afficherErreur("Erreur lors de la modification.");
            }
        }
    }

    @FXML
    private void handleAnnuler() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/SeanceView.fxml"));
            ScrollPane vue = loader.load();

            StackPane parent = (StackPane) titreFormLabel.getScene().lookup("#contentArea");
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