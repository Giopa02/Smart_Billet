package org.example.dao;

import org.example.database.DatabaseConnection;
import org.example.model.Billet;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.example.model.BilletAffichage;

public class BilletDAO {

    public List<Billet> getAll() {
        List<Billet> billets = new ArrayList<>();
        String sql = "SELECT * FROM Billet";

        try (Statement stmt = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                billets.add(new Billet(
                        rs.getInt("id_billet"),
                        rs.getString("numero_unique"),
                        rs.getInt("id_client"),
                        rs.getInt("id_seance"),
                        rs.getTimestamp("dateAchat").toLocalDateTime(),
                        rs.getDouble("prix"),
                        rs.getString("statut")
                ));
            }

        } catch (SQLException e) {
            System.err.println("Erreur getAll Billet : " + e.getMessage());
        }

        return billets;
    }

    public List<Billet> getByClient(int idClient) {
        List<Billet> billets = new ArrayList<>();
        String sql = "SELECT * FROM Billet WHERE id_client = ?";

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, idClient);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                billets.add(new Billet(
                        rs.getInt("id_billet"),
                        rs.getString("numero_unique"),
                        rs.getInt("id_client"),
                        rs.getInt("id_seance"),
                        rs.getTimestamp("dateAchat").toLocalDateTime(),
                        rs.getDouble("prix"),
                        rs.getString("statut")
                ));
            }

        } catch (SQLException e) {
            System.err.println("Erreur getByClient Billet : " + e.getMessage());
        }

        return billets;
    }

    public boolean insert(Billet b) {
        String sql = "INSERT INTO Billet (numero_unique, id_client, id_seance, dateAchat, prix, statut) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setString(1, b.getNumeroUnique());
            stmt.setInt(2, b.getIdClient());
            stmt.setInt(3, b.getIdSeance());
            stmt.setTimestamp(4, Timestamp.valueOf(b.getDateAchat()));
            stmt.setDouble(5, b.getPrix());
            stmt.setString(6, b.getStatut());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erreur insert Billet : " + e.getMessage());
        }

        return false;
    }

    public boolean updateStatut(int id, String statut) {
        String sql = "UPDATE Billet SET statut=? WHERE id_billet=?";

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setString(1, statut);
            stmt.setInt(2, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erreur updateStatut Billet : " + e.getMessage());
        }

        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM Billet WHERE id_billet = ?";

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erreur delete Billet : " + e.getMessage());
        }

        return false;
    }

    public List<BilletAffichage> getAllAvecDetails() {
        List<BilletAffichage> billets = new ArrayList<>();
        String sql = "SELECT b.id_billet, b.numero_unique, c.nom AS nomClient, " +
                "e.titre AS nomEvenement, s.dateHeure, b.prix, b.statut, " +
                "b.dateAchat, b.id_client, b.id_seance " +
                "FROM Billet b " +
                "JOIN Client c ON b.id_client = c.id_client " +
                "JOIN Seance s ON b.id_seance = s.id_seance " +
                "JOIN Evenement e ON s.id_evenement = e.id_evenement " +
                "ORDER BY b.dateAchat DESC";

        try (Statement stmt = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                billets.add(new BilletAffichage(
                        rs.getInt("id_billet"),
                        rs.getString("numero_unique"),
                        rs.getString("nomClient"),
                        rs.getString("nomEvenement"),
                        rs.getTimestamp("dateHeure").toLocalDateTime(),
                        rs.getDouble("prix"),
                        rs.getString("statut"),
                        rs.getTimestamp("dateAchat").toLocalDateTime(),
                        rs.getInt("id_client"),
                        rs.getInt("id_seance")
                ));
            }

        } catch (SQLException e) {
            System.err.println("Erreur getAllAvecDetails Billet : " + e.getMessage());
        }

        return billets;
    }

    public List<BilletAffichage> getByClientAvecDetails(int idClient) {
        List<BilletAffichage> billets = new ArrayList<>();
        String sql = "SELECT b.id_billet, b.numero_unique, c.nom AS nomClient, " +
                "e.titre AS nomEvenement, s.dateHeure, b.prix, b.statut, " +
                "b.dateAchat, b.id_client, b.id_seance " +
                "FROM Billet b " +
                "JOIN Client c ON b.id_client = c.id_client " +
                "JOIN Seance s ON b.id_seance = s.id_seance " +
                "JOIN Evenement e ON s.id_evenement = e.id_evenement " +
                "WHERE b.id_client = ? " +
                "ORDER BY b.dateAchat DESC";

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, idClient);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                billets.add(new BilletAffichage(
                        rs.getInt("id_billet"),
                        rs.getString("numero_unique"),
                        rs.getString("nomClient"),
                        rs.getString("nomEvenement"),
                        rs.getTimestamp("dateHeure").toLocalDateTime(),
                        rs.getDouble("prix"),
                        rs.getString("statut"),
                        rs.getTimestamp("dateAchat").toLocalDateTime(),
                        rs.getInt("id_client"),
                        rs.getInt("id_seance")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Erreur getByClientAvecDetails : " + e.getMessage());
        }

        return billets;
    }

    public boolean updateComplet(int id, int idClient, int idSeance, double prix, String statut) {
        String sql = "UPDATE Billet SET id_client = ?, id_seance = ?, prix = ?, statut = ? WHERE id_billet = ?";
        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, idClient);
            stmt.setInt(2, idSeance);
            stmt.setDouble(3, prix);
            stmt.setString(4, statut);
            stmt.setInt(5, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur updateComplet Billet : " + e.getMessage());
            return false;
        }
    }


    public int getMaxId() {
        String sql = "SELECT MAX(id_billet) FROM Billet";
        try (Statement stmt = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("Erreur getMaxId Billet : " + e.getMessage());
        }
        return 0;
    }
}