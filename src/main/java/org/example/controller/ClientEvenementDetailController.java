package org.example.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.control.ScrollPane;
import org.example.dao.CategorieDAO;
import org.example.dao.SeanceDAO;
import org.example.model.Categorie;
import org.example.model.Evenement;
import org.example.model.SeanceAffichage;
import org.example.model.Client;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class ClientEvenementDetailController {

    @FXML private ImageView afficheImage;
    @FXML private Label titreLabel;
    @FXML private Label descriptionCourteLabel;
    @FXML private Label descriptionLongueLabel;
    @FXML private Label langueLabel;
    @FXML private Label dureeLabel;
    @FXML private Label ageMinLabel;
    @FXML private Label prixLabel;
    @FXML private Label categoriesLabel;
    @FXML private VBox seancesContainer;

    private final SeanceDAO seanceDAO = new SeanceDAO();
    private final CategorieDAO categorieDAO = new CategorieDAO();
    private Evenement evenement;
    private Client clientConnecte;

    public void setEvenement(Evenement evenement) {
        this.evenement = evenement;

        titreLabel.setText(evenement.getTitre());
        descriptionCourteLabel.setText(evenement.getDescriptionCourte() != null ?
                evenement.getDescriptionCourte() : "");
        descriptionLongueLabel.setText(evenement.getDescriptionLongue() != null ?
                evenement.getDescriptionLongue() : "");
        langueLabel.setText("🌐 " + (evenement.getLangue() != null ? evenement.getLangue() : ""));
        dureeLabel.setText("⏱ " + evenement.getDuree() + " min");
        ageMinLabel.setText(evenement.getAgeMin() > 0 ? "🔞 " + evenement.getAgeMin() + "+" : "✅ Tout public");
        prixLabel.setText("À partir de " + String.format("%.2f", evenement.getPrixBase()) + " €");

        // Catégories
        List<Categorie> cats = categorieDAO.getByEvenement(evenement.getId());
        String nomsCats = cats.stream().map(Categorie::getNom).collect(Collectors.joining(" • "));
        categoriesLabel.setText("🏷 " + nomsCats);

        // Affiche
        if (evenement.getAffiche() != null && !evenement.getAffiche().isEmpty()) {
            try {
                afficheImage.setImage(new Image(evenement.getAffiche(), true));
            } catch (Exception e) {
                System.err.println("Erreur chargement affiche : " + e.getMessage());
            }
        }

        chargerSeances();
    }

    public void setClient(Client client) {
        this.clientConnecte = client;
    }

    private void chargerSeances() {
        seancesContainer.getChildren().clear();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy à HH:mm");

        List<SeanceAffichage> seances = seanceDAO.getAllAvecDetails().stream()
                .filter(s -> s.getNomEvenement().equals(evenement.getTitre()))
                .collect(Collectors.toList());

        if (seances.isEmpty()) {
            Label aucune = new Label("Aucune séance disponible pour cet événement.");
            aucune.setStyle("-fx-text-fill: #888888;");
            seancesContainer.getChildren().add(aucune);
            return;
        }

        for (SeanceAffichage seance : seances) {
            int placesRestantes = seanceDAO.getPlacesRestantes(seance.getId());

            HBox ligne = new HBox(15);
            ligne.setAlignment(Pos.CENTER_LEFT);
            ligne.setPadding(new Insets(12));
            ligne.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 8px; " +
                    "-fx-border-color: #e0e0e0; -fx-border-radius: 8px;");

            VBox infos = new VBox(4);
            HBox.setHgrow(infos, Priority.ALWAYS);

            Label dateLabel = new Label("📅 " + seance.getDateHeure().format(formatter));
            dateLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");

            Label salleLabel = new Label("📍 " + seance.getNomSalle());
            salleLabel.setStyle("-fx-text-fill: #666666; -fx-font-size: 12px;");

            String placesText = placesRestantes > 0 ?
                    placesRestantes + " place(s) restante(s)" : "❌ Complet";
            Label placesLabel = new Label(placesText);
            placesLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: " +
                    (placesRestantes > 0 ? "#4caf50" : "#f44336") + ";");

            infos.getChildren().addAll(dateLabel, salleLabel, placesLabel);

            Label prixSeance = new Label(String.format("%.2f €", seance.getPrix()));
            prixSeance.setStyle("-fx-font-weight: bold; -fx-text-fill: #5c6bc0; -fx-font-size: 14px;");

            Button btnAcheter = new Button("🎫 Acheter");

            if (placesRestantes <= 0) {
                btnAcheter.setDisable(true);
                btnAcheter.setStyle("-fx-background-color: #cccccc; -fx-text-fill: white; " +
                        "-fx-background-radius: 6px;");
            } else {
                btnAcheter.setStyle("-fx-background-color: #5c6bc0; -fx-text-fill: white; " +
                        "-fx-background-radius: 6px; -fx-cursor: hand;");
                btnAcheter.setOnAction(e -> acheterBillet(seance, placesRestantes));
            }

            ligne.getChildren().addAll(infos, prixSeance, btnAcheter);
            seancesContainer.getChildren().add(ligne);
        }
    }

    private String genererNumeroUnique() {
        int annee = java.time.LocalDateTime.now().getYear();
        org.example.dao.BilletDAO billetDAO = new org.example.dao.BilletDAO();
        int maxId = billetDAO.getMaxId();
        return String.format("TNG-%d-%05d", annee, maxId + 1);
    }

    private void acheterBillet(SeanceAffichage seance, int placesRestantes) {
        if (clientConnecte == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Connexion requise");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez vous connecter pour acheter un billet.");
            alert.initOwner(org.example.Main.getStage());
            alert.showAndWait();
            return;
        }

        Dialog<Integer> dialog = new Dialog<>();
        dialog.setTitle("Achat de billets");
        dialog.setHeaderText("\"" + evenement.getTitre() + "\"\n"
                + seance.getDateHeure().format(DateTimeFormatter.ofPattern("dd/MM/yyyy à HH:mm")));
        dialog.initOwner(org.example.Main.getStage());

        Spinner<Integer> spinner = new Spinner<>(1, placesRestantes, 1);
        spinner.setEditable(true);
        spinner.setPrefWidth(80);

        VBox content = new VBox(10);
        content.setPadding(new Insets(15));

        Label prixTotal = new Label("Total : " + String.format("%.2f €", seance.getPrix()));
        prixTotal.setStyle("-fx-font-weight: bold; -fx-text-fill: #5c6bc0;");

        spinner.valueProperty().addListener((obs, oldVal, newVal) ->
                prixTotal.setText("Total : " + String.format("%.2f €", seance.getPrix() * newVal))
        );

        content.getChildren().addAll(
                new Label("Quantité (max " + placesRestantes + ") :"),
                spinner,
                prixTotal
        );

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.setResultConverter(btn -> btn == ButtonType.OK ? spinner.getValue() : null);

        dialog.showAndWait().ifPresent(quantite -> {
            org.example.dao.BilletDAO billetDAO = new org.example.dao.BilletDAO();
            int succes = 0;

            for (int i = 0; i < quantite; i++) {
                org.example.model.Billet billet = new org.example.model.Billet();
                billet.setIdClient(clientConnecte.getId());
                billet.setIdSeance(seance.getId());
                billet.setPrix(seance.getPrix());
                billet.setStatut("valide");
                billet.setDateAchat(java.time.LocalDateTime.now());
                billet.setNumeroUnique(genererNumeroUnique());

                if (billetDAO.insert(billet)) succes++;
            }

            if (succes == quantite) {
                Alert sucAlert = new Alert(Alert.AlertType.INFORMATION);
                sucAlert.setTitle("Achat confirmé !");
                sucAlert.setHeaderText(null);
                sucAlert.setContentText(succes + " billet(s) réservé(s) avec succès ! 🎉\n"
                        + "Total payé : " + String.format("%.2f €", seance.getPrix() * succes));
                sucAlert.initOwner(org.example.Main.getStage());
                sucAlert.showAndWait();
                chargerSeances();
            } else {
                Alert errAlert = new Alert(Alert.AlertType.ERROR);
                errAlert.setTitle("Erreur");
                errAlert.setHeaderText(null);
                errAlert.setContentText("Erreur lors de l'achat. Veuillez réessayer.");
                errAlert.initOwner(org.example.Main.getStage());
                errAlert.showAndWait();
            }
        });
    }

    @FXML
    private void handleRetour() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ClientEvenementsView.fxml"));
            ScrollPane vue = loader.load();
            StackPane parent = (StackPane) titreLabel.getScene().lookup("#contentArea");
            parent.getChildren().clear();
            parent.getChildren().add(vue);
        } catch (IOException e) {
            System.err.println("Erreur retour : " + e.getMessage());
        }
    }
}