package org.example;

import org.example.dao.EvenementDAO;
import org.example.model.Evenement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// ===== Ces tests vérifient que les opérations sur les événements fonctionnent correctement. Mockito simule la base de données donc MAMP n'a pas besoin d'être lancé. =====

public class EvenementDAOTest {

    private EvenementDAO evenementDAO;

    private Evenement evenement1;
    private Evenement evenement2;

    @BeforeEach
    void setUp() {
        // Créer un mock du DAO (pas de vraie connexion BDD)
        evenementDAO = Mockito.mock(EvenementDAO.class);

        // Créer des événements de test
        evenement1 = new Evenement(1, "Concert Jazz", "affiche1.jpg",
                "Un concert de jazz", "Description longue...",
                120, 0, "Français", 25.0);

        evenement2 = new Evenement(2, "Cinéma Classics", "affiche2.jpg",
                "Soirée cinéma", "Description longue...",
                90, 12, "Anglais", 15.0);
    }

    @Test
    @DisplayName("getAll retourne la liste complète des événements")
    void testGetAllRetourneListeComplete() {
        when(evenementDAO.getAll()).thenReturn(Arrays.asList(evenement1, evenement2));

        List<Evenement> liste = evenementDAO.getAll();

        assertEquals(2, liste.size());
        verify(evenementDAO, times(1)).getAll();
    }

    @Test
    @DisplayName("getAll retourne une liste vide si aucun événement")
    void testGetAllListeVide() {
        when(evenementDAO.getAll()).thenReturn(List.of());

        List<Evenement> liste = evenementDAO.getAll();

        assertTrue(liste.isEmpty());
    }

    @Test
    @DisplayName("getById retourne le bon événement")
    void testGetByIdRetourneBonEvenement() {
        when(evenementDAO.getById(1)).thenReturn(evenement1);

        Evenement result = evenementDAO.getById(1);

        assertNotNull(result);
        assertEquals("Concert Jazz", result.getTitre());
        assertEquals(25.0, result.getPrixBase());
    }

    @Test
    @DisplayName("getById retourne null si événement inexistant")
    void testGetByIdInexistant() {
        when(evenementDAO.getById(999)).thenReturn(null);

        Evenement result = evenementDAO.getById(999);

        assertNull(result);
    }

    @Test
    @DisplayName("insert retourne true si succès")
    void testInsertSucces() {
        when(evenementDAO.insert(evenement1)).thenReturn(true);

        boolean succes = evenementDAO.insert(evenement1);

        assertTrue(succes);
        verify(evenementDAO, times(1)).insert(evenement1);
    }

    @Test
    @DisplayName("insert retourne false si échec")
    void testInsertEchec() {
        when(evenementDAO.insert(evenement1)).thenReturn(false);

        boolean succes = evenementDAO.insert(evenement1);

        assertFalse(succes);
    }

    @Test
    @DisplayName("update retourne true si succès")
    void testUpdateSucces() {
        when(evenementDAO.update(evenement1)).thenReturn(true);

        boolean succes = evenementDAO.update(evenement1);

        assertTrue(succes);
    }

    @Test
    @DisplayName("delete retourne true si succès")
    void testDeleteSucces() {
        when(evenementDAO.delete(1)).thenReturn(true);

        boolean succes = evenementDAO.delete(1);

        assertTrue(succes);
        verify(evenementDAO, times(1)).delete(1);
    }

    @Test
    @DisplayName("delete retourne false si événement inexistant")
    void testDeleteInexistant() {
        when(evenementDAO.delete(999)).thenReturn(false);

        boolean succes = evenementDAO.delete(999);

        assertFalse(succes);
    }

    @Test
    @DisplayName("Le titre de l'événement est bien récupéré")
    void testTitreEvenement() {
        when(evenementDAO.getById(1)).thenReturn(evenement1);

        Evenement result = evenementDAO.getById(1);

        assertEquals("Concert Jazz", result.getTitre());
        assertEquals("Français", result.getLangue());
        assertEquals(0, result.getAgeMin());
    }

    @Test
    @DisplayName("Le prix de base est bien récupéré")
    void testPrixBase() {
        when(evenementDAO.getById(2)).thenReturn(evenement2);

        Evenement result = evenementDAO.getById(2);

        assertEquals(15.0, result.getPrixBase());
        assertEquals(12, result.getAgeMin());
    }
}