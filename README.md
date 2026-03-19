# 🎟️ SmartBillet

> Application de bureau JavaFX simulant le système d'information d'une billetterie culturelle.  
> Projet BTS SIO SLAM — Mars 2026

---

## 📋 Sommaire

- [Présentation](#présentation)
- [Fonctionnalités clés](#fonctionnalités-clés)
- [Technologies utilisées](#technologies-utilisées)
- [Prérequis et installation](#prérequis-et-installation)
- [Architecture du projet](#architecture-du-projet)
- [Structure du projet](#structure-du-projet)
- [Tests unitaires](#tests-unitaires)
- [Difficultés rencontrées](#difficultés-rencontrées)
- [Améliorations à apporter](#améliorations-à-apporter)

---

## Présentation

SmartBillet est une application de bureau (client lourd) développée en **Java 21 avec JavaFX 21**, inspirée du modèle BilletRéduc. Elle permet à un administrateur de gérer l'ensemble des événements, séances, clients et billets, et à un client de consulter les événements et d'acheter des billets.

L'application se décompose en deux espaces distincts :

- **Espace Administrateur** : back-office complet avec CRUD sur toutes les entités
- **Espace Client** : front-office avec inscription, connexion, consultation des événements, achat de billets, suivi des commandes et gestion du compte

---

## Fonctionnalités clés

### 🔐 Espace Administrateur

| Module | Fonctionnalités |
|---|---|
| **Événements** | CRUD complet — catégories multiples (checkboxes) — affiche (URL image) — description courte/longue — durée, âge minimum, langue, prix de base — filtres : catégorie, prix min/max, âge min/max |
| **Clients** | CRUD complet — validation email (regex) et téléphone (10 chiffres) — filtres : ville/adresse, âge min/max — popup billets par client |
| **Séances** | CRUD complet — affichage noms lisibles (JOIN Événement + Salle) — places restantes en temps réel (format X/Y) — filtres : salle, prix min/max, date début/fin |
| **Billets** | CRUD complet — autocomplétion client — filtres : événement, statut (valide/annulé/remboursé), prix, date — numéro unique format `TNG-YYYY-XXXXX` |
| **Salles** | CRUD complet — association à un complexe culturel — gestion de la capacité |

### 👤 Espace Client

| Écran | Fonctionnalités |
|---|---|
| **Inscription** | Formulaire complet avec validations — connexion automatique après inscription — hashage bcrypt immédiat |
| **Connexion** | Authentification email/mot de passe — vérification bcrypt — messages d'erreur distincts |
| **Grille événements** | Cards visuelles avec affiche, titre, description, prix, langue — recherche textuelle — filtre par catégorie |
| **Détail événement** | Affiche pleine largeur — liste des séances avec places restantes en vert/rouge — bouton désactivé si complet |
| **Achat de billet** | Spinner de quantité — calcul du prix total dynamique — génération numéro unique — rafraîchissement des places après achat |
| **Mes billets** | Cartes colorées par statut (vert/rouge/orange) — détail en popup — annulation avec confirmation — filtre par statut |
| **Mon compte** | Modification des informations personnelles — changement de mot de passe sécurisé avec vérification de l'ancien mot de passe |

---

## Technologies utilisées

| Technologie / Outil | Usage |
|---|---|
| **Java 21** | Langage principal |
| **JavaFX 21.0.9** | Framework UI — composants, FXML, CSS |
| **Scene Builder** | Conception des fichiers FXML |
| **Maven** | Gestionnaire de dépendances et build |
| **MySQL** (via MAMP, port 8889) | Base de données relationnelle |
| **JDBC** (mysql-connector-java 8.0.33) | Connexion Java → MySQL |
| **bcrypt** (at.favre.lib 0.10.2) | Hashage sécurisé des mots de passe (coût 12) |
| **JUnit 5** | Tests unitaires (57 tests) |
| **Mockito** | Mocking des DAO dans les tests |
| **IntelliJ IDEA** | IDE de développement |
| **Git / GitHub** | Versionnement du projet |
| **phpMyAdmin** | Administration de la base de données |

---

## Prérequis et installation

### Prérequis

- **Java JDK 21** — [Télécharger](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html)
- **JavaFX SDK 21** — [Télécharger](https://gluonhq.com/products/javafx/)
- **Maven** — [Télécharger](https://maven.apache.org/download.cgi)
- **MAMP** (ou tout autre serveur MySQL local) avec MySQL sur le **port 8889**
- **IntelliJ IDEA** (Community ou Ultimate) recommandé

### Installation

1. **Cloner le dépôt**
   ```bash
   git clone https://github.com/<votre-username>/SmartBillet.git
   cd SmartBillet
   ```

2. **Importer la base de données**
   - Démarrer MAMP et ouvrir phpMyAdmin (`http://localhost:8888/phpMyAdmin`)
   - Créer une base de données nommée `smart_billet_v2`
   - Importer le fichier SQL fourni (`smart_billet_v2.sql`)

3. **Configurer la connexion BDD**  
   Vérifier les paramètres dans `src/main/java/org/example/database/DatabaseConnection.java` :
   ```java
   private static final String URL = "jdbc:mysql://localhost:8889/smart_billet_v2";
   private static final String USER = "root";
   private static final String PASSWORD = "root";
   ```

4. **Configurer les VM Options JavaFX**  
   Dans IntelliJ, aller dans **Run > Edit Configurations** et ajouter :
   ```
   --module-path /chemin/vers/javafx-sdk-21/lib --add-modules javafx.controls,javafx.fxml
   ```

5. **Lancer l'application**
   ```bash
   mvn clean javafx:run
   ```

### Compte administrateur par défaut

| Champ | Valeur |
|---|---|
| Email | `admin@smartbillet.fr` |
| Mot de passe | `Admin1234!` |

---

## Architecture du projet

L'application suit une architecture **MVC / DAO** stricte :

```
Vues (FXML)
    │
    ▼
Controllers  ──►  DAO (accès BDD)  ──►  MySQL
    │                   │
    ▼                   ▼
  Models           DatabaseConnection (Singleton)
```

- **Model** : classes POJO représentant les entités (`Evenement`, `Client`, `Billet`, etc.)
- **DAO** : couche d'accès aux données — toutes les requêtes SQL sont isolées ici, jamais dans les controllers
- **Controller** : logique métier et liaison avec les vues FXML
- **Main.java** : navigation centralisée via un `StackPane` racine unique — évite la perte du plein écran à chaque changement de vue

### Base de données — 9 tables

| Table | Description |
|---|---|
| `ComplexeCulturel` | Lieu principal (nom, adresse) |
| `Salle` | Appartient à un ComplexeCulturel — capacité |
| `Evenement` | Titre, descriptions, durée, âge min, langue, prix_base, affiche |
| `Seance` | Regroupe Événement + Salle + dateHeure + prix |
| `Categorie` | Cinéma, Théâtre, Concert, etc. |
| `Evenement_Categorie` | Table de liaison N-N Événement ↔ Catégorie |
| `Client` | Nom, email, mot_de_passe (bcrypt), téléphone, adresse, dateNaissance |
| `Billet` | numero_unique `TNG-YYYY-XXXXX`, statut ENUM, dateAchat, prix |
| `Administrateur` | Email, mot_de_passe (bcrypt), nom |

---

## Structure du projet

```
src/main/java/org/example/
├── Main.java                          → Navigation centralisée (StackPane unique)
├── database/
│   └── DatabaseConnection.java        → Singleton, port MAMP 8889
├── model/
│   ├── Evenement.java
│   ├── Seance.java
│   ├── Client.java
│   ├── Billet.java
│   ├── Salle.java
│   ├── ComplexeCulturel.java
│   ├── Categorie.java
│   ├── Administrateur.java
│   ├── SeanceAffichage.java           → Modèle JOIN Événement + Salle
│   └── BilletAffichage.java           → Modèle JOIN Client + Séance + Événement
├── dao/
│   ├── EvenementDAO.java
│   ├── SeanceDAO.java
│   ├── ClientDAO.java
│   ├── BilletDAO.java
│   ├── SalleDAO.java
│   ├── ComplexeCulturelDAO.java
│   ├── CategorieDAO.java
│   └── AdministrateurDAO.java
└── controller/
    ├── SelectionController.java
    ├── LoginController.java
    ├── DashboardController.java
    ├── EvenementController.java / EvenementFormController.java
    ├── ClientController.java / ClientFormController.java
    ├── SeanceController.java / SeanceFormController.java
    ├── BilletController.java / BilletFormController.java / BilletModifFormController.java
    ├── SalleController.java / SalleFormController.java
    ├── ClientLoginController.java
    ├── ClientInscriptionController.java
    ├── ClientDashboardController.java
    ├── ClientEvenementsController.java
    ├── ClientEvenementDetailController.java
    ├── ClientMesBilletsController.java
    └── ClientMonCompteController.java

src/main/resources/
└── org/example/
    └── *.fxml                         → Vues FXML (Scene Builder)

src/test/java/org/example/
├── ValidationTest.java
├── BilletTest.java
├── EvenementDAOTest.java
├── ClientDAOTest.java
└── BilletDAOTest.java
```

---

## Tests unitaires

57 tests JUnit 5 + Mockito — tous validés ✅

| Classe de test | Nb tests | Ce qui est testé |
|---|---|---|
| `ValidationTest` | 14 | Regex email et téléphone — cas valides et invalides |
| `BilletTest` | 8 | Génération numéro `TNG-YYYY-XXXXX`, format 5 chiffres, unicité |
| `EvenementDAOTest` | 11 | `getAll`, `getById`, `insert`, `update`, `delete` avec mock Mockito |
| `ClientDAOTest` | 12 | CRUD + email dupliqué + dateNaissance avec mock |
| `BilletDAOTest` | 12 | `getAllAvecDetails`, `getByClient`, `updateStatut`, `updateComplet` |

Pour lancer les tests :
```bash
mvn test
```

---

## Difficultés rencontrées

| Problème | Solution apportée |
|---|---|
| **Perte du plein écran** à chaque changement de vue (`new Scene()`) | Refactorisation complète : une seule scène avec `StackPane` racine. `Main.naviguerVers()` centralise tous les changements de vue. |
| **`fx:controller` invalide** après ajout d'un `ScrollPane` comme racine FXML | Déplacé `fx:controller` sur le `ScrollPane` dans tous les fichiers FXML. |
| **`setAll()` ambigu** — erreur de compilation sur `parent.getChildren().setAll(vue)` | Remplacé par `.clear()` + `.add()` dans tous les controllers. |
| **Dates affichées avec `T`** — format ISO par défaut de `LocalDateTime` | `setCellFactory` avec `DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")` sur toutes les colonnes. |
| **Popups ouvertes dans une fenêtre séparée** en plein écran | `alert.initOwner(Main.getStage())` ajouté systématiquement sur toutes les alertes et popups. |
| **`ClassCastException` dans l'autocomplétion `ComboBox`** — JavaFX caste `String` → `Client` lors de la sélection | Vérification `if (combo.getValue() instanceof Client) return;` + `setOnKeyPressed` pour réinitialiser. |
| **`getScene()` null** dans les `FormControllers` — le tableau n'est plus visible quand le formulaire est affiché | Lookup via un champ visible du formulaire (`titreLabel.getScene().lookup("#contentArea")`). |
| **`Location is not set`** — `getClass().getResource()` ne fonctionne pas dans une méthode statique | Remplacé par `Main.class.getResource(fxmlPath)` dans `naviguerVers()`. |
| **Hash bcrypt de 59 caractères** au lieu de 60 lors du copier-coller dans phpMyAdmin | Généré via `GenererHash.java` dans IntelliJ, sélection complète avec `Ctrl+A` dans la console. |
| **Anciens comptes clients incompatibles** avec bcrypt (mots de passe en clair) | Requête SQL ciblée : `DELETE FROM Client WHERE mot_de_passe NOT LIKE '$2a$%'`. |
| **`Client` connecté `null`** lors de l'achat de billet — `initialize()` appelé avant `setClient()` | Déplacé l'appel `afficherCards()` dans `setClient()` pour s'assurer que le client est défini avant la création des cards. |
| **Doublon numéro billet** `TNG-YYYY-XXXXX` — `COUNT` basé sur le nombre de billets, pas le max | Remplacé par `MAX(id_billet) + 1` via `BilletDAO.getMaxId()` pour garantir l'unicité même après suppressions. |
| **Colonne `places restantes`** dans `SeanceView` — erreur FXML ligne 45 | Colonne déclarée dans le FXML mais non liée dans le controller. Retirée du FXML et du controller. |

---

## Améliorations à apporter

- [ ] 📄 **Export PDF des billets** avec QR code de validation
- [ ] 📊 **Tableau de bord statistiques** (CA par événement, taux de remplissage, billets vendus)
- [ ] 💸 **Système de remboursement automatique** avec règles métier
- [ ] 📧 **Notifications email** de confirmation d'achat (JavaMail)
- [ ] 🔒 **Amélioration de la génération de numéro billet** en cas d'achats simultanés (UUID ou transaction SQL)
- [ ] 🌍 **Internationalisation (i18n)** pour une version multilingue

---

*SmartBillet — BTS SIO SLAM — Mars 2026*  
*Application JavaFX de billetterie — Architecture MVC/DAO — Java 21 + MySQL*
