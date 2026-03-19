package org.example.dao;

import org.example.database.DatabaseConnection;
import org.example.model.Seance;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.example.model.SeanceAffichage;

public class SeanceDAO {

    public List<Seance> getAll() {
        List<Seance> seances = new ArrayList<>();
        String sql = "SELECT * FROM Seance";

        try (Statement stmt = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                seances.add(new Seance(
                        rs.getInt("id_seance"),
                        rs.getInt("id_evenement"),
                        rs.getInt("id_salle"),
                        rs.getTimestamp("dateHeure").toLocalDateTime(),
                        rs.getDouble("prix")
                ));
            }

        } catch (SQLException e) {
            System.err.println("Erreur getAll Seance : " + e.getMessage());
        }

        return seances;
    }

    public List<Seance> getByEvenement(int idEvenement) {
        List<Seance> seances = new ArrayList<>();
        String sql = "SELECT * FROM Seance WHERE id_evenement = ?";

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, idEvenement);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                seances.add(new Seance(
                        rs.getInt("id_seance"),
                        rs.getInt("id_evenement"),
                        rs.getInt("id_salle"),
                        rs.getTimestamp("dateHeure").toLocalDateTime(),
                        rs.getDouble("prix")
                ));
            }

        } catch (SQLException e) {
            System.err.println("Erreur getByEvenement Seance : " + e.getMessage());
        }

        return seances;
    }

    public boolean insert(Seance s) {
        String sql = "INSERT INTO Seance (id_evenement, id_salle, dateHeure, prix) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, s.getIdEvenement());
            stmt.setInt(2, s.getIdSalle());
            stmt.setTimestamp(3, Timestamp.valueOf(s.getDateHeure()));
            stmt.setDouble(4, s.getPrix());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erreur insert Seance : " + e.getMessage());
        }

        return false;
    }

    public boolean update(Seance s) {
        String sql = "UPDATE Seance SET id_evenement=?, id_salle=?, dateHeure=?, prix=? WHERE id_seance=?";

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, s.getIdEvenement());
            stmt.setInt(2, s.getIdSalle());
            stmt.setTimestamp(3, Timestamp.valueOf(s.getDateHeure()));
            stmt.setDouble(4, s.getPrix());
            stmt.setInt(5, s.getId());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erreur update Seance : " + e.getMessage());
        }

        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM Seance WHERE id_seance = ?";

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erreur delete Seance : " + e.getMessage());
        }

        return false;
    }

    public List<SeanceAffichage> getAllAvecDetails() {
        List<SeanceAffichage> seances = new ArrayList<>();
        String sql = "SELECT s.id_seance, e.titre, sa.nom AS nomSalle, " +
                "s.dateHeure, s.prix, s.id_evenement, s.id_salle, sa.capacite " +
                "FROM Seance s " +
                "JOIN Evenement e ON s.id_evenement = e.id_evenement " +
                "JOIN Salle sa ON s.id_salle = sa.id_salle " +
                "ORDER BY s.dateHeure";

        try (Statement stmt = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                seances.add(new SeanceAffichage(
                        rs.getInt("id_seance"),
                        rs.getString("titre"),
                        rs.getString("nomSalle"),
                        rs.getTimestamp("dateHeure").toLocalDateTime(),
                        rs.getDouble("prix"),
                        rs.getInt("id_evenement"),
                        rs.getInt("id_salle"),
                        rs.getInt("capacite")
                ));
            }

        } catch (SQLException e) {
            System.err.println("Erreur getAllAvecDetails Seance : " + e.getMessage());
        }

        return seances;
    }

    public int getPlacesRestantes(int idSeance) {
        String sql = "SELECT s.id_seance, sa.capacite, " +
                "COUNT(b.id_billet) AS billets_vendus " +
                "FROM Seance s " +
                "JOIN Salle sa ON s.id_salle = sa.id_salle " +
                "LEFT JOIN Billet b ON b.id_seance = s.id_seance " +
                "AND b.statut = 'valide' " +
                "WHERE s.id_seance = ? " +
                "GROUP BY s.id_seance, sa.capacite";

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, idSeance);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int capacite = rs.getInt("capacite");
                int billetsVendus = rs.getInt("billets_vendus");
                return capacite - billetsVendus;
            }
        } catch (SQLException e) {
            System.err.println("Erreur getPlacesRestantes : " + e.getMessage());
        }
        return 0;
    }
}