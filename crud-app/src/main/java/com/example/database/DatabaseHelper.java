package com.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseHelper {
    private static final String URL = "jdbc:sqlite:crud-app.db";

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}
