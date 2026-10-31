package com.placement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ApplicationRepository {

    public void findAllApplications() {

        String sql = """
                SELECT a.application_id, s.name,
                       j.job_title, a.status
                FROM applications a
                JOIN students s
                ON a.student_id = s.student_id
                JOIN jobs j
                ON a.job_id = j.job_id
                """;

        try (
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                System.out.println(
                    rs.getLong("application_id") + " | " +
                    rs.getString("name") + " | " +
                    rs.getString("job_title") + " | " +
                    rs.getString("status")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void applyForJob(long studentId, long jobId) {

    String sql = """
            INSERT INTO applications (student_id, job_id)
            SELECT ?, ?
            WHERE EXISTS (
                SELECT 1
                FROM students s
                JOIN jobs j ON j.job_id = ?
                JOIN job_departments jd
                    ON jd.job_id = j.job_id
                   AND jd.department_id = s.department_id
                WHERE s.student_id = ?
                  AND s.cgpa >= j.minimum_cgpa
            )
            """;

    try (
        Connection con = DatabaseConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql)
    ) {
        ps.setLong(1, studentId);
        ps.setLong(2, jobId);
        ps.setLong(3, jobId);
        ps.setLong(4, studentId);

        int rows = ps.executeUpdate();

        System.out.println(rows > 0
                ? "Application submitted successfully."
                : "Student is not eligible or already applied.");

    } catch (SQLException e) {
        System.out.println("Error: " + e.getMessage());
    }
}
public void updateApplicationStatus(long applicationId, String status) {

    String sql = """
            UPDATE applications
            SET status = ?
            WHERE application_id = ?
            """;

    try (
        Connection con = DatabaseConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql)
    ) {
        ps.setString(1, status.toUpperCase());
        ps.setLong(2, applicationId);

        int rows = ps.executeUpdate();

        System.out.println(rows > 0
                ? "Application status updated successfully."
                : "Application not found.");

    } catch (SQLException e) {
        System.out.println("Error: " + e.getMessage());
    }
}
public void findApplicationsForJob(long jobId) {

    String sql = """
            SELECT a.application_id, s.student_id,
                   s.name, a.status
            FROM applications a
            JOIN students s
            ON a.student_id = s.student_id
            WHERE a.job_id = ?
            """;

    try (
        Connection con = DatabaseConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql)
    ) {
        ps.setLong(1, jobId);

        try (ResultSet rs = ps.executeQuery()) {
            boolean found = false;

            while (rs.next()) {
                found = true;
                System.out.println(
                    rs.getLong("application_id") + " | " +
                    rs.getLong("student_id") + " | " +
                    rs.getString("name") + " | " +
                    rs.getString("status")
                );
            }

            if (!found) {
                System.out.println("No applications found.");
            }
        }

    } catch (SQLException e) {
        System.out.println("Error: " + e.getMessage());
    }
}
}