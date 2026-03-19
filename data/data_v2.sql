SET FOREIGN_KEY_CHECKS=0;

USE `billetterie_v2`;

-- --------------------------------------------------------
-- ComplexeCulturel
-- --------------------------------------------------------

INSERT INTO `ComplexeCulturel` (`id_complexe`, `nom`, `adresse`) VALUES
(1, 'Théâtre du Parc', '12 avenue des Arts, 75001 Paris'),
(2, 'Centre Culturel Lumière', '5 rue de la Liberté, 69002 Lyon'),
(3, 'Espace Nov\'Ais', '23 boulevard du Quai, 31000 Toulouse');

-- --------------------------------------------------------
-- Salle (avec capacite)
-- --------------------------------------------------------

INSERT INTO `Salle` (`id_salle`, `nom`, `capacite`, `id_complexe`) VALUES
(1,  'Salle A - Théâtre',  350, 1),
(2,  'Salle B - Théâtre',  200, 1),
(3,  'Salle C - Théâtre',  100, 1),
(4,  'Salle D - Théâtre',   80, 1),
(5,  'Salle A - Centre',   400, 2),
(6,  'Salle B - Centre',   250, 2),
(7,  'Salle C - Centre',   150, 2),
(8,  'Salle D - Centre',    80, 2),
(9,  'Salle A - Espace',   500, 3),
(10, 'Salle B - Espace',   300, 3),
(11, 'Salle C - Espace',   180, 3),
(12, 'Salle D - Espace',    90, 3);

-- --------------------------------------------------------
-- Categorie
-- --------------------------------------------------------

INSERT INTO `Categorie` (`id_categorie`, `nom`) VALUES
(1, 'Théâtre'),
(2, 'Musique'),
(3, 'Danse'),
(4, 'Conférence'),
(5, 'Cinéma'),
(6, 'Jeune Public');

-- --------------------------------------------------------
-- Evenement
-- --------------------------------------------------------

INSERT INTO `Evenement` (`id_evenement`, `titre`, `affiche`, `description_courte`, `description_longue`, `duree`, `age_min`, `langue`, `prix_base`) VALUES
(1,  'La Nuit des Étoiles',        'nuit_etoiles.jpg',  'Concert symphonique sous la direction de M. Duval.',           'Un voyage musical exceptionnel à travers les grandes œuvres du répertoire classique et romantique, interprétées par l\'Orchestre Philharmonique de Paris.',  120, 6,  'Français', 35.00),
(2,  'Pièce: Le Voyageur',         'voyageur.jpg',      'Pièce contemporaine en 2 actes.',                              'Une exploration poétique et intense de la condition humaine à travers le regard d\'un voyageur qui traverse le temps et l\'espace.',                         90,  12, 'Français', 20.00),
(3,  'Festival Danse Libre',       'danse.jpg',         'Trois compagnies internationales présentent leurs créations.',  'Trois jours de performances exceptionnelles mêlant danse contemporaine, hip-hop et danse classique revisitée par des artistes du monde entier.',            150, 10, 'Français', 45.00),
(4,  'Conférence: L\'IA et nous',  'ia.jpg',            'Table ronde avec experts en IA.',                              'Des experts de renom débattent des enjeux éthiques, sociaux et économiques de l\'intelligence artificielle dans notre quotidien.',                          60,  16, 'Français', 12.50),
(5,  'Avant-première: Lumières',   'lumieres.jpg',      'Projection en avant-première du film Lumières.',               'Découvrez en exclusivité ce film poétique et visuel qui retrace l\'histoire de la lumière à travers les âges, suivi d\'une rencontre avec l\'équipe.',       110, 10, 'Français', 10.00),
(6,  'Concert Pop: Nova',          'nova.jpg',          'Concert du groupe Nova pour la tournée Horizons.',             'Le groupe Nova revient sur scène avec sa tournée Horizons, un spectacle immersif mêlant musique électro-pop, visuels époustouflants et performances live.',   100, 0,  'Français', 28.00),
(7,  'Atelier Marionnettes',       'marionnettes.jpg',  'Atelier pour enfants animé par la Compagnie Petit Pas.',       'Un atelier participatif et créatif où les enfants fabriquent et animent leurs propres marionnettes sous la guidance de la Compagnie Petit Pas.',            60,  3,  'Français', 6.00),
(8,  'Rencontre Auteur: Claire Morel', 'morel.jpg',     'Rencontre et dédicace avec l\'auteure Claire Morel.',          'Claire Morel, auteure du best-seller "Les Ombres du Matin", vous invite à une rencontre intime autour de son univers littéraire, suivie d\'une séance de dédicaces.', 90, 12, 'Français', 8.00),
(9,  'Soirée Jazz Intime',         'jazz.jpg',          'Trio de jazz dans une ambiance intimiste.',                    'Le Trio Ellington vous plonge dans une atmosphère feutrée et chaleureuse pour une soirée jazz mémorable, entre standards revisités et compositions originales.', 80, 0,  'Français', 22.00),
(10, 'Ciné-Plein Air',             'cine_pa.jpg',       'Projection en plein air d\'un classique du cinéma.',           'Installez-vous confortablement sous les étoiles pour (re)découvrir un chef-d\'œuvre du cinéma mondial dans une ambiance conviviale et festive.',              120, 0,  'Français', 5.00);

-- --------------------------------------------------------
-- Evenement_Categorie
-- --------------------------------------------------------

INSERT INTO `Evenement_Categorie` (`id_evenement`, `id_categorie`) VALUES
(1, 2),
(2, 1),
(2, 6),
(3, 3),
(4, 4),
(5, 5),
(5, 6),
(6, 2),
(6, 6),
(7, 1),
(7, 6),
(8, 5),
(9, 2),
(10, 5),
(10, 6);

-- --------------------------------------------------------
-- Seance (remplace Horaire + Evenement_Horaire + Evenement_Salle)
-- Chaque séance = événement + salle + date/heure précise
-- --------------------------------------------------------

INSERT INTO `Seance` (`id_seance`, `id_evenement`, `id_salle`, `dateHeure`, `prix`) VALUES
-- La Nuit des Étoiles (événement 1) - 3 séances
(1,  1, 1,  '2026-03-12 18:00:00', 35.00),
(2,  1, 1,  '2026-03-12 20:00:00', 35.00),
(3,  1, 8,  '2026-03-13 18:00:00', 35.00),
-- Pièce: Le Voyageur (événement 2) - 2 séances
(4,  2, 12, '2026-03-12 18:00:00', 20.00),
(5,  2, 12, '2026-03-12 20:00:00', 20.00),
-- Festival Danse Libre (événement 3) - 2 séances
(6,  3, 9,  '2026-04-05 18:00:00', 45.00),
(7,  3, 9,  '2026-04-06 20:00:00', 45.00),
-- Conférence: L'IA et nous (événement 4) - 3 séances
(8,  4, 7,  '2026-05-20 18:00:00', 12.50),
(9,  4, 7,  '2026-05-20 20:00:00', 12.50),
(10, 4, 8,  '2026-05-21 22:00:00', 12.50),
-- Avant-première: Lumières (événement 5) - 3 séances
(11, 5, 11, '2026-06-01 18:00:00', 10.00),
(12, 5, 11, '2026-06-01 20:00:00', 10.00),
(13, 5, 11, '2026-06-02 22:00:00', 10.00),
-- Concert Pop: Nova (événement 6) - 3 séances
(14, 6, 1,  '2026-06-15 18:00:00', 28.00),
(15, 6, 1,  '2026-06-15 20:00:00', 28.00),
(16, 6, 1,  '2026-06-16 22:00:00', 28.00),
-- Atelier Marionnettes (événement 7) - 3 séances
(17, 7, 6,  '2026-07-10 18:00:00', 6.00),
(18, 7, 6,  '2026-07-10 20:00:00', 6.00),
(19, 7, 6,  '2026-07-11 22:00:00', 6.00),
-- Rencontre Auteur (événement 8) - 2 séances
(20, 8, 8,  '2026-08-03 18:00:00', 8.00),
(21, 8, 12, '2026-08-03 20:00:00', 8.00),
-- Soirée Jazz Intime (événement 9) - 2 séances
(22, 9, 6,  '2026-09-12 18:00:00', 22.00),
(23, 9, 6,  '2026-09-12 20:00:00', 22.00),
-- Ciné-Plein Air (événement 10) - 2 séances
(24, 10, 3, '2026-07-20 18:00:00', 5.00),
(25, 10, 3, '2026-07-20 20:00:00', 5.00);

-- --------------------------------------------------------
-- Client (sans histoireAchat, sans mot_de_passe)
-- --------------------------------------------------------

INSERT INTO `Client` (`id_client`, `nom`, `email`, `telephone`, `adresse`, `dateNaissance`) VALUES
(1,  'Lucas Martin',    'lucas.martin@example.com',    '0643738222', '69 rue de Exemple, 75005 Paris',  '1990-01-14'),
(2,  'Emma Bernard',    'emma.bernard@example.com',    '0625058410', '37 rue de Exemple, 75014 Paris',  '1980-11-01'),
(3,  'Léa Dubois',      'lea.dubois@example.com',      '0625550723', '37 rue de Exemple, 75014 Paris',  '1997-08-18'),
(4,  'Hugo Thomas',     'hugo.thomas@example.com',     '0650566596', '27 rue de Exemple, 75017 Paris',  '1994-11-11'),
(5,  'Chloé Robert',    'chloe.robert@example.com',    '0629153706', '84 rue de Exemple, 75006 Paris',  '2003-01-20'),
(6,  'Noah Petit',      'noah.petit@example.com',      '0696114480', '61 rue de Exemple, 75017 Paris',  '1984-05-08'),
(7,  'Camille Durand',  'camille.durand@example.com',  '0633074480', '70 rue de Exemple, 75018 Paris',  '1987-12-20'),
(8,  'Lola Moreau',     'lola.moreau@example.com',     '0658271857', '7 rue de Exemple, 75006 Paris',   '1995-09-26'),
(9,  'Ethan Leroy',     'ethan.leroy@example.com',     '0663338320', '28 rue de Exemple, 75019 Paris',  '1982-09-19'),
(10, 'Manon Roux',      'manon.roux@example.com',      '0625739927', '105 rue de Exemple, 75009 Paris', '1984-04-10'),
(11, 'Julien Faure',    'julien.faure@example.com',    '0630005348', '48 rue de Exemple, 75011 Paris',  '1982-03-20'),
(12, 'Sarah Garnier',   'sarah.garnier@example.com',   '0643540631', '109 rue de Exemple, 75013 Paris', '2001-10-27'),
(13, 'Mathis Chevalier','mathis.chevalier@example.com','0695231463', '82 rue de Exemple, 75011 Paris',  '1993-05-28'),
(14, 'Zoé Marchand',    'zoe.marchand@example.com',    '0663723683', '47 rue de Exemple, 75010 Paris',  '1990-03-05'),
(15, 'Arthur Lemaire',  'arthur.lemaire@example.com',  '0680248623', '10 rue de Exemple, 75004 Paris',  '1997-01-26'),
(16, 'Inès Perrin',     'ines.perrin@example.com',     '0657869134', '81 rue de Exemple, 75013 Paris',  '2003-11-27'),
(17, 'Nathan Morin',    'nathan.morin@example.com',    '0648742999', '13 rue de Exemple, 75019 Paris',  '2002-08-22'),
(18, 'Mila Garcia',     'mila.garcia@example.com',     '0616357218', '7 rue de Exemple, 75008 Paris',   '2002-09-22'),
(19, 'Tom Blanc',       'tom.blanc@example.com',       '0632476190', '100 rue de Exemple, 75019 Paris', '1998-06-26'),
(20, 'Anaïs Gauthier',  'anais.gauthier@example.com',  '0647466542', '34 rue de Exemple, 75017 Paris',  '2003-01-25'),
(21, 'Adrien Muller',   'adrien.muller@example.com',   '0633780292', '109 rue de Exemple, 75007 Paris', '1999-11-28'),
(22, 'Clara Henry',     'clara.henry@example.com',     '0676664302', '113 rue de Exemple, 75017 Paris', '1997-02-06'),
(23, 'Louis Rousseau',  'louis.rousseau@example.com',  '0673369437', '59 rue de Exemple, 75001 Paris',  '1996-08-18'),
(24, 'Sacha Barbier',   'sacha.barbier@example.com',   '0648473063', '9 rue de Exemple, 75016 Paris',   '1992-05-06'),
(25, 'Lucie Colin',     'lucie.colin@example.com',     '0629998536', '2 rue de Exemple, 75012 Paris',   '1994-02-07'),
(26, 'Paul Lambert',    'paul.lambert@example.com',    '0619468745', '29 rue de Exemple, 75020 Paris',  '1988-06-14'),
(27, 'Julia Simon',     'julia.simon@example.com',     '0692645783', '6 rue de Exemple, 75007 Paris',   '1999-03-11'),
(28, 'Maxime Michel',   'maxime.michel@example.com',   '0666105228', '118 rue de Exemple, 75004 Paris', '1986-03-10'),
(29, 'Laura David',     'laura.david@example.com',     '0624976695', '62 rue de Exemple, 75018 Paris',  '1996-05-26'),
(30, 'Simon Leclerc',   'simon.leclerc@example.com',   '0619576098', '75 rue de Exemple, 75005 Paris',  '1995-08-13');

-- --------------------------------------------------------
-- Billet (id auto, numero_unique, id_seance au lieu de id_horaire, statut)
-- --------------------------------------------------------

INSERT INTO `Billet` (`id_billet`, `numero_unique`, `id_client`, `id_seance`, `dateAchat`, `prix`, `statut`) VALUES
(1,  'TNG-2026-00001', 1,  1,  '2026-02-05 14:30:00', 35.00, 'valide'),
(2,  'TNG-2026-00002', 1,  11, '2026-05-05 10:00:00', 10.00, 'valide'),
(3,  'TNG-2026-00003', 1,  22, '2026-08-15 14:00:00', 22.00, 'valide'),
(4,  'TNG-2026-00004', 2,  4,  '2026-02-08 08:45:00', 20.00, 'valide'),
(5,  'TNG-2026-00005', 2,  14, '2026-05-10 09:00:00', 28.00, 'valide'),
(6,  'TNG-2026-00006', 2,  24, '2026-06-22 13:30:00',  5.00, 'valide'),
(7,  'TNG-2026-00007', 3,  6,  '2026-03-01 11:00:00', 45.00, 'valide'),
(8,  'TNG-2026-00008', 3,  17, '2026-06-15 13:15:00',  6.00, 'valide'),
(9,  'TNG-2026-00009', 4,  8,  '2026-04-15 16:00:00', 12.50, 'valide'),
(10, 'TNG-2026-00010', 4,  20, '2026-07-10 10:30:00',  8.00, 'valide'),
(11, 'TNG-2026-00011', 5,  1,  '2026-02-10 09:15:00', 35.00, 'valide'),
(12, 'TNG-2026-00012', 5,  14, '2026-05-15 15:30:00', 28.00, 'valide'),
(13, 'TNG-2026-00013', 5,  22, '2026-08-20 10:30:00', 22.00, 'valide'),
(14, 'TNG-2026-00014', 6,  4,  '2026-02-12 15:20:00', 20.00, 'valide'),
(15, 'TNG-2026-00015', 6,  11, '2026-05-10 14:45:00', 10.00, 'valide'),
(16, 'TNG-2026-00016', 6,  24, '2026-06-26 10:00:00',  5.00, 'annule'),
(17, 'TNG-2026-00017', 7,  6,  '2026-03-08 13:45:00', 45.00, 'valide'),
(18, 'TNG-2026-00018', 7,  17, '2026-06-20 10:45:00',  6.00, 'valide'),
(19, 'TNG-2026-00019', 8,  8,  '2026-04-20 11:30:00', 12.50, 'valide'),
(20, 'TNG-2026-00020', 8,  20, '2026-07-15 15:00:00',  8.00, 'valide'),
(21, 'TNG-2026-00021', 9,  5,  '2026-02-16 12:00:00', 20.00, 'valide'),
(22, 'TNG-2026-00022', 9,  15, '2026-05-20 12:45:00', 28.00, 'valide'),
(23, 'TNG-2026-00023', 9,  25, '2026-07-01 16:20:00',  5.00, 'valide'),
(24, 'TNG-2026-00024', 10, 9,  '2026-04-25 14:15:00', 12.50, 'valide'),
(25, 'TNG-2026-00025', 10, 23, '2026-08-25 16:45:00', 22.00, 'valide'),
(26, 'TNG-2026-00026', 11, 7,  '2026-03-12 09:20:00', 45.00, 'valide'),
(27, 'TNG-2026-00027', 11, 18, '2026-06-25 17:20:00',  6.00, 'valide'),
(28, 'TNG-2026-00028', 12, 2,  '2026-02-15 16:45:00', 35.00, 'valide'),
(29, 'TNG-2026-00029', 12, 21, '2026-07-20 12:20:00',  8.00, 'valide'),
(30, 'TNG-2026-00030', 13, 12, '2026-05-15 09:30:00', 10.00, 'valide'),
(31, 'TNG-2026-00031', 13, 23, '2026-08-30 12:15:00', 22.00, 'valide'),
(32, 'TNG-2026-00032', 14, 5,  '2026-02-22 17:30:00', 20.00, 'valide'),
(33, 'TNG-2026-00033', 14, 15, '2026-05-28 10:20:00', 28.00, 'valide'),
(34, 'TNG-2026-00034', 14, 25, '2026-07-05 14:45:00',  5.00, 'rembourse'),
(35, 'TNG-2026-00035', 15, 7,  '2026-03-18 18:00:00', 45.00, 'valide'),
(36, 'TNG-2026-00036', 15, 18, '2026-06-28 09:30:00',  6.00, 'valide'),
(37, 'TNG-2026-00037', 16, 9,  '2026-05-01 09:00:00', 12.50, 'valide'),
(38, 'TNG-2026-00038', 16, 21, '2026-07-24 09:45:00',  8.00, 'valide'),
(39, 'TNG-2026-00039', 17, 12, '2026-05-20 16:00:00', 10.00, 'valide'),
(40, 'TNG-2026-00040', 17, 24, '2026-07-10 09:15:00',  5.00, 'valide'),
(41, 'TNG-2026-00041', 18, 2,  '2026-02-20 11:20:00', 35.00, 'valide'),
(42, 'TNG-2026-00042', 18, 22, '2026-09-02 09:20:00', 22.00, 'valide'),
(43, 'TNG-2026-00043', 19, 4,  '2026-02-26 10:15:00', 20.00, 'valide'),
(44, 'TNG-2026-00044', 19, 16, '2026-06-02 16:50:00', 28.00, 'valide'),
(45, 'TNG-2026-00045', 20, 6,  '2026-03-22 14:30:00', 45.00, 'valide'),
(46, 'TNG-2026-00046', 20, 19, '2026-07-01 14:50:00',  6.00, 'valide'),
(47, 'TNG-2026-00047', 21, 10, '2026-05-05 17:45:00', 12.50, 'valide'),
(48, 'TNG-2026-00048', 21, 20, '2026-07-28 17:30:00',  8.00, 'valide'),
(49, 'TNG-2026-00049', 22, 5,  '2026-03-02 14:00:00', 20.00, 'valide'),
(50, 'TNG-2026-00050', 22, 13, '2026-05-22 11:15:00', 10.00, 'valide'),
(51, 'TNG-2026-00051', 22, 23, '2026-09-06 15:50:00', 22.00, 'valide'),
(52, 'TNG-2026-00052', 23, 3,  '2026-02-25 19:00:00', 35.00, 'valide'),
(53, 'TNG-2026-00053', 23, 16, '2026-06-06 14:00:00', 28.00, 'valide'),
(54, 'TNG-2026-00054', 23, 25, '2026-07-14 11:50:00',  5.00, 'valide'),
(55, 'TNG-2026-00055', 24, 10, '2026-05-10 12:20:00', 12.50, 'annule'),
(56, 'TNG-2026-00056', 24, 21, '2026-07-31 13:10:00',  8.00, 'valide'),
(57, 'TNG-2026-00057', 25, 7,  '2026-03-28 10:45:00', 45.00, 'valide'),
(58, 'TNG-2026-00058', 25, 19, '2026-07-05 11:00:00',  6.00, 'valide'),
(59, 'TNG-2026-00059', 26, 4,  '2026-03-07 09:30:00', 20.00, 'valide'),
(60, 'TNG-2026-00060', 26, 13, '2026-05-26 13:40:00', 10.00, 'valide'),
(61, 'TNG-2026-00061', 26, 24, '2026-07-18 15:30:00',  5.00, 'valide'),
(62, 'TNG-2026-00062', 27, 3,  '2026-03-01 13:30:00', 35.00, 'valide'),
(63, 'TNG-2026-00063', 27, 14, '2026-06-10 11:30:00', 28.00, 'valide'),
(64, 'TNG-2026-00064', 27, 22, '2026-09-10 11:10:00', 22.00, 'valide'),
(65, 'TNG-2026-00065', 28, 8,  '2026-05-15 15:30:00', 12.50, 'valide'),
(66, 'TNG-2026-00066', 28, 20, '2026-08-01 11:40:00',  8.00, 'valide'),
(67, 'TNG-2026-00067', 29, 6,  '2026-04-01 16:20:00', 45.00, 'valide'),
(68, 'TNG-2026-00068', 29, 17, '2026-07-08 16:15:00',  6.00, 'valide'),
(69, 'TNG-2026-00069', 30, 1,  '2026-03-05 10:00:00', 35.00, 'valide'),
(70, 'TNG-2026-00070', 30, 11, '2026-05-29 18:20:00', 10.00, 'valide');

-- --------------------------------------------------------
-- Administrateur
-- --------------------------------------------------------

INSERT INTO `Administrateur` (`id_admin`, `email`, `mot_de_passe`, `nom`) VALUES
(1, 'admin@ticngo.fr', '$2b$12$placeholder_hash_bcrypt', 'Admin Principal');

SET FOREIGN_KEY_CHECKS=1;
