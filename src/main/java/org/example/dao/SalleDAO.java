package org.example.dao;

import org.example.database.DatabaseConnection;
import org.example.model.Salle;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SalleDAO {

    public List<Salle> getAll() {
        List<Salle> salles = new ArrayList<>();
        String sql = "SELECT * FROM Salle";

        try (Statement stmt = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                salles.add(new Salle(
                        rs.getInt("id_salle"),
                        rs.getString("nom"),
                        rs.getInt("capacite"),
                        rs.getInt("id_complexe")
                ));
            }

        } catch (SQLException e) {
            System.err.println("Erreur getAll Salle : " + e.getMessage());
        }

        return salles;
    }

    public Salle getById(int id) {
        String sql = "SELECT * FROM Salle WHERE id_salle = ?";

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Salle(
                        rs.getInt("id_salle"),
                        rs.getString("nom"),
                        rs.getInt("capacite"),
                        rs.getInt("id_complexe")
                );
            }

        } catch (SQLException e) {
            System.err.println("Erreur getById Salle : " + e.getMessage());
        }

        return null;
    }

    public List<Salle> getByComplexe(int idComplexe) {
        List<Salle> salles = new ArrayList<>();
        String sql = "SELECT * FROM Salle WHERE id_complexe = ?";

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, idComplexe);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                salles.add(new Salle(
                        rs.getInt("id_salle"),
                        rs.getString("nom"),
                        rs.getInt("capacite"),
                        rs.getInt("id_complexe")
                ));
            }

        } catch (SQLException e) {
            System.err.println("Erreur getByComplexe Salle : " + e.getMessage());
        }

        return salles;
    }

    public boolean insert(Salle s) {
        String sql = "INSERT INTO Salle (nom, capacite, id_complexe) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setString(1, s.getNom());
            stmt.setInt(2, s.getCapacite());
            stmt.setInt(3, s.getIdComplexe());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erreur insert Salle : " + e.getMessage());
        }

        return false;
    }

    public boolean update(Salle s) {
        String sql = "UPDATE Salle SET nom=?, capacite=?, id_complexe=? WHERE id_salle=?";

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setString(1, s.getNom());
            stmt.setInt(2, s.getCapacite());
            stmt.setInt(3, s.getIdComplexe());
            stmt.setInt(4, s.getId());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erreur update Salle : " + e.getMessage());
        }

        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM Salle WHERE id_salle = ?";

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erreur delete Salle : " + e.getMessage());
        }

        return false;
    }

    public boolean updateCapacite(int idSalle, int nouvelleCapacite) {
        String sql = "UPDATE Salle SET capacite = ? WHERE id_salle = ?";

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, nouvelleCapacite);
            stmt.setInt(2, idSalle);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erreur updateCapacite Salle : " + e.getMessage());
        }

        return false;
    }

}