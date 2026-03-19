package org.example.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.example.dao.CategorieDAO;
import org.example.dao.EvenementDAO;
import org.example.model.Categorie;
import org.example.model.Evenement;
import org.example.model.Client;
import java.net.URL;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;



public class ClientEvenementsController implements Initializable {
    private Client clientConnecte;

    @FXML private FlowPane cardsContainer;
    @FXML private TextField searchField;
    @FXML private ComboBox<Categorie> filtreCategorie;

    private final EvenementDAO evenementDAO = new EvenementDAO();
    private final CategorieDAO categorieDAO = new CategorieDAO();
    private List<Evenement> tousLesEvenements;

    public void setClient(Client client) {
        this.clientConnecte = client;
        // Rafraîchir les cards avec le client maintenant disponible
        afficherCards(tousLesEvenements);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tousLesEvenements = evenementDAO.getAll();

        // Charger catégories filtre
        filtreCategorie.setItems(FXCollections.observableArrayList(categorieDAO.getAll()));

        afficherCards(tousLesEvenements);
    }

    private void afficherCards(List<Evenement> evenements) {
        cardsContainer.getChildren().clear();

        for (Evenement e : evenements) {
            cardsContainer.getChildren().add(creerCard(e));
        }
    }

    private VBox creerCard(Evenement evenement) {
        VBox card = new VBox(10);
        card.setPrefWidth(220);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 12px; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 2); " +
                "-fx-cursor: hand;");
        card.setPadding(new Insets(0, 0, 15, 0));

        // Image affiche
        ImageView imageView = new ImageView();
        imageView.setFitWidth(220);
        imageView.setFitHeight(150);
        imageView.setPreserveRatio(false);
        imageView.setStyle("-fx-background-radius: 12px 12px 0 0;");

        if (evenement.getAffiche() != null && !evenement.getAffiche().isEmpty()) {
            try {
                String affiche = evenement.getAffiche();
                Image img;
                if (affiche.startsWith("http://") || affiche.startsWith("https://")) {
                    // URL distante
                    img = new Image(affiche, 220, 150, false, true, true);
                } else {
                    // Chemin local dans les ressources
                    URL ressource = getClass().getResource(affiche);
                    if (ressource != null) {
                        img = new Image(ressource.toExternalForm(), 220, 150, false, true, true);
                    } else {
                        img = null;
                    }
                }
                if (img != null) imageView.setImage(img);
            } catch (Exception ex) {
                imageView.setStyle("-fx-background-color: #e8eaf6;");
            }
        }

        // Contenu texte
        VBox content = new VBox(6);
        content.setPadding(new Insets(10, 15, 0, 15));

        Label titre = new Label(evenement.getTitre());
        titre.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        titre.setTextFill(Color.web("#333333"));
        titre.setWrapText(true);

        Label description = new Label(evenement.getDescriptionCourte() != null ?
                evenement.getDescriptionCourte() : "");
        description.setStyle("-fx-text-fill: #888888; -fx-font-size: 12px;");
        description.setWrapText(true);
        description.setMaxHeight(40);

        Label prix = new Label("À partir de " + String.format("%.2f", evenement.getPrixBase()) + " €");
        prix.setStyle("-fx-text-fill: #5c6bc0; -fx-font-weight: bold; -fx-font-size: 13px;");

        Label langue = new Label("🌐 " + (evenement.getLangue() != null ? evenement.getLangue() : ""));
        langue.setStyle("-fx-text-fill: #aaaaaa; -fx-font-size: 11px;");

        Button btnDetail = new Button("Voir les séances →");
        btnDetail.setStyle("-fx-background-color: #5c6bc0; -fx-text-fill: white; " +
                "-fx-background-radius: 6px; -fx-font-size: 12px; -fx-cursor: hand;");
        btnDetail.setMaxWidth(Double.MAX_VALUE);
        VBox.setMargin(btnDetail, new Insets(5, 15, 0, 15));

        btnDetail.setOnAction(e -> ouvrirDetail(evenement));

        content.getChildren().addAll(titre, description, prix, langue);
        card.getChildren().addAll(imageView, content, btnDetail);

        return card;
    }

    private void ouvrirDetail(Evenement evenement) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ClientEvenementDetailView.fxml"));
            ScrollPane vue = loader.load();

            ClientEvenementDetailController controller = loader.getController();
            controller.setEvenement(evenement);
            controller.setClient(clientConnecte);

            StackPane parent = (StackPane) cardsContainer.getScene().lookup("#contentArea");
            parent.getChildren().clear();
            parent.getChildren().add(vue);
        } catch (IOException e) {
            System.err.println("Erreur ouverture détail : " + e.getMessage());
            e.printStackTrace();
        }
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
        afficherCards(tousLesEvenements);
    }

    private void appliquerFiltres() {
        String recherche = searchField.getText().toLowerCase().trim();
        Categorie categorie = filtreCategorie.getValue();

        List<Evenement> filtres = tousLesEvenements.stream()
                .filter(e -> {
                    boolean matchRecherche = recherche.isEmpty()
                            || e.getTitre().toLowerCase().contains(recherche);
                    boolean matchCategorie = categorie == null
                            || categorieDAO.getIdsByEvenement(e.getId()).contains(categorie.getId());
                    return matchRecherche && matchCategorie;
                })
                .collect(Collectors.toList());

        afficherCards(filtres);
    }
}