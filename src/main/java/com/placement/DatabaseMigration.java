package com.placement;

import org.flywaydb.core.Flyway;

public class DatabaseMigration {

    public static void main(String[] args) {
        String url = System.getenv("DB_URL");
        String username = System.getenv("DB_USERNAME");
        String password = System.getenv("DB_PASSWORD");

        if (url == null || username == null || password == null) {
            System.out.println("Database environment variables are missing.");
            return;
        }

        Flyway flyway = Flyway.configure()
                .dataSource(url, username, password)
                .load();

        flyway.migrate();

        System.out.println("Database migration completed successfully!");
    }
}