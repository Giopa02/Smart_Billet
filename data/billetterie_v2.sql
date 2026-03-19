-- ============================================
-- Base de données : billetterie_v2
-- Projet : Tic'n Go - Back-office Admin
-- ============================================

CREATE DATABASE IF NOT EXISTS `billetterie_v2`
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_0900_ai_ci;

USE `billetterie_v2`;

-- --------------------------------------------------------
-- Table : ComplexeCulturel
-- Un complexe est un lieu qui contient plusieurs salles
-- --------------------------------------------------------

CREATE TABLE `ComplexeCulturel` (
  `id_complexe` int NOT NULL AUTO_INCREMENT,
  `nom`         varchar(100) NOT NULL,
  `adresse`     varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id_complexe`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------
-- Table : Salle
-- Une salle appartient à un complexe
-- --------------------------------------------------------

CREATE TABLE `Salle` (
  `id_salle`    int NOT NULL AUTO_INCREMENT,
  `nom`         varchar(100) NOT NULL,
  `capacite`    int DEFAULT NULL,
  `id_complexe` int NOT NULL,
  PRIMARY KEY (`id_salle`),
  CONSTRAINT `fk_salle_complexe` FOREIGN KEY (`id_complexe`)
    REFERENCES `ComplexeCulturel` (`id_complexe`)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------
-- Table : Categorie
-- Ex : Comédie, Concert, One-man-show, etc.
-- --------------------------------------------------------

CREATE TABLE `Categorie` (
  `id_categorie` int NOT NULL AUTO_INCREMENT,
  `nom`          varchar(50) NOT NULL,
  PRIMARY KEY (`id_categorie`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------
-- Table : Evenement
-- Un spectacle/événement, sans date (la date est dans Seance)
-- --------------------------------------------------------

CREATE TABLE `Evenement` (
  `id_evenement`      int NOT NULL AUTO_INCREMENT,
  `titre`             varchar(100) NOT NULL,
  `affiche`           varchar(255) DEFAULT NULL,
  `description_courte` varchar(255) DEFAULT NULL,
  `description_longue` text DEFAULT NULL,
  `duree`             int DEFAULT NULL COMMENT 'Durée en minutes',
  `age_min`           int DEFAULT NULL COMMENT 'Âge minimum requis, NULL si aucun',
  `langue`            varchar(50) DEFAULT NULL,
  `prix_base`         decimal(6,2) DEFAULT NULL,
  PRIMARY KEY (`id_evenement`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------
-- Table : Evenement_Categorie
-- Liaison N-N entre événements et catégories
-- --------------------------------------------------------

CREATE TABLE `Evenement_Categorie` (
  `id_evenement`  int NOT NULL,
  `id_categorie`  int NOT NULL,
  PRIMARY KEY (`id_evenement`, `id_categorie`),
  CONSTRAINT `fk_ec_evenement` FOREIGN KEY (`id_evenement`)
    REFERENCES `Evenement` (`id_evenement`)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_ec_categorie` FOREIGN KEY (`id_categorie`)
    REFERENCES `Categorie` (`id_categorie`)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------
-- Table : Seance
-- Une séance = un événement joué dans une salle à une date/heure
-- --------------------------------------------------------

CREATE TABLE `Seance` (
  `id_seance`    int NOT NULL AUTO_INCREMENT,
  `id_evenement` int NOT NULL,
  `id_salle`     int NOT NULL,
  `dateHeure`    datetime NOT NULL,
  `prix`         decimal(6,2) DEFAULT NULL COMMENT 'Prix spécifique à la séance, peut différer du prix_base',
  PRIMARY KEY (`id_seance`),
  CONSTRAINT `fk_seance_evenement` FOREIGN KEY (`id_evenement`)
    REFERENCES `Evenement` (`id_evenement`)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_seance_salle` FOREIGN KEY (`id_salle`)
    REFERENCES `Salle` (`id_salle`)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------
-- Table : Client
-- Les clients qui achètent des billets
-- L'historique d'achat se consulte via la table Billet
-- --------------------------------------------------------

CREATE TABLE `Client` (
  `id_client`     int NOT NULL AUTO_INCREMENT,
  `nom`           varchar(100) NOT NULL,
  `email`         varchar(100) NOT NULL,
  `telephone`     varchar(15) DEFAULT NULL,
  `adresse`       varchar(255) DEFAULT NULL,
  `dateNaissance` date DEFAULT NULL,
  PRIMARY KEY (`id_client`),
  UNIQUE KEY `uq_client_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------
-- Table : Billet
-- Un billet = un client qui achète une place pour une séance
-- --------------------------------------------------------

CREATE TABLE `Billet` (
  `id_billet`      int NOT NULL AUTO_INCREMENT,
  `numero_unique`  varchar(50) NOT NULL COMMENT 'Numéro affiché sur le billet, ex: TNG-2024-00001',
  `id_client`      int NOT NULL,
  `id_seance`      int NOT NULL,
  `dateAchat`      datetime DEFAULT CURRENT_TIMESTAMP,
  `prix`           decimal(6,2) NOT NULL,
  `statut`         enum('valide','annule','rembourse') NOT NULL DEFAULT 'valide',
  PRIMARY KEY (`id_billet`),
  UNIQUE KEY `uq_numero_unique` (`numero_unique`),
  CONSTRAINT `fk_billet_client` FOREIGN KEY (`id_client`)
    REFERENCES `Client` (`id_client`)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_billet_seance` FOREIGN KEY (`id_seance`)
    REFERENCES `Seance` (`id_seance`)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------
-- Table : Administrateur
-- Accès au back-office
-- --------------------------------------------------------

CREATE TABLE `Administrateur` (
  `id_admin`       int NOT NULL AUTO_INCREMENT,
  `email`          varchar(100) NOT NULL,
  `mot_de_passe`   varchar(255) NOT NULL COMMENT 'Hashé avec bcrypt',
  `nom`            varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id_admin`),
  UNIQUE KEY `uq_admin_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ============================================
-- Données de test
-- ============================================

INSERT INTO `ComplexeCulturel` (`nom`, `adresse`) VALUES
('Le Grand Rex', '1 Boulevard Poissonnière, 75002 Paris'),
('Zénith de Paris', '211 Avenue Jean Jaurès, 75019 Paris');

INSERT INTO `Salle` (`nom`, `capacite`, `id_complexe`) VALUES
('Grande Salle', 2800, 1),
('Salle Pleyel', 500, 1),
('Salle principale', 6300, 2);

INSERT INTO `Categorie` (`nom`) VALUES
('Comédie'),
('Concert'),
('One-man-show'),
('Comédie musicale'),
('Théâtre');

INSERT INTO `Evenement` (`titre`, `description_courte`, `duree`, `age_min`, `langue`, `prix_base`) VALUES
('Les Misérables', 'Le chef-d\'oeuvre de Victor Hugo adapté en comédie musicale', 180, NULL, 'Français', 45.00),
('Gad Elmaleh - Sans Tambour', 'Le retour du roi de l\'humour français', 120, 12, 'Français', 35.00),
('Carmen', 'L\'opéra de Bizet dans une mise en scène moderne', 150, NULL, 'Français', 55.00);

INSERT INTO `Evenement_Categorie` (`id_evenement`, `id_categorie`) VALUES
(1, 4),
(2, 3),
(3, 5);

INSERT INTO `Seance` (`id_evenement`, `id_salle`, `dateHeure`, `prix`) VALUES
(1, 1, '2025-04-10 20:00:00', 45.00),
(1, 1, '2025-04-11 15:00:00', 40.00),
(2, 2, '2025-04-15 21:00:00', 35.00),
(3, 3, '2025-04-20 19:30:00', 55.00);

INSERT INTO `Client` (`nom`, `email`, `telephone`, `adresse`, `dateNaissance`) VALUES
('Sophie Martin', 'sophie.martin@email.com', '0612345678', '12 rue de la Paix, Paris', '1990-03-15'),
('Lucas Dubois', 'lucas.dubois@email.com', '0687654321', '5 avenue Montaigne, Paris', '1985-07-22'),
('Emma Leroy', 'emma.leroy@email.com', NULL, '8 rue du Faubourg, Lyon', '2000-11-08');

INSERT INTO `Billet` (`numero_unique`, `id_client`, `id_seance`, `prix`, `statut`) VALUES
('TNG-2025-00001', 1, 1, 45.00, 'valide'),
('TNG-2025-00002', 2, 1, 45.00, 'valide'),
('TNG-2025-00003', 1, 3, 35.00, 'valide'),
('TNG-2025-00004', 3, 4, 55.00, 'annule');

INSERT INTO `Administrateur` (`email`, `mot_de_passe`, `nom`) VALUES
('admin@ticngo.fr', '$2b$12$placeholder_hash_bcrypt', 'Admin Principal');
