package com.placement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CompanyRepository {

    public void findAllCompanies() {

        String sql = "SELECT company_id, company_name FROM companies";

        try (
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                System.out.println(
                    rs.getLong("company_id") + " | " +
                    rs.getString("company_name")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addCompany(String name) {

        String sql = """
                INSERT INTO companies (company_name)
                VALUES (?)
                """;

        try (
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, name);

            ps.executeUpdate();

            System.out.println("Company added successfully.");

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    public void updateCompany(long id, String name) {

    String sql = """
            UPDATE companies
            SET company_name = ?
            WHERE company_id = ?
            """;

    try (
        Connection con = DatabaseConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql)
    ) {
        ps.setString(1, name);
        ps.setLong(2, id);

        int rows = ps.executeUpdate();

        System.out.println(rows > 0
                ? "Company updated successfully."
                : "Company not found.");

    } catch (SQLException e) {
        System.out.println("Error: " + e.getMessage());
    }
}
}