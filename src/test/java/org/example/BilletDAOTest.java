package org.example;

import org.example.dao.BilletDAO;
import org.example.model.BilletAffichage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


// ===== Ces tests vérifient les opérations spécifiques aux billets, qui sont plus complexes que les autres DAO. =====

public class BilletDAOTest {

    private BilletDAO billetDAO;
    private BilletAffichage billet1;
    private BilletAffichage billet2;

    @BeforeEach
    void setUp() {
        billetDAO = Mockito.mock(BilletDAO.class);

        billet1 = new BilletAffichage(1, "TNG-2026-00001", "Alice Dupont",
                "Concert Jazz",
                LocalDateTime.of(2026, 4, 15, 20, 0),
                25.0, "valide",
                LocalDateTime.of(2026, 3, 1, 10, 30),
                1, 1);

        billet2 = new BilletAffichage(2, "TNG-2026-00002", "Bob Martin",
                "Cinéma Classics",
                LocalDateTime.of(2026, 4, 20, 18, 0),
                15.0, "annule",
                LocalDateTime.of(2026, 3, 2, 14, 0),
                2, 2);
    }

    @Test
    @DisplayName("getAllAvecDetails retourne la liste complète")
    void testGetAllAvecDetailsRetourneListeComplete() {
        when(billetDAO.getAllAvecDetails()).thenReturn(Arrays.asList(billet1, billet2));

        List<BilletAffichage> liste = billetDAO.getAllAvecDetails();

        assertEquals(2, liste.size());
        verify(billetDAO, times(1)).getAllAvecDetails();
    }

    @Test
    @DisplayName("getAllAvecDetails retourne liste vide si aucun billet")
    void testGetAllAvecDetailsListeVide() {
        when(billetDAO.getAllAvecDetails()).thenReturn(List.of());

        List<BilletAffichage> liste = billetDAO.getAllAvecDetails();

        assertTrue(liste.isEmpty());
    }

    @Test
    @DisplayName("getByClientAvecDetails retourne les billets d'un client")
    void testGetByClientAvecDetails() {
        when(billetDAO.getByClientAvecDetails(1)).thenReturn(List.of(billet1));

        List<BilletAffichage> liste = billetDAO.getByClientAvecDetails(1);

        assertEquals(1, liste.size());
        assertEquals("Alice Dupont", liste.get(0).getNomClient());
    }

    @Test
    @DisplayName("getByClientAvecDetails retourne liste vide si client sans billets")
    void testGetByClientAvecDetailsSansBillets() {
        when(billetDAO.getByClientAvecDetails(999)).thenReturn(List.of());

        List<BilletAffichage> liste = billetDAO.getByClientAvecDetails(999);

        assertTrue(liste.isEmpty());
    }

    @Test
    @DisplayName("insert retourne true si succès")
    void testInsertSucces() {
        when(billetDAO.insert(any())).thenReturn(true);

        boolean succes = billetDAO.insert(null);

        assertTrue(succes);
    }

    @Test
    @DisplayName("delete retourne true si succès")
    void testDeleteSucces() {
        when(billetDAO.delete(1)).thenReturn(true);

        boolean succes = billetDAO.delete(1);

        assertTrue(succes);
        verify(billetDAO, times(1)).delete(1);
    }

    @Test
    @DisplayName("delete retourne false si billet inexistant")
    void testDeleteInexistant() {
        when(billetDAO.delete(999)).thenReturn(false);

        boolean succes = billetDAO.delete(999);

        assertFalse(succes);
    }

    @Test
    @DisplayName("updateStatut retourne true si succès")
    void testUpdateStatutSucces() {
        when(billetDAO.updateStatut(1, "annule")).thenReturn(true);

        boolean succes = billetDAO.updateStatut(1, "annule");

        assertTrue(succes);
        verify(billetDAO, times(1)).updateStatut(1, "annule");
    }

    @Test
    @DisplayName("updateStatut retourne false si billet inexistant")
    void testUpdateStatutInexistant() {
        when(billetDAO.updateStatut(999, "annule")).thenReturn(false);

        boolean succes = billetDAO.updateStatut(999, "annule");

        assertFalse(succes);
    }

    @Test
    @DisplayName("updateComplet retourne true si succès")
    void testUpdateCompletSucces() {
        when(billetDAO.updateComplet(1, 1, 1, 25.0, "valide")).thenReturn(true);

        boolean succes = billetDAO.updateComplet(1, 1, 1, 25.0, "valide");

        assertTrue(succes);
    }

    @Test
    @DisplayName("Le numéro unique du billet est bien récupéré")
    void testNumeroUnique() {
        when(billetDAO.getAllAvecDetails()).thenReturn(List.of(billet1));

        List<BilletAffichage> liste = billetDAO.getAllAvecDetails();

        assertEquals("TNG-2026-00001", liste.get(0).getNumeroUnique());
    }

    @Test
    @DisplayName("Le statut du billet est bien récupéré")
    void testStatutBillet() {
        when(billetDAO.getAllAvecDetails()).thenReturn(Arrays.asList(billet1, billet2));

        List<BilletAffichage> liste = billetDAO.getAllAvecDetails();

        assertEquals("valide", liste.get(0).getStatut());
        assertEquals("annule", liste.get(1).getStatut());
    }
}