
package com.placement;

import java.util.*;
import java.sql.*;

public class App {

    static Scanner sc = new Scanner(System.in);

    static StudentRepository studentRepo = new StudentRepository();
    static CompanyRepository companyRepo = new CompanyRepository();
    static JobRepository jobRepo = new JobRepository();
    static ApplicationRepository applicationRepo = new ApplicationRepository();

    public static void main(String[] args) {

        while (true) {

            System.out.println("\n=== COLLEGE PLACEMENT PORTAL ===");
            System.out.println("1. Add Student");
            System.out.println("2. List Students");
            System.out.println("3. Update Student");
            System.out.println("4. Add Company");
            System.out.println("5. List Companies");
            System.out.println("6. Update Company");
            System.out.println("7. Add Job");
            System.out.println("8. List Jobs");
            System.out.println("9. Update Job");
            System.out.println("10. Apply for Job");
            System.out.println("11. List Applications for a Job");
            System.out.println("12. Update Application Status");
            System.out.println("0. Exit");

            System.out.print("Choose: ");
            String choice = sc.nextLine().trim();

            try {
                switch (choice) {
                    case "1" -> addStudent();
                    case "2" -> studentRepo.findAllStudents();
                    case "3" -> updateStudent();
                    case "4" -> addCompany();
                    case "5" -> companyRepo.findAllCompanies();
                    case "6" -> updateCompany();
                    case "7" -> addJob();
                    case "8" -> jobRepo.findAllJobs();
                    case "9" -> updateJob();
                    case "10" -> applyForJob();
                    case "11" -> listApplicationsForJob();
                    case "12" -> updateApplicationStatus();

                    case "0" -> {
                        System.out.println("Goodbye!");
                        return;
                    }

                    default -> System.out.println("Invalid menu choice.");
                }

            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please try again.");
            }
        }
    }

    static String readRequired(String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }

    static double readCgpa(String prompt) {
        double cgpa = Double.parseDouble(readRequired(prompt));

        if (!Double.isFinite(cgpa) || cgpa < 0 || cgpa > 10) {
            throw new IllegalArgumentException("CGPA must be between 0 and 10.");
        }

        return cgpa;
    }

    static long readId(String prompt) {
        long id = Long.parseLong(readRequired(prompt));

        if (id <= 0) {
            throw new IllegalArgumentException("ID must be positive.");
        }

        return id;
    }

    static Set<Long> readDepartments() {

        String input = readRequired(
            "Allowed departments (comma-separated): "
        );

        Set<Long> departmentIds = new LinkedHashSet<>();

        String sql = """
                SELECT department_id
                FROM departments
                WHERE LOWER(department_name) = LOWER(?)
                """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            for (String department : input.split(",")) {

                ps.setString(1, department.trim());

                try (ResultSet rs = ps.executeQuery()) {

                    if (rs.next()) {
                        departmentIds.add(rs.getLong("department_id"));
                    } else {
                        throw new IllegalArgumentException(
                            "Department not found: " + department.trim()
                        );
                    }
                }
            }

        } catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage());
        }

        if (departmentIds.isEmpty()) {
            throw new IllegalArgumentException(
                "At least one department is required."
            );
        }

        return departmentIds;
    }

    static void addStudent() {

        String name = readRequired("Student name: ");
        String email = readRequired("Email: ");
        String department = readRequired("Department: ");
        double cgpa = readCgpa("CGPA (0-10): ");

        if (name.isEmpty() || email.isEmpty() || department.isEmpty()) {
            System.out.println("All fields are required.");
            return;
        }

        studentRepo.addStudent(name, email, department, cgpa);
    }

    static void updateStudent() {

        long id = readId("Student ID to update: ");

        String name = readRequired("New name: ");
        String email = readRequired("New email: ");
        String department = readRequired("New department: ");
        double cgpa = readCgpa("New CGPA: ");

        if (name.isEmpty() || email.isEmpty() || department.isEmpty()) {
            System.out.println("All fields are required.");
            return;
        }

        studentRepo.updateStudent(id, name, email, department, cgpa);
    }

    static void addCompany() {

        String name = readRequired("Company name: ");

        if (name.isEmpty()) {
            System.out.println("Company name cannot be blank.");
            return;
        }

        companyRepo.addCompany(name);
    }

    static void updateCompany() {

        long id = readId("Company ID to update: ");
        String name = readRequired("New company name: ");

        if (name.isEmpty()) {
            System.out.println("Company name cannot be blank.");
            return;
        }

        companyRepo.updateCompany(id, name);
    }

    static void addJob() {

        long companyId = readId("Company ID: ");
        String role = readRequired("Job role: ");
        double minimumCgpa = readCgpa("Minimum CGPA: ");
        Set<Long> departments = readDepartments();

        if (role.isEmpty()) {
            System.out.println("Job role cannot be blank.");
            return;
        }

        jobRepo.addJob(companyId, role, minimumCgpa, departments);
    }

    static void updateJob() {

        long jobId = readId("Job ID to update: ");
        long companyId = readId("New company ID: ");

        String role = readRequired("New job role: ");
        double minimumCgpa = readCgpa("New minimum CGPA: ");
        Set<Long> departments = readDepartments();

        if (role.isEmpty()) {
            System.out.println("Job role cannot be blank.");
            return;
        }

        jobRepo.updateJob(
            jobId, companyId, role, minimumCgpa, departments
        );
    }

    static void applyForJob() {

        long studentId = readId("Student ID: ");
        long jobId = readId("Job ID: ");

        applicationRepo.applyForJob(studentId, jobId);
    }

    static void listApplicationsForJob() {

        long jobId = readId("Job ID: ");

        applicationRepo.findApplicationsForJob(jobId);
    }

    static void updateApplicationStatus() {

        long applicationId = readId("Application ID: ");

        System.out.println("1. SHORTLISTED");
        System.out.println("2. REJECTED");

        String choice = readRequired("Choose new status: ");

        String status;

        switch (choice) {
            case "1" -> status = "SHORTLISTED";
            case "2" -> status = "REJECTED";
            default -> {
                System.out.println("Invalid status choice.");
                return;
            }
        }

        applicationRepo.updateApplicationStatus(applicationId, status);
    }
}
