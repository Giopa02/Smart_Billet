package org.example.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.example.dao.CategorieDAO;
import org.example.dao.EvenementDAO;
import org.example.model.Categorie;
import org.example.model.Evenement;
import org.example.Main;
import javafx.scene.control.ScrollPane;

import javafx.stage.FileChooser;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class EvenementFormController implements Initializable {

    @FXML private Label titreFormLabel;
    @FXML private TextField titreField;
    @FXML private TextField descriptionCourteField;
    @FXML private TextArea descriptionLongueField;
    @FXML private TextField dureeField;
    @FXML private TextField ageMinField;
    @FXML private TextField langueField;
    @FXML private TextField prixField;
    @FXML private TextField afficheField;
    @FXML private VBox categoriesContainer;
    @FXML private Label errorLabel;
    @FXML private Label affichePreviewLabel;

    private final EvenementDAO evenementDAO = new EvenementDAO();
    private final CategorieDAO categorieDAO = new CategorieDAO();
    private final List<CheckBox> checkBoxes = new ArrayList<>();

    private Evenement evenementAModifier = null;
    private EvenementController evenementController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Charger toutes les catégories comme checkboxes
        List<Categorie> categories = categorieDAO.getAll();
        for (Categorie cat : categories) {
            CheckBox cb = new CheckBox(cat.getNom());
            cb.setUserData(cat.getId());
            cb.setStyle("-fx-font-size: 13px;");
            checkBoxes.add(cb);
            categoriesContainer.getChildren().add(cb);
        }
    }

    public void setEvenementController(EvenementController controller) {
        this.evenementController = controller;
    }

    public void setEvenement(Evenement evenement) {
        this.evenementAModifier = evenement;
        titreFormLabel.setText("Modifier un événement");

        titreField.setText(evenement.getTitre());
        descriptionCourteField.setText(evenement.getDescriptionCourte() != null ? evenement.getDescriptionCourte() : "");
        descriptionLongueField.setText(evenement.getDescriptionLongue() != null ? evenement.getDescriptionLongue() : "");
        dureeField.setText(String.valueOf(evenement.getDuree()));
        ageMinField.setText(String.valueOf(evenement.getAgeMin()));
        langueField.setText(evenement.getLangue() != null ? evenement.getLangue() : "");
        prixField.setText(String.valueOf(evenement.getPrixBase()));
        afficheField.setText(evenement.getAffiche() != null ? evenement.getAffiche() : "");

        // Cocher les catégories déjà associées
        List<Integer> idsCategories = categorieDAO.getIdsByEvenement(evenement.getId());
        for (CheckBox cb : checkBoxes) {
            int idCat = (int) cb.getUserData();
            cb.setSelected(idsCategories.contains(idCat));
        }
    }

    @FXML
    private void handleSauvegarder() {
        if (titreField.getText().trim().isEmpty()) {
            afficherErreur("Le titre est obligatoire.");
            return;
        }

        int duree = 0, ageMin = 0;
        double prix = 0;

        try {
            duree = dureeField.getText().trim().isEmpty() ? 0 : Integer.parseInt(dureeField.getText().trim());
        } catch (NumberFormatException e) {
            afficherErreur("La durée doit être un nombre entier.");
            return;
        }

        try {
            ageMin = ageMinField.getText().trim().isEmpty() ? 0 : Integer.parseInt(ageMinField.getText().trim());
        } catch (NumberFormatException e) {
            afficherErreur("L'âge minimum doit être un nombre entier.");
            return;
        }

        try {
            prix = prixField.getText().trim().isEmpty() ? 0 : Double.parseDouble(prixField.getText().trim().replace(",", "."));
        } catch (NumberFormatException e) {
            afficherErreur("Le prix doit être un nombre valide.");
            return;
        }

        // Récupérer les catégories cochées
        List<Integer> categoriesSelectionnees = new ArrayList<>();
        for (CheckBox cb : checkBoxes) {
            if (cb.isSelected()) {
                categoriesSelectionnees.add((int) cb.getUserData());
            }
        }

        if (evenementAModifier == null) {
            // MODE AJOUT
            Evenement nouvel = new Evenement(
                    0,
                    titreField.getText().trim(),
                    afficheField.getText().trim(),
                    descriptionCourteField.getText().trim(),
                    descriptionLongueField.getText().trim(),
                    duree, ageMin,
                    langueField.getText().trim(),
                    prix
            );

            boolean succes = evenementDAO.insert(nouvel);
            if (succes) {
                // Récupérer l'id du nouvel événement
                List<Evenement> tous = evenementDAO.getAll();
                int nouvelId = tous.get(tous.size() - 1).getId();

                // Sauvegarder les catégories
                for (int idCat : categoriesSelectionnees) {
                    categorieDAO.insertEvenementCategorie(nouvelId, idCat);
                }

                evenementController.rafraichir();
                evenementController.afficherSucces("Événement ajouté avec succès !");
                handleAnnuler();
            } else {
                afficherErreur("Erreur lors de l'ajout.");
            }

        } else {
            // MODE MODIFICATION
            evenementAModifier.setTitre(titreField.getText().trim());
            evenementAModifier.setAffiche(afficheField.getText().trim());
            evenementAModifier.setDescriptionCourte(descriptionCourteField.getText().trim());
            evenementAModifier.setDescriptionLongue(descriptionLongueField.getText().trim());
            evenementAModifier.setDuree(duree);
            evenementAModifier.setAgeMin(ageMin);
            evenementAModifier.setLangue(langueField.getText().trim());
            evenementAModifier.setPrixBase(prix);

            boolean succes = evenementDAO.update(evenementAModifier);
            if (succes) {
                // Mettre à jour les catégories
                categorieDAO.deleteByEvenement(evenementAModifier.getId());
                for (int idCat : categoriesSelectionnees) {
                    categorieDAO.insertEvenementCategorie(evenementAModifier.getId(), idCat);
                }

                evenementController.rafraichir();
                evenementController.afficherSucces("Événement modifié avec succès !");
                handleAnnuler();
            } else {
                afficherErreur("Erreur lors de la modification.");
            }
        }
    }

    @FXML
    private void handleAnnuler() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/EvenementView.fxml"));
            ScrollPane vue = loader.load();

            StackPane parent = (StackPane) titreFormLabel.getScene().lookup("#contentArea");
            parent.getChildren().clear();
            parent.getChildren().add(vue);
        } catch (IOException e) {
            System.err.println("Erreur annulation : " + e.getMessage());
        }
    }

    @FXML
    private void handleParcourirImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une affiche");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.webp")
        );

        File fichier = fileChooser.showOpenDialog(Main.getStage());
        if (fichier == null) return;

        try {
            String nomFichier = System.currentTimeMillis() + "_" + fichier.getName();

            // Copier dans src/main/resources/images/ (pour la persistance)
            String dossierSrc = "src/main/resources/images/";
            Files.createDirectories(Paths.get(dossierSrc));
            Files.copy(fichier.toPath(), Paths.get(dossierSrc + nomFichier), StandardCopyOption.REPLACE_EXISTING);

            // Copier aussi dans target/classes/images/ (pour l'exécution immédiate)
            String dossierTarget = "target/classes/images/";
            Files.createDirectories(Paths.get(dossierTarget));
            Files.copy(fichier.toPath(), Paths.get(dossierTarget + nomFichier), StandardCopyOption.REPLACE_EXISTING);

            afficheField.setText("/images/" + nomFichier);
            affichePreviewLabel.setText("✅ Image importée : " + nomFichier);

        } catch (IOException e) {
            affichePreviewLabel.setStyle("-fx-text-fill: #f44336; -fx-font-size: 11px;");
            affichePreviewLabel.setText("❌ Erreur lors de l'import : " + e.getMessage());
        }
    }

    private void afficherErreur(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
}