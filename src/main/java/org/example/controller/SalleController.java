package org.example.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.scene.control.ScrollPane;
import org.example.Main;
import org.example.dao.ComplexeCulturelDAO;
import org.example.dao.SalleDAO;
import org.example.model.Salle;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class SalleController implements Initializable {

    @FXML private TableView<Salle> tableSalles;
    @FXML private TableColumn<Salle, Integer> colId;
    @FXML private TableColumn<Salle, String> colNom;
    @FXML private TableColumn<Salle, Integer> colCapacite;
    @FXML private TableColumn<Salle, String> colComplexe;
    @FXML private TextField searchField;
    @FXML private Label successLabel;

    private final SalleDAO salleDAO = new SalleDAO();
    private final ComplexeCulturelDAO complexeDAO = new ComplexeCulturelDAO();
    private ObservableList<Salle> toutesLesSalles;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colCapacite.setCellValueFactory(new PropertyValueFactory<>("capacite"));

        colComplexe.setCellValueFactory(cellData -> {
            String nomComplexe = complexeDAO.getNomById(cellData.getValue().getIdComplexe());
            return new javafx.beans.property.SimpleStringProperty(
                    nomComplexe != null ? nomComplexe : "N/A");
        });

        chargerSalles();
    }

    private void chargerSalles() {
        List<Salle> liste = salleDAO.getAll();
        toutesLesSalles = FXCollections.observableArrayList(liste);
        tableSalles.setItems(toutesLesSalles);
    }

    @FXML
    private void handleRecherche() {
        String recherche = searchField.getText().toLowerCase().trim();
        if (recherche.isEmpty()) {
            tableSalles.setItems(toutesLesSalles);
            return;
        }
        ObservableList<Salle> filtres = toutesLesSalles.stream()
                .filter(s -> s.getNom().toLowerCase().contains(recherche))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        tableSalles.setItems(filtres);
    }

    @FXML
    private void handleAjouter() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/SalleFormView.fxml"));
            ScrollPane formulaire = loader.load();

            SalleFormController formController = loader.getController();
            formController.setSalleController(this);

            StackPane parent = (StackPane) tableSalles.getScene().lookup("#contentArea");
            parent.getChildren().clear();
            parent.getChildren().add(formulaire);
        } catch (IOException e) {
            System.err.println("Erreur ouverture formulaire salle : " + e.getMessage());
        }
    }

    @FXML
    private void handleModifier() {
        Salle selectionne = tableSalles.getSelectionModel().getSelectedItem();
        if (selectionne == null) {
            afficherAlerte("Sélection requise", "Veuillez sélectionner une salle à modifier.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/SalleFormView.fxml"));
            ScrollPane formulaire = loader.load();

            SalleFormController formController = loader.getController();
            formController.setSalleController(this);
            formController.setSalle(selectionne);

            StackPane parent = (StackPane) tableSalles.getScene().lookup("#contentArea");
            parent.getChildren().clear();
            parent.getChildren().add(formulaire);
        } catch (IOException e) {
            System.err.println("Erreur ouverture formulaire modification salle : " + e.getMessage());
        }
    }

    @FXML
    private void handleSupprimer() {
        Salle selectionne = tableSalles.getSelectionModel().getSelectedItem();
        if (selectionne == null) {
            afficherAlerte("Sélection requise", "Veuillez sélectionner une salle à supprimer.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Supprimer la salle \"" + selectionne.getNom() + "\" ?");
        confirmation.initOwner(Main.getStage());

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                boolean succes = salleDAO.delete(selectionne.getId());
                if (succes) {
                    chargerSalles();
                    afficherSucces("Salle supprimée avec succès.");
                } else {
                    afficherAlerte("Erreur", "Impossible de supprimer la salle.");
                }
            }
        });
    }

    private void afficherAlerte(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(Main.getStage());
        alert.showAndWait();
    }

    public void afficherSucces(String message) {
        successLabel.setText("✅ " + message);
        successLabel.setVisible(true);
    }

    public void rafraichir() {
        chargerSalles();
    }
}