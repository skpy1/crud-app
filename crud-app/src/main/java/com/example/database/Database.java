package com.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private static final String URL = "jdbc:sqlite:crud-app.db";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    static {
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS entities (" +
                    "id TEXT PRIMARY KEY," +
                    "name TEXT NOT NULL CHECK(length(name) BETWEEN 3 AND 50)," +
                    "description TEXT CHECK(length(description) <= 255)," +
                    "created_at DATETIME NOT NULL," +
                    "updated_at DATETIME NOT NULL)";
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }
}