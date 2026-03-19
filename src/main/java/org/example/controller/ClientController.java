package org.example.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import org.example.dao.ClientDAO;
import org.example.model.Client;
import org.example.Main;

import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.scene.control.TableColumn;
import org.example.dao.BilletDAO;
import org.example.model.BilletAffichage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ClientController implements Initializable {

    @FXML private TableView<Client> tableClients;
    @FXML private TableColumn<Client, Integer> colId;
    @FXML private TableColumn<Client, String> colNom;
    @FXML private TableColumn<Client, String> colEmail;
    @FXML private TableColumn<Client, String> colTelephone;
    @FXML private TableColumn<Client, String> colAdresse;
    @FXML private TableColumn<Client, LocalDate> colDateNaissance;
    @FXML private TextField searchField;

    // Filtres
    @FXML private TextField filtreVille;
    @FXML private TextField filtreAgeMin;
    @FXML private TextField filtreAgeMax;

    private final ClientDAO clientDAO = new ClientDAO();
    private final BilletDAO billetDAO = new BilletDAO();
    private ObservableList<Client> tousLesClients;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colTelephone.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        colAdresse.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        colDateNaissance.setCellValueFactory(new PropertyValueFactory<>("dateNaissance"));

        colDateNaissance.setCellFactory(col -> new TableCell<>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : formatter.format(item));
            }
        });

        chargerClients();
    }

    private void chargerClients() {
        List<Client> liste = clientDAO.getAll();
        tousLesClients = FXCollections.observableArrayList(liste);
        tableClients.setItems(null);
        tableClients.setItems(tousLesClients);
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
        filtreVille.clear();
        filtreAgeMin.clear();
        filtreAgeMax.clear();
        tableClients.setItems(tousLesClients);
    }

    private void appliquerFiltres() {
        String recherche = searchField.getText().toLowerCase().trim();
        String ville = filtreVille.getText().toLowerCase().trim();
        int ageMin = parseInt(filtreAgeMin.getText(), 0);
        int ageMax = parseInt(filtreAgeMax.getText(), Integer.MAX_VALUE);

        ObservableList<Client> filtres = tousLesClients.stream()
                .filter(c -> {
                    // Filtre recherche texte
                    boolean matchRecherche = recherche.isEmpty()
                            || c.getNom().toLowerCase().contains(recherche)
                            || c.getEmail().toLowerCase().contains(recherche)
                            || (c.getTelephone() != null && c.getTelephone().contains(recherche));

                    // Filtre ville/adresse
                    boolean matchVille = ville.isEmpty()
                            || (c.getAdresse() != null && c.getAdresse().toLowerCase().contains(ville));

                    // Filtre âge
                    boolean matchAge = true;
                    if (c.getDateNaissance() != null) {
                        int age = Period.between(c.getDateNaissance(), LocalDate.now()).getYears();
                        matchAge = age >= ageMin && age <= ageMax;
                    }

                    return matchRecherche && matchVille && matchAge;
                })
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        tableClients.setItems(filtres);
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ClientFormView.fxml"));
            ScrollPane formulaire = loader.load();

            ClientFormController formController = loader.getController();
            formController.setClientController(this);

            StackPane parent = (StackPane) tableClients.getScene().lookup("#contentArea");
            parent.getChildren().clear();
            parent.getChildren().add(formulaire);

        } catch (IOException e) {
            System.err.println("Erreur ouverture formulaire : " + e.getMessage());
        }
    }

    @FXML
    private void handleModifier() {
        Client selectionne = tableClients.getSelectionModel().getSelectedItem();
        if (selectionne == null) {
            afficherAlerte("Sélection requise", "Veuillez sélectionner un client à modifier.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ClientFormView.fxml"));
            ScrollPane formulaire = loader.load();

            ClientFormController formController = loader.getController();
            formController.setClientController(this);
            formController.setClient(selectionne);

            StackPane parent = (StackPane) tableClients.getScene().lookup("#contentArea");
            parent.getChildren().clear();
            parent.getChildren().add(formulaire);

        } catch (IOException e) {
            System.err.println("Erreur ouverture formulaire modification : " + e.getMessage());
        }
    }

    @FXML
    private void handleVoirBillets() {
        Client selectionne = tableClients.getSelectionModel().getSelectedItem();
        if (selectionne == null) {
            afficherAlerte("Sélection requise", "Veuillez sélectionner un client.");
            return;
        }

        List<BilletAffichage> billets = billetDAO.getByClientAvecDetails(selectionne.getId());

        if (billets.isEmpty()) {
            afficherAlerte("Aucun billet", selectionne.getNom() + " n'a aucun billet.");
            return;
        }

        // Créer une fenêtre popup
        Stage popup = new Stage();
        popup.setTitle("Billets de " + selectionne.getNom());
        popup.initModality(javafx.stage.Modality.APPLICATION_MODAL);

        TableView<BilletAffichage> table = new TableView<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        TableColumn<BilletAffichage, String> colNumero = new TableColumn<>("Numéro");
        colNumero.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("numeroUnique"));
        colNumero.setPrefWidth(150);

        TableColumn<BilletAffichage, String> colEvenement = new TableColumn<>("Événement");
        colEvenement.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("nomEvenement"));
        colEvenement.setPrefWidth(200);

        TableColumn<BilletAffichage, LocalDateTime> colDate = new TableColumn<>("Séance");
        colDate.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("dateHeure"));
        colDate.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : formatter.format(item));
            }
        });
        colDate.setPrefWidth(150);

        TableColumn<BilletAffichage, Double> colPrix = new TableColumn<>("Prix (€)");
        colPrix.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("prix"));
        colPrix.setPrefWidth(80);

        TableColumn<BilletAffichage, String> colStatut = new TableColumn<>("Statut");
        colStatut.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("statut"));
        colStatut.setPrefWidth(100);

        table.getColumns().addAll(colNumero, colEvenement, colDate, colPrix, colStatut);
        table.setItems(FXCollections.observableArrayList(billets));

        VBox vbox = new VBox(10);
        vbox.setPadding(new javafx.geometry.Insets(20));
        vbox.getChildren().add(table);

        popup.setScene(new javafx.scene.Scene(vbox, 720, 400));
        popup.initOwner(Main.getStage());
        popup.show();
    }

    @FXML
    private void handleSupprimer() {
        Client selectionne = tableClients.getSelectionModel().getSelectedItem();
        if (selectionne == null) {
            afficherAlerte("Sélection requise", "Veuillez sélectionner un client à supprimer.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Supprimer le client \"" + selectionne.getNom() + "\" ? Ses billets seront également supprimés.");

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                boolean succes = clientDAO.delete(selectionne.getId());
                if (succes) {
                    chargerClients();
                    afficherSucces("Client supprimé avec succès.");
                } else {
                    afficherAlerte("Erreur", "Impossible de supprimer le client.");
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
        chargerClients();
    }

    public void fermerFormulaire() {
        StackPane parent = (StackPane) tableClients.getScene().lookup("#contentArea");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ClientView.fxml"));
            parent.getChildren().clear();
            parent.getChildren().add(loader.load());
        } catch (IOException e) {
            System.err.println("Erreur fermeture formulaire : " + e.getMessage());
        }
    }
}