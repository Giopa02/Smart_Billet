package org.example.model;

import java.time.LocalDateTime;

public class Seance {

    private int id;
    private int idEvenement;
    private int idSalle;
    private LocalDateTime dateHeure;
    private double prix;

    public Seance() {}

    public Seance(int id, int idEvenement, int idSalle, LocalDateTime dateHeure, double prix) {
        this.id = id;
        this.idEvenement = idEvenement;
        this.idSalle = idSalle;
        this.dateHeure = dateHeure;
        this.prix = prix;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdEvenement() { return idEvenement; }
    public void setIdEvenement(int idEvenement) { this.idEvenement = idEvenement; }

    public int getIdSalle() { return idSalle; }
    public void setIdSalle(int idSalle) { this.idSalle = idSalle; }

    public LocalDateTime getDateHeure() { return dateHeure; }
    public void setDateHeure(LocalDateTime dateHeure) { this.dateHeure = dateHeure; }

    public double getPrix() { return prix; }
    public void setPrix(double prix) { this.prix = prix; }

    @Override
    public String toString() { return dateHeure.toString(); }
}