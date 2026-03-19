package org.example.model;

public class Administrateur {

    private int id;
    private String email;
    private String motDePasse;
    private String nom;

    public Administrateur() {}

    public Administrateur(int id, String email, String motDePasse, String nom) {
        this.id = id;
        this.email = email;
        this.motDePasse = motDePasse;
        this.nom = nom;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMotDePasse() { return motDePasse; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    @Override
    public String toString() { return nom + " (" + email + ")"; }
}