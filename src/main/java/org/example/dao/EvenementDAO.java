package org.example.dao;

import org.example.database.DatabaseConnection;
import org.example.model.Evenement;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EvenementDAO {

    // Récupérer tous les événements
    public List<Evenement> getAll() {
        List<Evenement> evenements = new ArrayList<>();
        String sql = "SELECT * FROM Evenement";

        try (Statement stmt = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Evenement e = new Evenement(
                        rs.getInt("id_evenement"),
                        rs.getString("titre"),
                        rs.getString("affiche"),
                        rs.getString("description_courte"),
                        rs.getString("description_longue"),
                        rs.getInt("duree"),
                        rs.getInt("age_min"),
                        rs.getString("langue"),
                        rs.getDouble("prix_base")
                );
                evenements.add(e);
            }

        } catch (SQLException e) {
            System.err.println("Erreur getAll Evenement : " + e.getMessage());
        }

        return evenements;
    }

    // Récupérer un événement par son id
    public Evenement getById(int id) {
        String sql = "SELECT * FROM Evenement WHERE id_evenement = ?";

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Evenement(
                        rs.getInt("id_evenement"),
                        rs.getString("titre"),
                        rs.getString("affiche"),
                        rs.getString("description_courte"),
                        rs.getString("description_longue"),
                        rs.getInt("duree"),
                        rs.getInt("age_min"),
                        rs.getString("langue"),
                        rs.getDouble("prix_base")
                );
            }

        } catch (SQLException e) {
            System.err.println("Erreur getById Evenement : " + e.getMessage());
        }

        return null;
    }

    // Ajouter un événement
    public boolean insert(Evenement e) {
        String sql = "INSERT INTO Evenement (titre, affiche, description_courte, description_longue, duree, age_min, langue, prix_base) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setString(1, e.getTitre());
            stmt.setString(2, e.getAffiche());
            stmt.setString(3, e.getDescriptionCourte());
            stmt.setString(4, e.getDescriptionLongue());
            stmt.setInt(5, e.getDuree());
            stmt.setInt(6, e.getAgeMin());
            stmt.setString(7, e.getLangue());
            stmt.setDouble(8, e.getPrixBase());

            return stmt.executeUpdate() > 0;

        } catch (SQLException ex) {
            System.err.println("Erreur insert Evenement : " + ex.getMessage());
        }

        return false;
    }

    // Modifier un événement
    public boolean update(Evenement e) {
        String sql = "UPDATE Evenement SET titre=?, affiche=?, description_courte=?, description_longue=?, duree=?, age_min=?, langue=?, prix_base=? WHERE id_evenement=?";

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setString(1, e.getTitre());
            stmt.setString(2, e.getAffiche());
            stmt.setString(3, e.getDescriptionCourte());
            stmt.setString(4, e.getDescriptionLongue());
            stmt.setInt(5, e.getDuree());
            stmt.setInt(6, e.getAgeMin());
            stmt.setString(7, e.getLangue());
            stmt.setDouble(8, e.getPrixBase());
            stmt.setInt(9, e.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException ex) {
            System.err.println("Erreur update Evenement : " + ex.getMessage());
        }

        return false;
    }

    // Supprimer un événement
    public boolean delete(int id) {
        String sql = "DELETE FROM Evenement WHERE id_evenement = ?";

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erreur delete Evenement : " + e.getMessage());
        }

        return false;
    }
}