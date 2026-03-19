package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;


// ===== Ces tests vérifient que les règles de format saisies par l'utilisateur sont correctes, sans toucher à la base de données. =====

public class ValidationTest {

    // ===== VALIDATION EMAIL =====

    private boolean validerEmail(String email) {
        return email != null && email.matches("^[\\w._%+\\-]+@[\\w.\\-]+\\.(fr|com|net|org|eu)$");
    }

    @Test
    @DisplayName("Email valide avec .com")
    void testEmailValideAvecCom() {
        assertTrue(validerEmail("test@gmail.com"));
    }

    @Test
    @DisplayName("Email valide avec .fr")
    void testEmailValideAvecFr() {
        assertTrue(validerEmail("prenom.nom@orange.fr"));
    }

    @Test
    @DisplayName("Email sans arobase invalide")
    void testEmailSansArobase() {
        assertFalse(validerEmail("testgmail.com"));
    }

    @Test
    @DisplayName("Email sans extension invalide")
    void testEmailSansExtension() {
        assertFalse(validerEmail("test@gmail"));
    }

    @Test
    @DisplayName("Email avec extension non supportée invalide")
    void testEmailExtensionNonSupportee() {
        assertFalse(validerEmail("test@gmail.xyz"));
    }

    @Test
    @DisplayName("Email null invalide")
    void testEmailNull() {
        assertFalse(validerEmail(null));
    }

    @Test
    @DisplayName("Email vide invalide")
    void testEmailVide() {
        assertFalse(validerEmail(""));
    }

    // ===== VALIDATION TÉLÉPHONE =====

    private boolean validerTelephone(String telephone) {
        return telephone != null && telephone.matches("^0[0-9]{9}$");
    }

    @Test
    @DisplayName("Téléphone valide 10 chiffres commençant par 0")
    void testTelephoneValide() {
        assertTrue(validerTelephone("0612345678"));
    }

    @Test
    @DisplayName("Téléphone fixe valide")
    void testTelephoneFixeValide() {
        assertTrue(validerTelephone("0123456789"));
    }

    @Test
    @DisplayName("Téléphone ne commençant pas par 0 invalide")
    void testTelephoneSans0() {
        assertFalse(validerTelephone("1612345678"));
    }

    @Test
    @DisplayName("Téléphone trop court invalide")
    void testTelephoneTropCourt() {
        assertFalse(validerTelephone("061234567"));
    }

    @Test
    @DisplayName("Téléphone trop long invalide")
    void testTelephoneTropLong() {
        assertFalse(validerTelephone("06123456789"));
    }

    @Test
    @DisplayName("Téléphone avec lettres invalide")
    void testTelephoneAvecLettres() {
        assertFalse(validerTelephone("061234567A"));
    }

    @Test
    @DisplayName("Téléphone null invalide")
    void testTelephoneNull() {
        assertFalse(validerTelephone(null));
    }
}