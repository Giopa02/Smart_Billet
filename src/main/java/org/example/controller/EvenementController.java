package org.example.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.dao.CategorieDAO;
import org.example.dao.EvenementDAO;
import org.example.model.Categorie;
import org.example.model.Evenement;
import org.example.Main;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class EvenementController implements Initializable {

    @FXML private TableView<Evenement> tableEvenements;
    @FXML private TableColumn<Evenement, Integer> colId;
    @FXML private TableColumn<Evenement, String> colTitre;
    @FXML private TableColumn<Evenement, String> colLangue;
    @FXML private TableColumn<Evenement, Integer> colDuree;
    @FXML private TableColumn<Evenement, Integer> colAgeMin;
    @FXML private TableColumn<Evenement, Double> colPrix;
    @FXML private TableColumn<Evenement, String> colCategories;
    @FXML private TextField searchField;

    // Filtres
    @FXML private ComboBox<Categorie> filtreCategorie;
    @FXML private TextField filtrePrixMin;
    @FXML private TextField filtrePrixMax;
    @FXML private TextField filtreAgeMin;
    @FXML private TextField filtreAgeMax;

    private final EvenementDAO evenementDAO = new EvenementDAO();
    private final CategorieDAO categorieDAO = new CategorieDAO();
    private ObservableList<Evenement> tousLesEvenements;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTitre.setCellValueFactory(new PropertyValueFactory<>("titre"));
        colLangue.setCellValueFactory(new PropertyValueFactory<>("langue"));
        colDuree.setCellValueFactory(new PropertyValueFactory<>("duree"));
        colAgeMin.setCellValueFactory(new PropertyValueFactory<>("ageMin"));
        colPrix.setCellValueFactory(new PropertyValueFactory<>("prixBase"));
        colCategories.setCellValueFactory(cellData -> {
            List<Categorie> cats = categorieDAO.getByEvenement(cellData.getValue().getId());
            String noms = cats.stream()
                    .map(Categorie::getNom)
                    .collect(Collectors.joining(", "));
            return new SimpleStringProperty(noms);
        });

        // Charger les catégories dans le filtre
        List<Categorie> categories = categorieDAO.getAll();
        filtreCategorie.setItems(FXCollections.observableArrayList(categories));

        chargerEvenements();
    }

    private void chargerEvenements() {
        List<Evenement> liste = evenementDAO.getAll();
        tousLesEvenements = FXCollections.observableArrayList(liste);
        tableEvenements.setItems(null);
        tableEvenements.setItems(tousLesEvenements);
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
        filtreCategorie.setValue(null);
        filtrePrixMin.clear();
        filtrePrixMax.clear();
        filtreAgeMin.clear();
        filtreAgeMax.clear();
        tableEvenements.setItems(tousLesEvenements);
    }

    private void appliquerFiltres() {
        String recherche = searchField.getText().toLowerCase().trim();
        Categorie categorie = filtreCategorie.getValue();

        double prixMin = parseDouble(filtrePrixMin.getText(), 0);
        double prixMax = parseDouble(filtrePrixMax.getText(), Double.MAX_VALUE);
        int ageMin = parseInt(filtreAgeMin.getText(), 0);
        int ageMax = parseInt(filtreAgeMax.getText(), Integer.MAX_VALUE);

        ObservableList<Evenement> filtres = tousLesEvenements.stream()
                .filter(e -> {
                    // Filtre recherche texte
                    boolean matchRecherche = recherche.isEmpty()
                            || e.getTitre().toLowerCase().contains(recherche)
                            || (e.getLangue() != null && e.getLangue().toLowerCase().contains(recherche));

                    // Filtre catégorie
                    boolean matchCategorie = true;
                    if (categorie != null) {
                        List<Integer> ids = categorieDAO.getIdsByEvenement(e.getId());
                        matchCategorie = ids.contains(categorie.getId());
                    }

                    // Filtre prix
                    boolean matchPrix = e.getPrixBase() >= prixMin && e.getPrixBase() <= prixMax;

                    // Filtre âge
                    boolean matchAge = e.getAgeMin() >= ageMin && e.getAgeMin() <= ageMax;

                    return matchRecherche && matchCategorie && matchPrix && matchAge;
                })
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        tableEvenements.setItems(filtres);
    }

    private double parseDouble(String text, double defaut) {
        try {
            return text.trim().isEmpty() ? defaut : Double.parseDouble(text.trim().replace(",", "."));
        } catch (NumberFormatException e) {
            return defaut;
        }
    }

    private int parseInt(String text, int defaut) {
        try {
            return text.trim().isEmpty() ? defaut : Integer.parseInt(text.trim());
        } catch (NumberFormatException e) {
            return defaut;
        }
    }

    @FXML
    private void handleAjouter() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/EvenementFormView.fxml"));
            ScrollPane formulaire = loader.load();

            EvenementFormController formController = loader.getController();
            formController.setEvenementController(this);

            StackPane parent = (StackPane) tableEvenements.getScene().lookup("#contentArea");
            parent.getChildren().clear();
            parent.getChildren().add(formulaire);

        } catch (IOException e) {
            System.err.println("Erreur ouverture formulaire : " + e.getMessage());
        }
    }

    @FXML
    private void handleModifier() {
        Evenement selectionne = tableEvenements.getSelectionModel().getSelectedItem();
        if (selectionne == null) {
            afficherAlerte("Sélection requise", "Veuillez sélectionner un événement à modifier.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/EvenementFormView.fxml"));
            ScrollPane formulaire = loader.load();

            EvenementFormController formController = loader.getController();
            formController.setEvenementController(this);
            formController.setEvenement(selectionne);

            StackPane parent = (StackPane) tableEvenements.getScene().lookup("#contentArea");
            parent.getChildren().clear();
            parent.getChildren().add(formulaire);

        } catch (IOException e) {
            System.err.println("Erreur ouverture formulaire modification : " + e.getMessage());
        }
    }

    @FXML
    private void handleSupprimer() {
        Evenement selectionne = tableEvenements.getSelectionModel().getSelectedItem();
        if (selectionne == null) {
            afficherAlerte("Sélection requise", "Veuillez sélectionner un événement à supprimer.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Supprimer l'événement \"" + selectionne.getTitre() + "\" ?");

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                boolean succes = evenementDAO.delete(selectionne.getId());
                if (succes) {
                    chargerEvenements();
                    afficherSucces("Événement supprimé avec succès.");
                } else {
                    afficherAlerte("Erreur", "Impossible de supprimer l'événement.");
                }
            }
        });
    }

    @FXML
    private void handleVoirAffiche() {
        Evenement selectionne = tableEvenements.getSelectionModel().getSelectedItem();
        if (selectionne == null) {
            afficherAlerte("Sélection requise", "Veuillez sélectionner un événement.");
            return;
        }

        String url = selectionne.getAffiche();
        if (url == null || url.trim().isEmpty()) {
            afficherAlerte("Aucune affiche", "Cet événement n'a pas d'affiche renseignée.");
            return;
        }

        try {
            javafx.scene.image.Image image = new javafx.scene.image.Image(url, true);

            javafx.scene.image.ImageView imageView = new javafx.scene.image.ImageView(image);
            imageView.setPreserveRatio(true);
            imageView.setFitWidth(400);
            imageView.setFitHeight(500);

            // Message si l'image ne charge pas
            image.errorProperty().addListener((obs, oldVal, error) -> {
                if (error) {
                    javafx.application.Platform.runLater(() ->
                            afficherAlerte("Erreur", "Impossible de charger l'image depuis : " + url)
                    );
                }
            });

            VBox vbox = new VBox(10);
            vbox.setPadding(new javafx.geometry.Insets(20));
            vbox.setAlignment(javafx.geometry.Pos.CENTER);
            vbox.getChildren().addAll(
                    new javafx.scene.control.Label(selectionne.getTitre()),
                    imageView
            );

            Stage popup = new Stage();
            popup.setTitle("Affiche — " + selectionne.getTitre());
            popup.initOwner(Main.getStage());
            popup.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            popup.setScene(new javafx.scene.Scene(vbox));
            popup.show();

        } catch (Exception e) {
            afficherAlerte("Erreur", "URL invalide : " + url);
        }
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
        chargerEvenements();
    }

    public void fermerFormulaire() {
        StackPane parent = (StackPane) tableEvenements.getScene().lookup("#contentArea");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/EvenementView.fxml"));
            parent.getChildren().clear();
            parent.getChildren().add(loader.load());
        } catch (IOException e) {
            System.err.println("Erreur fermeture formulaire : " + e.getMessage());
        }
    }
}