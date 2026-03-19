package org.example.model;

import java.time.LocalDateTime;

public class Billet {

    private int id;
    private String numeroUnique;
    private int idClient;
    private int idSeance;
    private LocalDateTime dateAchat;
    private double prix;
    private String statut;

    public Billet() {}

    public Billet(int id, String numeroUnique, int idClient, int idSeance, LocalDateTime dateAchat, double prix, String statut) {
        this.id = id;
        this.numeroUnique = numeroUnique;
        this.idClient = idClient;
        this.idSeance = idSeance;
        this.dateAchat = dateAchat;
        this.prix = prix;
        this.statut = statut;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNumeroUnique() { return numeroUnique; }
    public void setNumeroUnique(String numeroUnique) { this.numeroUnique = numeroUnique; }

    public int getIdClient() { return idClient; }
    public void setIdClient(int idClient) { this.idClient = idClient; }

    public int getIdSeance() { return idSeance; }
    public void setIdSeance(int idSeance) { this.idSeance = idSeance; }

    public LocalDateTime getDateAchat() { return dateAchat; }
    public void setDateAchat(LocalDateTime dateAchat) { this.dateAchat = dateAchat; }

    public double getPrix() { return prix; }
    public void setPrix(double prix) { this.prix = prix; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    @Override
    public String toString() { return numeroUnique; }
}