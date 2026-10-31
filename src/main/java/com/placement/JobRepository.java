package com.placement;

import java.sql.*;
import java.util.Set;

public class JobRepository {

    public void findAllJobs() {
        String sql = """
                SELECT j.job_id, c.company_name,
                       j.job_title, j.minimum_cgpa
                FROM jobs j
                JOIN companies c
                ON j.company_id = c.company_id
                """;

        try (
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                System.out.println(
                    rs.getLong("job_id") + " | " +
                    rs.getString("company_name") + " | " +
                    rs.getString("job_title") + " | " +
                    rs.getDouble("minimum_cgpa")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addJob(long companyId, String title,
                       double minimumCgpa, Set<Long> departmentIds) {

        String sql = """
                INSERT INTO jobs
                (company_id, job_title, minimum_cgpa)
                VALUES (?, ?, ?)
                """;

        try (Connection con = DatabaseConnection.getConnection()) {

            con.setAutoCommit(false);

            try (
                PreparedStatement ps = con.prepareStatement(
                    sql, Statement.RETURN_GENERATED_KEYS
                )
            ) {
                ps.setLong(1, companyId);
                ps.setString(2, title);
                ps.setDouble(3, minimumCgpa);
                ps.executeUpdate();

                ResultSet keys = ps.getGeneratedKeys();
                keys.next();
                long jobId = keys.getLong(1);

                String mappingSql = """
                        INSERT INTO job_departments
                        (job_id, department_id)
                        VALUES (?, ?)
                        """;

                try (PreparedStatement mapping =
                         con.prepareStatement(mappingSql)) {

                    for (long departmentId : departmentIds) {
                        mapping.setLong(1, jobId);
                        mapping.setLong(2, departmentId);
                        mapping.addBatch();
                    }

                    mapping.executeBatch();
                }

                con.commit();
                System.out.println("Job added successfully.");

            } catch (SQLException e) {
                con.rollback();
                throw e;
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    public void updateJob(long jobId, long companyId,
                      String title, double minimumCgpa,
                      Set<Long> departmentIds) {

    String sql = """
            UPDATE jobs
            SET company_id = ?, job_title = ?, minimum_cgpa = ?
            WHERE job_id = ?
            """;

    try (Connection con = DatabaseConnection.getConnection()) {

        con.setAutoCommit(false);

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, companyId);
            ps.setString(2, title);
            ps.setDouble(3, minimumCgpa);
            ps.setLong(4, jobId);

            int rows = ps.executeUpdate();

            if (rows == 0) {
                con.rollback();
                System.out.println("Job not found.");
                return;
            }

            try (PreparedStatement delete = con.prepareStatement(
                    "DELETE FROM job_departments WHERE job_id = ?")) {
                delete.setLong(1, jobId);
                delete.executeUpdate();
            }

            String insertSql = """
                    INSERT INTO job_departments
                    (job_id, department_id)
                    VALUES (?, ?)
                    """;

            try (PreparedStatement insert = con.prepareStatement(insertSql)) {
                for (long departmentId : departmentIds) {
                    insert.setLong(1, jobId);
                    insert.setLong(2, departmentId);
                    insert.addBatch();
                }
                insert.executeBatch();
            }

            con.commit();
            System.out.println("Job updated successfully.");

        } catch (SQLException e) {
            con.rollback();
            throw e;
        }

    } catch (SQLException e) {
        System.out.println("Error: " + e.getMessage());
    }
}
}