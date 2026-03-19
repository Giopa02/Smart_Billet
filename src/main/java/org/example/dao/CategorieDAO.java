package org.example.dao;

import org.example.database.DatabaseConnection;
import org.example.model.Categorie;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategorieDAO {

    public List<Categorie> getAll() {
        List<Categorie> categories = new ArrayList<>();
        String sql = "SELECT * FROM Categorie";

        try (Statement stmt = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                categories.add(new Categorie(
                        rs.getInt("id_categorie"),
                        rs.getString("nom")
                ));
            }

        } catch (SQLException e) {
            System.err.println("Erreur getAll Categorie : " + e.getMessage());
        }

        return categories;
    }

    public boolean insert(Categorie c) {
        String sql = "INSERT INTO Categorie (nom) VALUES (?)";

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setString(1, c.getNom());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erreur insert Categorie : " + e.getMessage());
        }

        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM Categorie WHERE id_categorie = ?";

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erreur delete Categorie : " + e.getMessage());
        }

        return false;
    }

    public List<Categorie> getByEvenement(int idEvenement) {
        List<Categorie> categories = new ArrayList<>();
        String sql = "SELECT c.* FROM Categorie c JOIN Evenement_Categorie ec ON c.id_categorie = ec.id_categorie WHERE ec.id_evenement = ?";

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, idEvenement);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                categories.add(new Categorie(
                        rs.getInt("id_categorie"),
                        rs.getString("nom")
                ));
            }

        } catch (SQLException e) {
            System.err.println("Erreur getByEvenement Categorie : " + e.getMessage());
        }

        return categories;
    }

    public List<Integer> getIdsByEvenement(int idEvenement) {
        List<Integer> ids = new ArrayList<>();
        String sql = "SELECT id_categorie FROM Evenement_Categorie WHERE id_evenement = ?";

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, idEvenement);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ids.add(rs.getInt("id_categorie"));
            }
        } catch (SQLException e) {
            System.err.println("Erreur getIdsByEvenement : " + e.getMessage());
        }

        return ids;
    }

    public void deleteByEvenement(int idEvenement) {
        String sql = "DELETE FROM Evenement_Categorie WHERE id_evenement = ?";
        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, idEvenement);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur deleteByEvenement : " + e.getMessage());
        }
    }

    public void insertEvenementCategorie(int idEvenement, int idCategorie) {
        String sql = "INSERT INTO Evenement_Categorie (id_evenement, id_categorie) VALUES (?, ?)";
        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, idEvenement);
            stmt.setInt(2, idCategorie);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur insertEvenementCategorie : " + e.getMessage());
        }
    }
}