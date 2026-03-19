package org.example;

import org.example.dao.ClientDAO;
import org.example.model.Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// ===== Même principe que EvenementDAOTest mais pour les clients. =====

public class ClientDAOTest {

    private ClientDAO clientDAO;
    private Client client1;
    private Client client2;

    @BeforeEach
    void setUp() {
        clientDAO = Mockito.mock(ClientDAO.class);

        client1 = new Client(1, "Alice Dupont", "alice@gmail.com",
                "0612345678", "12 rue de Paris, Paris",
                LocalDate.of(1995, 3, 15));

        client2 = new Client(2, "Bob Martin", "bob@orange.fr",
                "0787654321", "5 avenue Victor Hugo, Lyon",
                LocalDate.of(1988, 7, 22));
    }

    @Test
    @DisplayName("getAll retourne la liste complète des clients")
    void testGetAllRetourneListeComplete() {
        when(clientDAO.getAll()).thenReturn(Arrays.asList(client1, client2));

        List<Client> liste = clientDAO.getAll();

        assertEquals(2, liste.size());
        verify(clientDAO, times(1)).getAll();
    }

    @Test
    @DisplayName("getAll retourne liste vide si aucun client")
    void testGetAllListeVide() {
        when(clientDAO.getAll()).thenReturn(List.of());

        List<Client> liste = clientDAO.getAll();

        assertTrue(liste.isEmpty());
    }

    @Test
    @DisplayName("getById retourne le bon client")
    void testGetByIdRetourneBonClient() {
        when(clientDAO.getById(1)).thenReturn(client1);

        Client result = clientDAO.getById(1);

        assertNotNull(result);
        assertEquals("Alice Dupont", result.getNom());
        assertEquals("alice@gmail.com", result.getEmail());
    }

    @Test
    @DisplayName("getById retourne null si client inexistant")
    void testGetByIdInexistant() {
        when(clientDAO.getById(999)).thenReturn(null);

        Client result = clientDAO.getById(999);

        assertNull(result);
    }

    @Test
    @DisplayName("insert retourne true si succès")
    void testInsertSucces() {
        when(clientDAO.insert(client1)).thenReturn(true);

        boolean succes = clientDAO.insert(client1);

        assertTrue(succes);
        verify(clientDAO, times(1)).insert(client1);
    }

    @Test
    @DisplayName("insert retourne false si email déjà existant")
    void testInsertEmailDuplique() {
        when(clientDAO.insert(client1)).thenReturn(false);

        boolean succes = clientDAO.insert(client1);

        assertFalse(succes);
    }

    @Test
    @DisplayName("update retourne true si succès")
    void testUpdateSucces() {
        when(clientDAO.update(client1)).thenReturn(true);

        boolean succes = clientDAO.update(client1);

        assertTrue(succes);
        verify(clientDAO, times(1)).update(client1);
    }

    @Test
    @DisplayName("update retourne false si client inexistant")
    void testUpdateInexistant() {
        Client clientInexistant = new Client(999, "Inconnu", "inconnu@gmail.com",
                "0600000000", "", null);
        when(clientDAO.update(clientInexistant)).thenReturn(false);

        boolean succes = clientDAO.update(clientInexistant);

        assertFalse(succes);
    }

    @Test
    @DisplayName("delete retourne true si succès")
    void testDeleteSucces() {
        when(clientDAO.delete(1)).thenReturn(true);

        boolean succes = clientDAO.delete(1);

        assertTrue(succes);
        verify(clientDAO, times(1)).delete(1);
    }

    @Test
    @DisplayName("delete retourne false si client inexistant")
    void testDeleteInexistant() {
        when(clientDAO.delete(999)).thenReturn(false);

        boolean succes = clientDAO.delete(999);

        assertFalse(succes);
    }

    @Test
    @DisplayName("La date de naissance est bien récupérée")
    void testDateNaissance() {
        when(clientDAO.getById(1)).thenReturn(client1);

        Client result = clientDAO.getById(1);

        assertEquals(LocalDate.of(1995, 3, 15), result.getDateNaissance());
    }

    @Test
    @DisplayName("Le téléphone est bien récupéré")
    void testTelephone() {
        when(clientDAO.getById(2)).thenReturn(client2);

        Client result = clientDAO.getById(2);

        assertEquals("0787654321", result.getTelephone());
    }
}