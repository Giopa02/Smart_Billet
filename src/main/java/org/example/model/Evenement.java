package org.example.model;

public class Evenement {

    private int id;
    private String titre;
    private String affiche;
    private String descriptionCourte;
    private String descriptionLongue;
    private int duree;
    private int ageMin;
    private String langue;
    private double prixBase;

    // Constructeur vide
    public Evenement() {}

    // Constructeur complet
    public Evenement(int id, String titre, String affiche, String descriptionCourte,
                     String descriptionLongue, int duree, int ageMin, String langue, double prixBase) {
        this.id = id;
        this.titre = titre;
        this.affiche = affiche;
        this.descriptionCourte = descriptionCourte;
        this.descriptionLongue = descriptionLongue;
        this.duree = duree;
        this.ageMin = ageMin;
        this.langue = langue;
        this.prixBase = prixBase;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getAffiche() { return affiche; }
    public void setAffiche(String affiche) { this.affiche = affiche; }

    public String getDescriptionCourte() { return descriptionCourte; }
    public void setDescriptionCourte(String descriptionCourte) { this.descriptionCourte = descriptionCourte; }

    public String getDescriptionLongue() { return descriptionLongue; }
    public void setDescriptionLongue(String descriptionLongue) { this.descriptionLongue = descriptionLongue; }

    public int getDuree() { return duree; }
    public void setDuree(int duree) { this.duree = duree; }

    public int getAgeMin() { return ageMin; }
    public void setAgeMin(int ageMin) { this.ageMin = ageMin; }

    public String getLangue() { return langue; }
    public void setLangue(String langue) { this.langue = langue; }

    public double getPrixBase() { return prixBase; }
    public void setPrixBase(double prixBase) { this.prixBase = prixBase; }

    @Override
    public String toString() {
        return titre;
    }
}