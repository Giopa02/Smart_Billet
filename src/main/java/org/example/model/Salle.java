package org.example.model;

public class Salle {

    private int id;
    private String nom;
    private int capacite;
    private int idComplexe;

    public Salle() {}

    public Salle(int id, String nom, int capacite, int idComplexe) {
        this.id = id;
        this.nom = nom;
        this.capacite = capacite;
        this.idComplexe = idComplexe;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public int getCapacite() { return capacite; }
    public void setCapacite(int capacite) { this.capacite = capacite; }

    public int getIdComplexe() { return idComplexe; }
    public void setIdComplexe(int idComplexe) { this.idComplexe = idComplexe; }

    @Override
    public String toString() { return nom; }
}