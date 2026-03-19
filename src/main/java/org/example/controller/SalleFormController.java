package org.example.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.control.ScrollPane;
import org.example.dao.ComplexeCulturelDAO;
import org.example.dao.SalleDAO;
import org.example.model.ComplexeCulturel;
import org.example.model.Salle;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SalleFormController implements Initializable {

    @FXML private Label titreFormLabel;
    @FXML private TextField nomField;
    @FXML private TextField capaciteField;
    @FXML private ComboBox<ComplexeCulturel> complexeCombo;
    @FXML private Label errorLabel;

    private final SalleDAO salleDAO = new SalleDAO();
    private final ComplexeCulturelDAO complexeDAO = new ComplexeCulturelDAO();
    private Salle salleAModifier = null;
    private SalleController salleController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<ComplexeCulturel> complexes = complexeDAO.getAll();
        complexeCombo.setItems(FXCollections.observableArrayList(complexes));
    }

    public void setSalleController(SalleController controller) {
        this.salleController = controller;
    }

    public void setSalle(Salle salle) {
        this.salleAModifier = salle;
        titreFormLabel.setText("Modifier une salle");
        nomField.setText(salle.getNom());
        capaciteField.setText(String.valueOf(salle.getCapacite()));
        complexeCombo.getItems().stream()
                .filter(c -> c.getId() == salle.getIdComplexe())
                .findFirst()
                .ifPresent(c -> complexeCombo.setValue(c));
    }

    @FXML
    private void handleSauvegarder() {
        if (nomField.getText().trim().isEmpty()) {
            afficherErreur("Le nom est obligatoire.");
            return;
        }
        if (complexeCombo.getValue() == null) {
            afficherErreur("Veuillez sélectionner un complexe.");
            return;
        }

        int capacite;
        try {
            capacite = Integer.parseInt(capaciteField.getText().trim());
            if (capacite <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            afficherErreur("La capacité doit être un nombre entier positif.");
            return;
        }

        if (salleAModifier == null) {
            Salle nouvelle = new Salle(0, nomField.getText().trim(),
                    capacite, complexeCombo.getValue().getId());
            boolean succes = salleDAO.insert(nouvelle);
            if (succes) {
                salleController.rafraichir();
                salleController.afficherSucces("Salle ajoutée avec succès !");
                handleAnnuler();
            } else {
                afficherErreur("Erreur lors de l'ajout.");
            }
        } else {
            salleAModifier.setNom(nomField.getText().trim());
            salleAModifier.setCapacite(capacite);
            salleAModifier.setIdComplexe(complexeCombo.getValue().getId());
            boolean succes = salleDAO.update(salleAModifier);
            if (succes) {
                salleController.rafraichir();
                salleController.afficherSucces("Salle modifiée avec succès !");
                handleAnnuler();
            } else {
                afficherErreur("Erreur lors de la modification.");
            }
        }
    }

    @FXML
    private void handleAnnuler() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/SalleView.fxml"));
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