package org.example.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import org.example.dao.BilletDAO;
import org.example.dao.EvenementDAO;
import org.example.model.BilletAffichage;
import org.example.model.Evenement;
import org.example.Main;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class BilletController implements Initializable {

    @FXML private TableView<BilletAffichage> tableBillets;
    @FXML private TableColumn<BilletAffichage, Integer> colId;
    @FXML private TableColumn<BilletAffichage, String> colNumero;
    @FXML private TableColumn<BilletAffichage, String> colClient;
    @FXML private TableColumn<BilletAffichage, String> colEvenement;
    @FXML private TableColumn<BilletAffichage, LocalDateTime> colDateHeure;
    @FXML private TableColumn<BilletAffichage, Double> colPrix;
    @FXML private TableColumn<BilletAffichage, String> colStatut;
    @FXML private TableColumn<BilletAffichage, LocalDateTime> colDateAchat;
    @FXML private TextField searchField;

    // Filtres
    @FXML private ComboBox<String> filtreStatut;
    @FXML private ComboBox<Evenement> filtreEvenement;
    @FXML private TextField filtrePrixMin;
    @FXML private TextField filtrePrixMax;
    @FXML private DatePicker filtreDateDebut;
    @FXML private DatePicker filtreDateFin;

    private final BilletDAO billetDAO = new BilletDAO();
    private final EvenementDAO evenementDAO = new EvenementDAO();
    private ObservableList<BilletAffichage> tousLesBillets;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNumero.setCellValueFactory(new PropertyValueFactory<>("numeroUnique"));
        colClient.setCellValueFactory(new PropertyValueFactory<>("nomClient"));
        colEvenement.setCellValueFactory(new PropertyValueFactory<>("nomEvenement"));
        colDateHeure.setCellValueFactory(new PropertyValueFactory<>("dateHeure"));
        colPrix.setCellValueFactory(new PropertyValueFactory<>("prix"));
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));
        colDateAchat.setCellValueFactory(new PropertyValueFactory<>("dateAchat"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        colDateHeure.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : formatter.format(item));
            }
        });
        colDateAchat.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : formatter.format(item));
            }
        });

        // Remplir les filtres
        filtreStatut.setItems(FXCollections.observableArrayList(
                "Tous", "valide", "annule", "rembourse"
        ));
        filtreStatut.setValue("Tous");

        List<Evenement> evenements = evenementDAO.getAll();
        filtreEvenement.setItems(FXCollections.observableArrayList(evenements));

        chargerBillets();
    }

    private void chargerBillets() {
        List<BilletAffichage> liste = billetDAO.getAllAvecDetails();
        tousLesBillets = FXCollections.observableArrayList(liste);
        tableBillets.setItems(null);
        tableBillets.setItems(tousLesBillets);
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
        filtreStatut.setValue("Tous");
        filtreEvenement.setValue(null);
        filtrePrixMin.clear();
        filtrePrixMax.clear();
        filtreDateDebut.setValue(null);
        filtreDateFin.setValue(null);
        tableBillets.setItems(tousLesBillets);
    }

    private void appliquerFiltres() {
        String recherche = searchField.getText().toLowerCase().trim();
        String statut = filtreStatut.getValue();
        Evenement evenement = filtreEvenement.getValue();
        double prixMin = parseDouble(filtrePrixMin.getText(), 0);
        double prixMax = parseDouble(filtrePrixMax.getText(), Double.MAX_VALUE);
        LocalDate dateDebut = filtreDateDebut.getValue();
        LocalDate dateFin = filtreDateFin.getValue();

        ObservableList<BilletAffichage> filtres = tousLesBillets.stream()
                .filter(b -> {
                    boolean matchRecherche = recherche.isEmpty()
                            || b.getNumeroUnique().toLowerCase().contains(recherche)
                            || b.getNomClient().toLowerCase().contains(recherche)
                            || b.getNomEvenement().toLowerCase().contains(recherche);

                    boolean matchStatut = statut == null || statut.equals("Tous")
                            || b.getStatut().equals(statut);

                    boolean matchEvenement = evenement == null
                            || b.getNomEvenement().equals(evenement.getTitre());

                    boolean matchPrix = b.getPrix() >= prixMin && b.getPrix() <= prixMax;

                    boolean matchDate = true;
                    if (dateDebut != null) {
                        matchDate = !b.getDateAchat().toLocalDate().isBefore(dateDebut);
                    }
                    if (dateFin != null) {
                        matchDate = matchDate && !b.getDateAchat().toLocalDate().isAfter(dateFin);
                    }

                    return matchRecherche && matchStatut && matchEvenement && matchPrix && matchDate;
                })
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        tableBillets.setItems(filtres);
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/BilletFormView.fxml"));
            ScrollPane formulaire = loader.load();

            BilletFormController formController = loader.getController();
            formController.setBilletController(this);

            StackPane parent = (StackPane) tableBillets.getScene().lookup("#contentArea");
            parent.getChildren().clear();
            parent.getChildren().add(formulaire);

        } catch (IOException e) {
            System.err.println("Erreur ouverture formulaire : " + e.getMessage());
        }
    }

    @FXML
    private void handleModifierStatut() {
        BilletAffichage selectionne = tableBillets.getSelectionModel().getSelectedItem();
        if (selectionne == null) {
            afficherAlerte("Sélection requise", "Veuillez sélectionner un billet.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/BilletModifFormView.fxml"));
            ScrollPane formulaire = loader.load();

            BilletModifFormController formController = loader.getController();
            formController.setBilletController(this);
            formController.setBillet(selectionne);

            StackPane parent = (StackPane) tableBillets.getScene().lookup("#contentArea");
            parent.getChildren().clear();
            parent.getChildren().add(formulaire);

        } catch (IOException e) {
            System.err.println("Erreur ouverture formulaire modification : " + e.getMessage());
        }
    }

    @FXML
    private void handleSupprimer() {
        BilletAffichage selectionne = tableBillets.getSelectionModel().getSelectedItem();
        if (selectionne == null) {
            afficherAlerte("Sélection requise", "Veuillez sélectionner un billet à supprimer.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Supprimer le billet \"" + selectionne.getNumeroUnique() + "\" ?");

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                boolean succes = billetDAO.delete(selectionne.getId());
                if (succes) {
                    chargerBillets();
                    afficherSucces("Billet supprimé avec succès.");
                } else {
                    afficherAlerte("Erreur", "Impossible de supprimer le billet.");
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
        chargerBillets();
    }
}