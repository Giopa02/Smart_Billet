package org.example.database;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {

    private static Connection connection = null;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Properties props = new Properties();
                props.load(new FileInputStream("config.properties"));

                String url = props.getProperty("db.url");
                String user = props.getProperty("db.user");
                String password = props.getProperty("db.password");

                connection = DriverManager.getConnection(url, user, password);
            } catch (IOException e) {
                throw new SQLException("Impossible de lire config.properties : " + e.getMessage());
            }
        }
        return connection;
    }
}