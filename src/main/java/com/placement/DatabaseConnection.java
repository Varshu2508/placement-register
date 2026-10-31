package com.placement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    public static Connection getConnection() throws SQLException {
        String url = System.getenv("DB_URL");
        String username = System.getenv("DB_USERNAME");
        String password = System.getenv("DB_PASSWORD");

        if (url == null || username == null || password == null) {
            throw new SQLException(
                "Database settings missing. Check DB_URL, DB_USERNAME, DB_PASSWORD."
            );
        }

        return DriverManager.getConnection(url, username, password);
    }
}