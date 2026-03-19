package org.example.model;

import java.time.LocalDateTime;

public class SeanceAffichage {

    private int id;
    private String nomEvenement;
    private String nomSalle;
    private LocalDateTime dateHeure;
    private double prix;
    private int idEvenement;
    private int idSalle;
    private int capacite;

    public SeanceAffichage(int id, String nomEvenement, String nomSalle,
                           LocalDateTime dateHeure, double prix,
                           int idEvenement, int idSalle, int capacite) {
        this.id = id;
        this.nomEvenement = nomEvenement;
        this.nomSalle = nomSalle;
        this.dateHeure = dateHeure;
        this.prix = prix;
        this.idEvenement = idEvenement;
        this.idSalle = idSalle;
        this.capacite = capacite;
    }

    public int getId() { return id; }
    public String getNomEvenement() { return nomEvenement; }
    public String getNomSalle() { return nomSalle; }
    public LocalDateTime getDateHeure() { return dateHeure; }
    public double getPrix() { return prix; }
    public int getIdEvenement() { return idEvenement; }
    public int getIdSalle() { return idSalle; }
    public int getCapacite() { return capacite; }
    public void setCapacite(int capacite) { this.capacite = capacite; }
}