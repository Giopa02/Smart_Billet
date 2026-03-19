package org.example.model;

import java.time.LocalDateTime;

public class BilletAffichage {

    private int id;
    private String numeroUnique;
    private String nomClient;
    private String nomEvenement;
    private LocalDateTime dateHeure;
    private double prix;
    private String statut;
    private LocalDateTime dateAchat;
    private int idClient;
    private int idSeance;

    public BilletAffichage(int id, String numeroUnique, String nomClient,
                           String nomEvenement, LocalDateTime dateHeure,
                           double prix, String statut, LocalDateTime dateAchat,
                           int idClient, int idSeance) {
        this.id = id;
        this.numeroUnique = numeroUnique;
        this.nomClient = nomClient;
        this.nomEvenement = nomEvenement;
        this.dateHeure = dateHeure;
        this.prix = prix;
        this.statut = statut;
        this.dateAchat = dateAchat;
        this.idClient = idClient;
        this.idSeance = idSeance;
    }

    public int getId() { return id; }
    public String getNumeroUnique() { return numeroUnique; }
    public String getNomClient() { return nomClient; }
    public String getNomEvenement() { return nomEvenement; }
    public LocalDateTime getDateHeure() { return dateHeure; }
    public double getPrix() { return prix; }
    public String getStatut() { return statut; }
    public LocalDateTime getDateAchat() { return dateAchat; }
    public int getIdClient() { return idClient; }
    public int getIdSeance() { return idSeance; }
}