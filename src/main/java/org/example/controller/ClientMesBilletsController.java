package org.example.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.example.dao.BilletDAO;
import org.example.model.BilletAffichage;
import org.example.model.Client;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ClientMesBilletsController implements Initializable {

    @FXML private VBox billetsContainer;
    @FXML private Label statsLabel;
    @FXML private ComboBox<String> filtreStatut;

    private final BilletDAO billetDAO = new BilletDAO();
    private Client client;
    private List<BilletAffichage> tousLesBillets;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        filtreStatut.setItems(FXCollections.observableArrayList(
                "valide", "annule", "rembourse"
        ));
    }

    public void setClient(Client client) {
        this.client = client;
        chargerBillets();
    }

    private void chargerBillets() {
        tousLesBillets = billetDAO.getByClientAvecDetails(client.getId());
        afficherBillets(tousLesBillets);
    }

    private void afficherBillets(List<BilletAffichage> billets) {
        billetsContainer.getChildren().clear();

        long nbValides = billets.stream().filter(b -> "valide".equals(b.getStatut())).count();
        statsLabel.setText(billets.size() + " billet(s) — " + nbValides + " valide(s)");

        if (billets.isEmpty()) {
            Label aucun = new Label("Aucun billet trouvé.");
            aucun.setStyle("-fx-text-fill: #888888; -fx-font-size: 14px;");
            billetsContainer.getChildren().add(aucun);
            return;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (BilletAffichage billet : billets) {
            HBox carte = new HBox(15);
            carte.setAlignment(Pos.CENTER_LEFT);
            carte.setPadding(new Insets(15));

            String couleurBord = switch (billet.getStatut()) {
                case "valide" -> "#4caf50";
                case "annule" -> "#f44336";
                case "rembourse" -> "#ff9800";
                default -> "#cccccc";
            };
            carte.setStyle("-fx-background-color: white; -fx-background-radius: 10px; " +
                    "-fx-border-color: " + couleurBord + "; -fx-border-width: 0 0 0 4px; " +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 6, 0, 0, 2);");

            // Infos billet
            VBox infos = new VBox(5);
            HBox.setHgrow(infos, Priority.ALWAYS);

            Label numeroLabel = new Label("🎫 " + billet.getNumeroUnique());
            numeroLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 13px; -fx-text-fill: #333333;");

            Label evenementLabel = new Label("🎭 " + billet.getNomEvenement());
            evenementLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #555555;");

            Label seanceLabel = new Label("📅 " + (billet.getDateHeure() != null ?
                    billet.getDateHeure().format(formatter) : "N/A"));
            seanceLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #888888;");

            Label achatLabel = new Label("🛒 Acheté le " + (billet.getDateAchat() != null ?
                    billet.getDateAchat().format(formatter) : "N/A"));
            achatLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #aaaaaa;");

            infos.getChildren().addAll(numeroLabel, evenementLabel, seanceLabel, achatLabel);

            // Prix + statut
            VBox droite = new VBox(8);
            droite.setAlignment(Pos.CENTER_RIGHT);

            Label prixLabel = new Label(String.format("%.2f €", billet.getPrix()));
            prixLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 15px; -fx-text-fill: #5c6bc0;");

            String statutTexte = switch (billet.getStatut()) {
                case "valide" -> "✅ Valide";
                case "annule" -> "❌ Annulé";
                case "rembourse" -> "💰 Remboursé";
                default -> billet.getStatut();
            };
            Label statutLabel = new Label(statutTexte);
            statutLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: " + couleurBord + ";");

            droite.getChildren().addAll(prixLabel, statutLabel);

            // Boutons
            VBox boutons = new VBox(8);
            boutons.setAlignment(Pos.CENTER);

            Button btnDetail = new Button("👁 Détail");
            btnDetail.setStyle("-fx-background-color: #e8eaf6; -fx-text-fill: #5c6bc0; " +
                    "-fx-background-radius: 6px; -fx-font-size: 11px; -fx-cursor: hand;");
            btnDetail.setOnAction(e -> afficherDetail(billet));

            boutons.getChildren().add(btnDetail);

            if ("valide".equals(billet.getStatut())) {
                Button btnAnnuler = new Button("🚫 Annuler");
                btnAnnuler.setStyle("-fx-background-color: #ffebee; -fx-text-fill: #f44336; " +
                        "-fx-background-radius: 6px; -fx-font-size: 11px; -fx-cursor: hand;");
                btnAnnuler.setOnAction(e -> annulerBillet(billet));
                boutons.getChildren().add(btnAnnuler);
            }

            carte.getChildren().addAll(infos, droite, boutons);
            billetsContainer.getChildren().add(carte);
        }
    }

    private void afficherDetail(BilletAffichage billet) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        Alert detail = new Alert(Alert.AlertType.INFORMATION);
        detail.setTitle("Détail du billet");
        detail.setHeaderText("🎫 " + billet.getNumeroUnique());
        detail.setContentText(
                "Événement : " + billet.getNomEvenement() + "\n" +
                        "Séance : " + (billet.getDateHeure() != null ? billet.getDateHeure().format(formatter) : "N/A") + "\n" +
                        "Prix : " + String.format("%.2f €", billet.getPrix()) + "\n" +
                        "Statut : " + billet.getStatut() + "\n" +
                        "Date d'achat : " + (billet.getDateAchat() != null ? billet.getDateAchat().format(formatter) : "N/A")
        );
        detail.initOwner(org.example.Main.getStage());
        detail.showAndWait();
    }

    private void annulerBillet(BilletAffichage billet) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Annuler le billet");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Êtes-vous sûr de vouloir annuler le billet " +
                billet.getNumeroUnique() + " ?\nCette action est irréversible.");
        confirmation.initOwner(org.example.Main.getStage());

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                boolean succes = billetDAO.updateStatut(billet.getId(), "annule");
                if (succes) {
                    Alert ok = new Alert(Alert.AlertType.INFORMATION);
                    ok.setTitle("Billet annulé");
                    ok.setHeaderText(null);
                    ok.setContentText("Votre billet a été annulé avec succès.");
                    ok.initOwner(org.example.Main.getStage());
                    ok.showAndWait();
                    chargerBillets(); // Rafraîchir
                } else {
                    Alert err = new Alert(Alert.AlertType.ERROR);
                    err.setTitle("Erreur");
                    err.setHeaderText(null);
                    err.setContentText("Erreur lors de l'annulation.");
                    err.initOwner(org.example.Main.getStage());
                    err.showAndWait();
                }
            }
        });
    }

    @FXML
    private void handleFiltrer() {
        String statut = filtreStatut.getValue();
        if (statut == null) {
            afficherBillets(tousLesBillets);
            return;
        }
        List<BilletAffichage> filtres = tousLesBillets.stream()
                .filter(b -> statut.equals(b.getStatut()))
                .collect(Collectors.toList());
        afficherBillets(filtres);
    }

    @FXML
    private void handleReinitialiser() {
        filtreStatut.setValue(null);
        afficherBillets(tousLesBillets);
    }
}