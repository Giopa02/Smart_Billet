package org.example.dao;

import org.example.database.DatabaseConnection;
import org.example.model.Client;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO {

    public List<Client> getAll() {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM Client";

        try (Statement stmt = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Client c = new Client(
                        rs.getInt("id_client"),
                        rs.getString("nom"),
                        rs.getString("email"),
                        rs.getString("mot_de_passe"),
                        rs.getString("telephone"),
                        rs.getString("adresse"),
                        rs.getDate("dateNaissance") != null ? rs.getDate("dateNaissance").toLocalDate() : null
                );
                clients.add(c);
            }

        } catch (SQLException e) {
            System.err.println("Erreur getAll Client : " + e.getMessage());
        }

        return clients;
    }

    public Client getById(int id) {
        String sql = "SELECT * FROM Client WHERE id_client = ?";

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Client(
                        rs.getInt("id_client"),
                        rs.getString("nom"),
                        rs.getString("email"),
                        rs.getString("mot_de_passe"),
                        rs.getString("telephone"),
                        rs.getString("adresse"),
                        rs.getDate("dateNaissance") != null ? rs.getDate("dateNaissance").toLocalDate() : null
                );
            }

        } catch (SQLException e) {
            System.err.println("Erreur getById Client : " + e.getMessage());
        }

        return null;
    }

    public Client getByEmail(String email) {
        String sql = "SELECT * FROM Client WHERE email = ?";

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Client(
                        rs.getInt("id_client"),
                        rs.getString("nom"),
                        rs.getString("email"),
                        rs.getString("mot_de_passe"),
                        rs.getString("telephone"),
                        rs.getString("adresse"),
                        rs.getDate("dateNaissance") != null ? rs.getDate("dateNaissance").toLocalDate() : null
                );
            }

        } catch (SQLException e) {
            System.err.println("Erreur getByEmail Client : " + e.getMessage());
        }

        return null;
    }

    public boolean insert(Client c) {
        String sql = "INSERT INTO Client (nom, email, mot_de_passe, telephone, adresse, dateNaissance) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setString(1, c.getNom());
            stmt.setString(2, c.getEmail());
            stmt.setString(3, c.getMotDePasse() != null ? c.getMotDePasse() : "");
            stmt.setString(4, c.getTelephone());
            stmt.setString(5, c.getAdresse());
            stmt.setDate(6, c.getDateNaissance() != null ? Date.valueOf(c.getDateNaissance()) : null);

            System.out.println("INSERT client : " + c.getNom() + " / " + c.getEmail() + " / mdp: " + c.getMotDePasse());
            int rows = stmt.executeUpdate();
            System.out.println("Lignes insérées : " + rows);
            return rows > 0;

        } catch (SQLException e) {
            System.err.println("Erreur insert Client : " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    public boolean update(Client c) {
        String sql = "UPDATE Client SET nom=?, email=?, mot_de_passe=?, telephone=?, adresse=?, dateNaissance=? WHERE id_client=?";

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setString(1, c.getNom());
            stmt.setString(2, c.getEmail());
            stmt.setString(3, c.getMotDePasse() != null ? c.getMotDePasse() : "");
            stmt.setString(4, c.getTelephone());
            stmt.setString(5, c.getAdresse());
            stmt.setDate(6, c.getDateNaissance() != null ? Date.valueOf(c.getDateNaissance()) : null);
            stmt.setInt(7, c.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erreur update Client : " + e.getMessage());
        }

        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM Client WHERE id_client = ?";

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erreur delete Client : " + e.getMessage());
        }

        return false;
    }
}