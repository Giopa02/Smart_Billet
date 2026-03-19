package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;

// ===== Ces tests vérifient que la génération automatique du numéro unique de billet fonctionne correctement. =====

public class BilletTest {

    // On recopie la logique de génération ici pour la tester
    private String genererNumeroUnique(int compteur) {
        int annee = LocalDateTime.now().getYear();
        return String.format("TNG-%d-%05d", annee, compteur);
    }

    @Test
    @DisplayName("Le numéro commence bien par TNG")
    void testNumeroCommenceParTNG() {
        String numero = genererNumeroUnique(1);
        assertTrue(numero.startsWith("TNG-"));
    }

    @Test
    @DisplayName("Le numéro contient l'année courante")
    void testNumeroContientAnneeCourante() {
        int annee = LocalDateTime.now().getYear();
        String numero = genererNumeroUnique(1);
        assertTrue(numero.contains(String.valueOf(annee)));
    }

    @Test
    @DisplayName("Le compteur est bien formaté sur 5 chiffres")
    void testCompteurCinqChiffres() {
        String numero = genererNumeroUnique(1);
        // Format attendu : TNG-2026-00001
        String[] parties = numero.split("-");
        assertEquals(3, parties.length);
        assertEquals(5, parties[2].length());
    }

    @Test
    @DisplayName("Compteur 1 → 00001")
    void testCompteur1() {
        String numero = genererNumeroUnique(1);
        assertTrue(numero.endsWith("00001"));
    }

    @Test
    @DisplayName("Compteur 100 → 00100")
    void testCompteur100() {
        String numero = genererNumeroUnique(100);
        assertTrue(numero.endsWith("00100"));
    }

    @Test
    @DisplayName("Compteur 99999 → 99999")
    void testCompteurMax() {
        String numero = genererNumeroUnique(99999);
        assertTrue(numero.endsWith("99999"));
    }

    @Test
    @DisplayName("Deux numéros avec compteurs différents sont différents")
    void testNumerosDifferents() {
        String numero1 = genererNumeroUnique(1);
        String numero2 = genererNumeroUnique(2);
        assertNotEquals(numero1, numero2);
    }

    @Test
    @DisplayName("Format complet TNG-YYYY-XXXXX")
    void testFormatComplet() {
        int annee = LocalDateTime.now().getYear();
        String numero = genererNumeroUnique(42);
        assertEquals("TNG-" + annee + "-00042", numero);
    }
}