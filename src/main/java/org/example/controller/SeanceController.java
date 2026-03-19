package org.example.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import org.example.dao.SalleDAO;
import org.example.dao.SeanceDAO;
import org.example.model.Salle;
import org.example.model.SeanceAffichage;
import org.example.Main;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class SeanceController implements Initializable {

    @FXML private TableView<SeanceAffichage> tableSeances;
    @FXML private TableColumn<SeanceAffichage, Integer> colId;
    @FXML private TableColumn<SeanceAffichage, String> colEvenement;
    @FXML private TableColumn<SeanceAffichage, String> colSalle;
    @FXML private TableColumn<SeanceAffichage, LocalDateTime> colDateHeure;
    @FXML private TableColumn<SeanceAffichage, String> colPlacesRestantes;
    @FXML private TableColumn<SeanceAffichage, Double> colPrix;
    @FXML private TextField searchField;

    // Filtres
    @FXML private ComboBox<Salle> filtreSalle;
    @FXML private TextField filtrePrixMin;
    @FXML private TextField filtrePrixMax;
    @FXML private DatePicker filtreDateDebut;
    @FXML private DatePicker filtreDateFin;

    private final SeanceDAO seanceDAO = new SeanceDAO();
    private final SalleDAO salleDAO = new SalleDAO();
    private ObservableList<SeanceAffichage> toutesLesSeances;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colEvenement.setCellValueFactory(new PropertyValueFactory<>("nomEvenement"));
        colSalle.setCellValueFactory(new PropertyValueFactory<>("nomSalle"));
        colDateHeure.setCellValueFactory(new PropertyValueFactory<>("dateHeure"));
        colPrix.setCellValueFactory(new PropertyValueFactory<>("prix"));

        colDateHeure.setCellFactory(col -> new TableCell<>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : formatter.format(item));
            }
        });

        colPlacesRestantes.setCellValueFactory(cellData -> {
            int places = seanceDAO.getPlacesRestantes(cellData.getValue().getId());
            return new javafx.beans.property.SimpleStringProperty(places + " / " +
                    cellData.getValue().getCapacite());
        });

        // Charger les salles dans le filtre
        List<Salle> salles = salleDAO.getAll();
        filtreSalle.setItems(FXCollections.observableArrayList(salles));

        chargerSeances();
    }

    private void chargerSeances() {
        List<SeanceAffichage> liste = seanceDAO.getAllAvecDetails();
        toutesLesSeances = FXCollections.observableArrayList(liste);
        tableSeances.setItems(null);
        tableSeances.setItems(toutesLesSeances);
    }

    @FXML
    private void handleRecherche() {
        appliquerFiltres();
    }

    @FXML
    private void handleFiltrer() {
        appliquerFiltres();
    }

    @FXML
    private void handleReinitialiser() {
        searchField.clear();
        filtreSalle.setValue(null);
        filtrePrixMin.clear();
        filtrePrixMax.clear();
        filtreDateDebut.setValue(null);
        filtreDateFin.setValue(null);
        tableSeances.setItems(toutesLesSeances);
    }

    private void appliquerFiltres() {
        String recherche = searchField.getText().toLowerCase().trim();
        Salle salle = filtreSalle.getValue();
        double prixMin = parseDouble(filtrePrixMin.getText(), 0);
        double prixMax = parseDouble(filtrePrixMax.getText(), Double.MAX_VALUE);
        LocalDate dateDebut = filtreDateDebut.getValue();
        LocalDate dateFin = filtreDateFin.getValue();

        ObservableList<SeanceAffichage> filtres = toutesLesSeances.stream()
                .filter(s -> {
                    boolean matchRecherche = recherche.isEmpty()
                            || s.getNomEvenement().toLowerCase().contains(recherche)
                            || s.getNomSalle().toLowerCase().contains(recherche);

                    boolean matchSalle = salle == null
                            || s.getNomSalle().equals(salle.getNom());

                    boolean matchPrix = s.getPrix() >= prixMin && s.getPrix() <= prixMax;

                    boolean matchDate = true;
                    if (dateDebut != null) {
                        matchDate = !s.getDateHeure().toLocalDate().isBefore(dateDebut);
                    }
                    if (dateFin != null) {
                        matchDate = matchDate && !s.getDateHeure().toLocalDate().isAfter(dateFin);
                    }

                    return matchRecherche && matchSalle && matchPrix && matchDate;
                })
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        tableSeances.setItems(filtres);
    }

    private double parseDouble(String text, double defaut) {
        try {
            return text.trim().isEmpty() ? defaut : Double.parseDouble(text.trim().replace(",", "."));
        } catch (NumberFormatException e) {
            return defaut;
        }
    }

    @FXML
    private void handleAjouter() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/SeanceFormView.fxml"));
            ScrollPane formulaire = loader.load();

            SeanceFormController formController = loader.getController();
            formController.setSeanceController(this);

            StackPane parent = (StackPane) tableSeances.getScene().lookup("#contentArea");
            parent.getChildren().clear();
            parent.getChildren().add(formulaire);

        } catch (IOException e) {
            System.err.println("Erreur ouverture formulaire : " + e.getMessage());
        }
    }

    @FXML
    private void handleModifier() {
        SeanceAffichage selectionne = tableSeances.getSelectionModel().getSelectedItem();
        if (selectionne == null) {
            afficherAlerte("Sélection requise", "Veuillez sélectionner une séance à modifier.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/SeanceFormView.fxml"));
            ScrollPane formulaire = loader.load();

            SeanceFormController formController = loader.getController();
            formController.setSeanceController(this);
            formController.setSeance(selectionne);

            StackPane parent = (StackPane) tableSeances.getScene().lookup("#contentArea");
            parent.getChildren().clear();
            parent.getChildren().add(formulaire);

        } catch (IOException e) {
            System.err.println("Erreur ouverture formulaire modification : " + e.getMessage());
        }
    }

    @FXML
    private void handleSupprimer() {
        SeanceAffichage selectionne = tableSeances.getSelectionModel().getSelectedItem();
        if (selectionne == null) {
            afficherAlerte("Sélection requise", "Veuillez sélectionner une séance à supprimer.");
            return;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Supprimer la séance \"" + selectionne.getNomEvenement()
                + "\" du " + selectionne.getDateHeure().format(formatter) + " ?");

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                boolean succes = seanceDAO.delete(selectionne.getId());
                if (succes) {
                    chargerSeances();
                    afficherSucces("Séance supprimée avec succès.");
                } else {
                    afficherAlerte("Erreur", "Impossible de supprimer la séance.");
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
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(Main.getStage());
        alert.showAndWait();
    }

    public void rafraichir() {
        chargerSeances();
    }

    public void fermerFormulaire() {
        StackPane parent = (StackPane) tableSeances.getScene().lookup("#contentArea");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/SeanceView.fxml"));
            parent.getChildren().clear();
            parent.getChildren().add(loader.load());
        } catch (IOException e) {
            System.err.println("Erreur fermeture formulaire : " + e.getMessage());
        }
    }
}