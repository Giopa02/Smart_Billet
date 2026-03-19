package org.example.model;

public class ComplexeCulturel {

    private int id;
    private String nom;
    private String adresse;

    public ComplexeCulturel() {}

    public ComplexeCulturel(int id, String nom, String adresse) {
        this.id = id;
        this.nom = nom;
        this.adresse = adresse;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    @Override
    public String toString() { return nom; }
}