package org.example.model;

import java.time.LocalDate;

public class Client {

    private int id;
    private String nom;
    private String email;
    private String motDePasse;
    private String telephone;
    private String adresse;
    private LocalDate dateNaissance;

    public Client() {}

    // Constructeur sans mot de passe (pour l'admin)
    public Client(int id, String nom, String email, String telephone, String adresse, LocalDate dateNaissance) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.telephone = telephone;
        this.adresse = adresse;
        this.dateNaissance = dateNaissance;
    }

    // Constructeur avec mot de passe (pour l'inscription)
    public Client(int id, String nom, String email, String motDePasse, String telephone, String adresse, LocalDate dateNaissance) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.telephone = telephone;
        this.adresse = adresse;
        this.dateNaissance = dateNaissance;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMotDePasse() { return motDePasse; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    public LocalDate getDateNaissance() { return dateNaissance; }
    public void setDateNaissance(LocalDate dateNaissance) { this.dateNaissance = dateNaissance; }

    @Override
    public String toString() { return nom; }
}