
package com.placement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentRepository {

    public void findAllStudents() {

        String sql = """
                SELECT s.student_id, s.name, s.email,
                       d.department_name, s.cgpa
                FROM students s
                JOIN departments d
                ON s.department_id = d.department_id
                """;

        try (
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                System.out.println(
                    rs.getLong("student_id") + " | " +
                    rs.getString("name") + " | " +
                    rs.getString("email") + " | " +
                    rs.getString("department_name") + " | " +
                    rs.getDouble("cgpa")
                );
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void addStudent(String name, String email,
                           String department, double cgpa) {

        String sql = """
                INSERT INTO students
                (name, email, department_id, cgpa)
                SELECT ?, ?, department_id, ?
                FROM departments
                WHERE LOWER(TRIM(department_name)) =
                      LOWER(TRIM(?))
                """;

        try (
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setDouble(3, cgpa);
            ps.setString(4, department);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Student added successfully.");
            } else {
                System.out.println("Department not found.");
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void updateStudent(long id, String name,
                              String email, String department,
                              double cgpa) {

        String sql = """
                UPDATE students
                SET name = ?,
                    email = ?,
                    department_id = (
                        SELECT department_id
                        FROM departments
                        WHERE LOWER(TRIM(department_name)) =
                              LOWER(TRIM(?))
                    ),
                    cgpa = ?
                WHERE student_id = ?
                AND EXISTS (
                    SELECT 1
                    FROM departments
                    WHERE LOWER(TRIM(department_name)) =
                          LOWER(TRIM(?))
                )
                """;

        try (
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, department);
            ps.setDouble(4, cgpa);
            ps.setLong(5, id);
            ps.setString(6, department);

            int rows = ps.executeUpdate();

            System.out.println(rows > 0
                    ? "Student updated successfully."
                    : "Student or department not found.");

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
