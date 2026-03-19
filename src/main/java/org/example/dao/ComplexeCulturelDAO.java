package org.example.dao;

import org.example.database.DatabaseConnection;
import org.example.model.ComplexeCulturel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ComplexeCulturelDAO {

    public List<ComplexeCulturel> getAll() {
        List<ComplexeCulturel> complexes = new ArrayList<>();
        String sql = "SELECT * FROM ComplexeCulturel";

        try (Statement stmt = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                complexes.add(new ComplexeCulturel(
                        rs.getInt("id_complexe"),
                        rs.getString("nom"),
                        rs.getString("adresse")
                ));
            }

        } catch (SQLException e) {
            System.err.println("Erreur getAll ComplexeCulturel : " + e.getMessage());
        }

        return complexes;
    }

    public ComplexeCulturel getById(int id) {
        String sql = "SELECT * FROM ComplexeCulturel WHERE id_complexe = ?";

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new ComplexeCulturel(
                        rs.getInt("id_complexe"),
                        rs.getString("nom"),
                        rs.getString("adresse")
                );
            }

        } catch (SQLException e) {
            System.err.println("Erreur getById ComplexeCulturel : " + e.getMessage());
        }

        return null;
    }

    public boolean insert(ComplexeCulturel c) {
        String sql = "INSERT INTO ComplexeCulturel (nom, adresse) VALUES (?, ?)";

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setString(1, c.getNom());
            stmt.setString(2, c.getAdresse());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erreur insert ComplexeCulturel : " + e.getMessage());
        }

        return false;
    }

    public boolean update(ComplexeCulturel c) {
        String sql = "UPDATE ComplexeCulturel SET nom=?, adresse=? WHERE id_complexe=?";

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setString(1, c.getNom());
            stmt.setString(2, c.getAdresse());
            stmt.setInt(3, c.getId());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erreur update ComplexeCulturel : " + e.getMessage());
        }

        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM ComplexeCulturel WHERE id_complexe = ?";

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erreur delete ComplexeCulturel : " + e.getMessage());
        }

        return false;
    }

    public String getNomById(int id) {
        ComplexeCulturel complexe = getById(id);
        return complexe != null ? complexe.getNom() : null;
    }
}