-- COLLEGE PLACEMENT PORTAL
-- Level 2: SQL Queries
-- Total: 15 queries

-- 1. Display all students with their department names.
SELECT s.student_id, s.name, s.email,
       d.department_name, s.cgpa
FROM students s
JOIN departments d
ON s.department_id = d.department_id;


-- 2. Display all jobs with their company names.
SELECT j.job_id, j.job_title,
       c.company_name, j.minimum_cgpa
FROM jobs j
JOIN companies c
ON j.company_id = c.company_id;


-- 3. Display students and the jobs they applied for.
SELECT s.name AS student_name,
       j.job_title,
       a.status
FROM applications a
JOIN students s
ON a.student_id = s.student_id
JOIN jobs j
ON a.job_id = j.job_id;


-- 4. Display all students in ascending order of name.
SELECT student_id, name, email, cgpa
FROM students
ORDER BY name ASC;


-- 5. Display students whose CGPA is 8.0 or above.
SELECT student_id, name, cgpa
FROM students
WHERE cgpa >= 8.0;


-- 6. Count the total number of students.
SELECT COUNT(*) AS total_students
FROM students;


-- 7. Find the average CGPA of all students.
SELECT AVG(cgpa) AS average_cgpa
FROM students;


-- 8. Count the number of jobs offered by each company.
SELECT c.company_name,
       COUNT(j.job_id) AS total_jobs
FROM companies c
LEFT JOIN jobs j
ON c.company_id = j.company_id
GROUP BY c.company_name;


-- 9. Display students who have applied for at least one job.
SELECT student_id, name, email
FROM students
WHERE student_id IN (
    SELECT student_id
    FROM applications
);


-- 10. Display jobs whose minimum CGPA is greater
-- than the average minimum CGPA of all jobs.
SELECT job_id, job_title, minimum_cgpa
FROM jobs
WHERE minimum_cgpa > (
    SELECT AVG(minimum_cgpa)
    FROM jobs
);


-- 11. Display applications with status SHORTLISTED.
SELECT application_id, student_id, job_id, status
FROM applications
WHERE status = 'SHORTLISTED';


-- 12. Display students who have not applied for any job.
SELECT s.student_id, s.name
FROM students s
WHERE NOT EXISTS (
    SELECT 1
    FROM applications a
    WHERE a.student_id = s.student_id
);


-- 13. Display the number of applications for each job.
SELECT j.job_id, j.job_title,
       COUNT(a.application_id) AS total_applications
FROM jobs j
LEFT JOIN applications a
ON j.job_id = a.job_id
GROUP BY j.job_id, j.job_title;


-- 14. Display the students who are eligible for each job.
-- Eligibility requires the minimum CGPA and an allowed department.
SELECT s.student_id, s.name, s.cgpa,
       j.job_id, j.job_title
FROM students s
JOIN job_departments jd
ON s.department_id = jd.department_id
JOIN jobs j
ON jd.job_id = j.job_id
WHERE s.cgpa >= j.minimum_cgpa;


-- 15. Display the number of applications for each status.
SELECT status,
       COUNT(*) AS total_applications
FROM applications
GROUP BY status
ORDER BY status;