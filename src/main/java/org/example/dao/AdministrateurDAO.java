package org.example.dao;

import org.example.database.DatabaseConnection;
import org.example.model.Administrateur;

import java.sql.*;

public class AdministrateurDAO {

    public Administrateur getByEmail(String email) {
        String sql = "SELECT * FROM Administrateur WHERE email = ?";

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Administrateur(
                        rs.getInt("id_admin"),
                        rs.getString("email"),
                        rs.getString("mot_de_passe"),
                        rs.getString("nom")
                );
            }

        } catch (SQLException e) {
            System.err.println("Erreur getByEmail Administrateur : " + e.getMessage());
        }

        return null;
    }

    public boolean insert(Administrateur a) {
        String sql = "INSERT INTO Administrateur (email, mot_de_passe, nom) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setString(1, a.getEmail());
            stmt.setString(2, a.getMotDePasse());
            stmt.setString(3, a.getNom());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erreur insert Administrateur : " + e.getMessage());
        }

        return false;
    }

    public boolean update(Administrateur a) {
        String sql = "UPDATE Administrateur SET email=?, mot_de_passe=?, nom=? WHERE id_admin=?";

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setString(1, a.getEmail());
            stmt.setString(2, a.getMotDePasse());
            stmt.setString(3, a.getNom());
            stmt.setInt(4, a.getId());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erreur update Administrateur : " + e.getMessage());
        }

        return false;
    }
}