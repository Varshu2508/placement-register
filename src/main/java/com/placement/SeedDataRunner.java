package com.placement;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Statement;

public class SeedDataRunner {

    public static void main(String[] args) {

        try (
            Connection con = DatabaseConnection.getConnection();
            InputStream input = SeedDataRunner.class
                    .getClassLoader().getResourceAsStream("seed.sql");
        ) {
            if (input == null) {
                throw new RuntimeException("seed.sql file not found");
            }

            String sql = new String(
                    input.readAllBytes(),
                    StandardCharsets.UTF_8
            );

            try (Statement stmt = con.createStatement()) {
                for (String query : sql.split(";")) {
                    if (!query.isBlank()) {
                        stmt.executeUpdate(query);
                    }
                }
            }

            System.out.println("Seed data inserted successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}